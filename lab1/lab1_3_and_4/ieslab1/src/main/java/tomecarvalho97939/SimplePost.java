package tomecarvalho97939;

import java.util.*;
import redis.clients.jedis.Jedis;

public class SimplePost {
    private Jedis jedis;
//    public static String USERS = "users"; // Key set for users' name
    public static String LISTUSERS = "list_users";
    public static String MAPUSERS = "map_users";

    public SimplePost() {
        this.jedis = new Jedis("localhost");
    }

    public void saveListUser(String username) {
        jedis.rpush(LISTUSERS, username);
    }

    public List<String> getListUsers() {
        return jedis.lrange(LISTUSERS, 0, -1);
    }

    public void saveMapUser(String id, String username) {
        jedis.hset(MAPUSERS, id, username);
    }

    public Map<String, String> getMapUsers() {
        return jedis.hgetAll(MAPUSERS);
    }

//    public void saveUser(String username) {
//        jedis.sadd(USERS, username);
//    }

//    public Set<String> getUser() {
//        return jedis.smembers(USERS);
//    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

    public static void main(String[] args) {
        SimplePost board = new SimplePost();
        // set some users
//        String[] users = {"Ana", "Pedro", "Maria", "Luis"};
//        for (String user : users)
//            board.saveUser(user);
//        board.getAllKeys().forEach(System.out::println);
//        board.getUser().forEach(System.out::println);

        // list users
        System.out.println("--- List Users ---");
        List<String> listUsers = new ArrayList<String>(
                Arrays.asList("Dwight", "Claudette", "Meg", "Jake")
        );
        listUsers.forEach(board::saveListUser);
        board.getAllKeys().forEach(System.out::println);
        board.getListUsers().forEach(System.out::println);

        // map users
        System.out.println("\n--- Map Users ---");
        Map<String, String> mapUsers = Map.of(
                "5", "Nea",
                "6", "Laurie",
                "7", "Ace",
                "8", "David"
        );
        mapUsers.forEach(board::saveMapUser);
        board.getAllKeys().forEach(System.out::println);
        board.getMapUsers().forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
