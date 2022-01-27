import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;

public class App implements AutoCloseable {
    private final Driver driver;

    public App(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void test() {
        try (Session session = driver.session(SessionConfig.forDatabase("lab44"))) {
            Result result = session.run("MATCH (n) RETURN n");
            System.out.println(result);
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
                "SET trial.length = row.length\n" +
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

                "MERGE (killer_player)-[:PLAYED_AS_KILLER {killer: row.killer, kills: row.kills, hooks: row.hooks, result: row.killer_result, bloodpoints: row.killer_bp}]->(trial)\n" +
                "MERGE (survivor0_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor0, escaped: row.survivor0_escaped, bloodpoints: row.survivor0_bp}]->(trial)\n" +
                "MERGE (survivor1_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor1, escaped: row.survivor1_escaped, bloodpoints: row.survivor1_bp}]->(trial)\n" +
                "MERGE (survivor2_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor2, escaped: row.survivor2_escaped, bloodpoints: row.survivor2_bp}]->(trial)\n" +
                "MERGE (survivor3_player)-[:PLAYED_AS_SURVIVOR {survivor: row.survivor3, escaped: row.survivor3_escaped, bloodpoints: row.survivor3_bp}]->(trial)\n" +
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

    public static void main(String[] args) throws Exception {
        try (App app = new App("neo4j://localhost:7687", "neo4j", "password")) {
            app.loadCsv();
            app.test();
        }
    }
}
