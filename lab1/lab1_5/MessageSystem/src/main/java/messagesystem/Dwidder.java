package messagesystem;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.*;

public class Dwidder {
    private static final int dateLength = LocalDateTime.now().toString().length();
    private static final String USERSET = "userset";
    private static final Jedis jedis = new Jedis();
    private static final String usernameRegex = "[a-zA-Z0-9_]+";
    public static void main(String[] args) {
        if (args[0].equals("flushdb"))
                jedis.flushDB();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String cmd = sc.nextLine();
            String[] cmdSplit = cmd.split("\\(");
            String function = cmdSplit[0];
//            for (String s : cmdSplit)
//                System.out.println(s);
            switch (function) {
                case "register": // register(username)
                    String username = cmdSplit[1].substring(0, cmdSplit[1].length() - 1);
                    // System.out.println("username: " + username);
                    if (username.matches(usernameRegex))
                        register(username);
                    else
                        System.err.println("Invalid username. Use only letters (no diacritics), numbers and underscores.");
                    break;
                case "follow": // follow(follower, followed)
                    String[] argSplit = cmdSplit[1].split(",");
                    String follower = argSplit[0].strip();
                    argSplit[1] = argSplit[1].strip();
                    String followed = argSplit[1].substring(0, argSplit[1].length() - 1);
                    follow(follower, followed);
                    break;
                case "unfollow": // unfollow(follower, followed)
                    String[] argSplit2 = cmdSplit[1].split(",");
                    String follower2 = argSplit2[0].strip();
                    argSplit2[1] = argSplit2[1].strip();
                    String followed2 = argSplit2[1].substring(0, argSplit2[1].length() - 1);
                    unfollow(follower2, followed2);
                    break;
                case "dweed": // dweed(username, text)
                    String[] argSplit3 = cmdSplit[1].split(",");
                    String username2 = argSplit3[0].strip();
                    argSplit3[1] = argSplit3[1].strip();
                    String text = argSplit3[1].substring(0, argSplit3[1].length() - 1);
                    dweed(username2, text);
                    break;
                case "feed": // feed(username)
                    String username3 = cmdSplit[1].substring(0, cmdSplit[1].length() - 1);
                    feed(username3);
                    break;
                case "followers": // followers(username)
                    String username4 = cmdSplit[1].substring(0, cmdSplit[1].length() - 1);
                    followers(username4);
                    break;
                case "sleep": // Sleeps for a second, so the example file can be used correctly
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "exit":
                    return;
            }
        }
    }

    // Register a new user
    private static boolean register(String username) {
        if (jedis.sismember(USERSET, username)) {
            System.out.println("User " + username + " already exists.");
            return false;
        }
        if (!username.matches(usernameRegex)) {
            System.out.println("Invalid username. Use only letters (no diacritics), numbers and underscores.");
            return false;
        }
        jedis.sadd(USERSET, username);
        System.out.println("User " + username + " registered.");
        return true;
    }

    // Post a dweed (format example below)
    //    RickAstley                   <- username (added automatically)
    //    never gonna give you up    | <- dweed text
    //    never gonna let you down   |
    //    2021-10-22T20:39:59.123      <- submission time (added automatically)

    private static boolean dweed(String username, String text) {
        if (!jedis.sismember(USERSET, username)) {
            System.out.println("User " + username + " doesn't exist.");
            return false;
        }
        String dweed = username + "\n" + text + "\n" + LocalDateTime.now();
        jedis.lpush(username + ":dweeds", dweed);
        System.out.println(username + " dweeded.");
        return true;
    }

    // Follow a user
    private static boolean follow(String follower, String followed) {
        if (!jedis.sismember(USERSET, follower)) {
            System.out.println(follower + " is not an user.");
            return false;
        }
        if (!jedis.sismember(USERSET, followed)) {
            System.out.println(followed + " is not an user.");
            return false;
        }
        jedis.sadd(followed + ":flwrs", follower);
        jedis.sadd(follower + ":flwd", followed);
        System.out.println(follower + " is now following " + followed + ".");
        return true;
    }

    // Unfollow a user
    private static boolean unfollow(String follower, String followed) {
        if (!jedis.sismember(USERSET, follower)) {
            System.out.println(follower + " is not an user.");
            return false;
        }
        if (!jedis.sismember(USERSET, followed)) {
            System.out.println(followed + " is not an user.");
            return false;
        }
        jedis.srem(followed + ":flwrs", follower);
        jedis.srem(follower + ":flwd", followed);
        System.out.println(follower + " is no longer following " + followed);
        return true;
    }

    // Show a user's feed (dweeds from the users they follow)
    private static boolean feed(String username) {
        if (!jedis.sismember(USERSET, username)) {
            System.out.println(username + " is not an user.");
            return false;
        }
        List<String> dweeds = new ArrayList<>();
        for (String flwd : jedis.smembers(username + ":flwd"))
            dweeds.addAll(jedis.lrange(flwd + ":dweeds", 0, -1));
        dweeds.sort(Comparator.comparing(Dwidder::date).reversed()); // show most recent dweeds first
        System.out.println(username + "'s feed");
        if (dweeds.size() > 0)
            dweeds.forEach(System.out::println);
        else
            System.out.println("Wow, such empty.");
        System.out.println();
        return true;
    }

    // Show a user's followers
    private static boolean followers(String username) {
        if (!jedis.sismember(USERSET, username)) {
            System.out.println(username + " is not an user.");
            return false;
        }
        Set<String> followers = jedis.smembers(username + ":flwrs");
        StringJoiner followersString = new StringJoiner(",");
        followers.forEach(followersString::add);
        System.out.println(username + "'s followers (" + followers.size() + "): " + followersString);
        return true;
    }

    // Get the date and time of a dweed
    private static LocalDateTime date(String dweed) {
        int len = dweed.length();
        String dateString = dweed.substring(len - dateLength, len);
        System.out.println("dateString: " + dateString);
        return LocalDateTime.parse(dateString);
    }
}
