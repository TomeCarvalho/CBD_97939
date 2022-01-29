import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App implements AutoCloseable {
    private final Driver driver;

    public App(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void test() {
        try (Session session = driver.session(SessionConfig.forDatabase("lab44"))) {
            Result result = session.run("MATCH (n) RETURN n");
            while (result.hasNext()) {
                Map<String, Object> row = result.next().asMap();
                row.forEach((key, value) -> System.out.println(key + ": " + value));
                System.out.println();
            }
        }
    }

    private void createConstraints(Session session) {
        try {
            session.run("CREATE CONSTRAINT FOR (trial:Trial) REQUIRE trial.match_id IS UNIQUE");
            session.run("CREATE CONSTRAINT FOR (player:Player) REQUIRE player.name IS UNIQUE");
            session.run("CREATE CONSTRAINT FOR (realm:Realm) REQUIRE realm.name IS UNIQUE");
            session.run("CREATE CONSTRAINT FOR (map:Map) REQUIRE map.name IS UNIQUE");
        } catch (ClientException clientException) {
            System.out.println(clientException);
        }
    }

    private void loadNodes(Session session) {
        session.run("LOAD CSV WITH HEADERS FROM \"file:///matches.csv\" AS row\n" +
                "MERGE (trial:Trial {match_id: row.match_id})\n" +
                "SET trial.length = toInteger(row.length)\n" +
                "MERGE (player0:Player {name: row.killer_player})\n" +
                "MERGE (player1:Player {name: row.survivor0_player})\n" +
                "MERGE (player2:Player {name: row.survivor1_player})\n" +
                "MERGE (player3:Player {name: row.survivor2_player})\n" +
                "MERGE (player4:Player {name: row.survivor3_player})\n" +
                "MERGE (realm:Realm {name: row.realm})\n" +
                "MERGE (map:Map {name: row.map});");
    }

    private void loadRelationships(Session session) {
        session.run("LOAD CSV WITH HEADERS FROM \"file:///matches.csv\" AS row\n" +
                "MATCH (killer_player:Player {name: row.killer_player})," +
                "(trial:Trial {match_id: row.match_id})," +
                "(survivor0_player:Player {name: row.survivor0_player})," +
                "(survivor1_player:Player {name: row.survivor1_player})," +
                "(survivor2_player:Player {name: row.survivor2_player})," +
                "(survivor3_player:Player {name: row.survivor3_player})," +
                "(realm:Realm {name: row.realm})," +
                "(map:Map {name: row.map})\n" +

                "MERGE (killer_player)-[:PLAYED_AS_KILLER {killer: row.killer, kills: toInteger(row.kills), hooks: toInteger(row.hooks), result: row.killer_result, bloodpoints: toInteger(row.killer_bp)}]->(trial)\n" +
                "MERGE (survivor0_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor0, escaped: row.survivor0_escaped, bloodpoints: toInteger(row.survivor0_bp)}]->(trial)\n" +
                "MERGE (survivor1_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor1, escaped: row.survivor1_escaped, bloodpoints: toInteger(row.survivor1_bp)}]->(trial)\n" +
                "MERGE (survivor2_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor2, escaped: row.survivor2_escaped, bloodpoints: toInteger(row.survivor2_bp)}]->(trial)\n" +
                "MERGE (survivor3_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor3, escaped: row.survivor3_escaped, bloodpoints: toInteger(row.survivor3_bp)}]->(trial)\n" +
                "MERGE (map)-[:BELONGS_TO_REALM]->(realm)\n" +
                "MERGE (trial)-[:PLAYED_IN_MAP]->(map)");
    }

    public void loadCsv() {
        try (Session session = driver.session(SessionConfig.forDatabase("lab44"))) {
            createConstraints(session);
            loadNodes(session);
            loadRelationships(session);
        }
    }


    @Override
    public void close() throws Exception {
        driver.close();
    }

    // Query 1: For each player, show their average number of kills and hooks per match played as killer and the number of matches
    //          Sort the results by the average number of kills (highest to lowest)
    private Result avgKillsAndHooks(Session session) {
        return session.run("MATCH (player:Player)-[r:PLAYED_AS_KILLER]->(Trial)\n" +
                "WITH player, AVG(r.kills) AS avg_kills, AVG(r.hooks) AS avg_hooks, COUNT(r) AS num_killer_matches\n" +
                "RETURN player, avg_kills, avg_hooks, num_killer_matches\n" +
                "ORDER BY avg_kills DESC");
    }

    // Query 2: Show how many times a player has been killed by every other player
    //          Sort the results by that value, from highest to lowest
    private Result timesKilledBy(Session session, String playerName) {
        return session.run("MATCH (killer:Player)-[:PLAYED_AS_KILLER]->(Trial)" +
                "<-[r:PLAYED_AS_SURVIVOR {escaped: \"False\"}]-(Player {name: \"" + playerName + "\"})\n" +
                "WITH killer, COUNT(r) AS times_killed_by\n" +
                "RETURN killer, times_killed_by");
    }

    // Query 3: Show all the trials where a list of players played together as survivors
    private Result trialsWithSurvivorGroup(Session session, List<String> survivors) {
        assert (survivors.size() > 0 && survivors.size() < 5);
        StringBuilder query = new StringBuilder("MATCH ");
        StringBuilder returnSb = new StringBuilder("\nRETURN Trial");
        for (int i = 0; i < survivors.size(); i++) {
            query.append(String.format("(player%d:Player {name: \"%s\"})-[r%d:PLAYED_AS_SURVIVOR]->(Trial), ", i, survivors.get(i), i));
            returnSb.append(String.format(", player%d, r%d", i, i));
        }
        query.setLength(query.length() - 2);
        query.append(returnSb);
        // System.out.println("trialsWithDoubble query: " + query);
        return session.run(query.toString());
    }

    // Query 4: Show each player's average kills per killer character, sorted highest to lowest
    //   Sort by the following attributes, with the following priorities:
    //     1. The player name; 2. The average kills value (highest to lowest); 3. The killer name
    private Result avgKillsPerKillerPerPlayer(Session session) {
        return session.run("MATCH (Player)-[r:PLAYED_AS_KILLER]->(Trial)\n" +
                "WITH Player, r.killer AS killer, AVG(r.kills) AS avg_kills\n" +
                "RETURN Player, killer, avg_kills\n" +
                "ORDER BY Player.name, avg_kills DESC, killer");
    }

    // Query 5: Calculate each players' playtime in hours: global and separated by role (killer/survivor)
    //  Sort by the global playtime
    //  Note: the trial's length attribute is in minutes
    private Result playtimes(Session session) {
        return session.run("MATCH (Player)-[:PLAYED_AS_SURVIVOR]->(trial_survivor:Trial)\n" +
                "WITH Player, SUM(trial_survivor.length) / 60 AS playtime_survivor\n" +
                "MATCH (Player)-[:PLAYED_AS_KILLER]->(trial_killer:Trial)\n" +
                "WITH Player, playtime_survivor, SUM(trial_killer.length) / 60 AS playtime_killer\n" +
                "RETURN Player, playtime_survivor, playtime_killer, playtime_survivor + playtime_killer AS playtime\n" +
                "ORDER BY playtime");
    }

    // Query 6: Calculate the survivor escape rate for every map and show the corresponding realm for each.
    //   Sort by realm name and then escape rate (lowest to highest)
    private Result mapEscapeRates(Session session) {
//        match (Player)-[r:PLAYED_AS_KILLER]->(Trial)-[:PLAYED_IN_MAP]->(Map)-[:BELONGS_TO_REALM]->(Realm)
//        return Realm, Map, (4 - (sum(r.kills * 1.0) / count(r))) / 4 as escape_rate
//        order by Realm.name, escape_rate
        return session.run("");
    }

    public static void main(String[] args) throws Exception {
        try (App app = new App("neo4j://localhost:7687", "neo4j", "password")) {
            app.loadCsv();
            try (Session session = app.driver.session(SessionConfig.forDatabase("lab44"))) {
                System.out.println(app.avgKillsAndHooks(session));
                System.out.println(app.timesKilledBy(session, "praxz"));
                System.out.println(app.trialsWithSurvivorGroup(session, Arrays.asList("praxz", "Trivialiac", "ScottJund")));
                System.out.println(app.avgKillsPerKillerPerPlayer(session));
                System.out.println(app.playtimes(session));
            }
        }
    }
}
