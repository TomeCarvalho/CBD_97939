import org.neo4j.driver.*;

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

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public static void main(String[] args) throws Exception {
        try (App app = new App("neo4j://localhost:7687", "neo4j", "password")) {
            app.test();
        }
    }
}
