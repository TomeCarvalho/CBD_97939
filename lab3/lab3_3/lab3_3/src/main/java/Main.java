import com.datastax.oss.driver.api.core.CqlSession;

public class Main {
    private CqlSession session;

    public static void main(String[] args) {
        // This will connect to localhost port 9042 (default)
        try (CqlSession session = CqlSession.builder().withKeyspace("lab3_2").build()) {
            // a) Insertion, editing and searching demonstration

            // Searching: Show the IDs of the videos uploaded by johndoe
            session.execute("select * from videos_by_author where author_username = 'johndoe'")
                    .forEach(row -> System.out.println(row.getInt("video_id")));

            // Insertion: Add a new user
            session.execute("insert into users(username, name, email, register_date) " +
                    "values ('newuser', 'Nu Yuzer', 'newuser@cbd.com', '2021-12-19');");

            // Editing: Edit the new user
            session.execute("update users set name='New User' where username='newuser'");

            // b) 4 queries from exercise 3.02

            // The following queries are from 3.2 d)

            System.out.println("1. Os últimos 3 comentários introduzidos para um vídeo;");
            session.execute("select * from comments_by_video where video_id = 2 limit 3")
                    .forEach(row -> System.out.println(row.getString("comment")));

            System.out.println("3. Todos os vídeos com a tag Aveiro;");
            session.execute("select * from videos_by_tag where tag = 'Aveiro'")
                    .forEach(row -> System.out.println(row.getInt("video_id")));

            System.out.println("5. Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num " +
                    "determinado período de tempo (Agosto de 2017, por exemplo);\n" +
                    "Videos uploaded by johndoe in 2020");
            session.execute("select * from videos_by_author where author_username = 'johndoe' " +
                    "and upload_ts >= '2020-01-01' and upload_ts < '2021-01-01';")
                    .forEach(row -> System.out.println(row.getInt("video_id")));

            System.out.println("7. Todos os seguidores (followers) de determinado vídeo;");
            session.execute("select * from video_followers where video_id = 1")
                    .forEach(row -> System.out.println(row.getString("username")));
        }
    }
}