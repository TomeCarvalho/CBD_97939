package tomecarvalho97939;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class AutocompleteAlpha implements Autocomplete {
    private final Jedis jedis;

    public AutocompleteAlpha() {
        jedis = new Jedis();
    }

    @Override
    public boolean loadNames() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("target/classes/names.txt")));
            String line = reader.readLine();
            while (line != null) {
                jedis.set(line, "");
                line = reader.readLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void search(String prompt) {
        Set<String> keys = new TreeSet<>(jedis.keys(prompt + "*"));
        keys.forEach(System.out::println);
    }
}
