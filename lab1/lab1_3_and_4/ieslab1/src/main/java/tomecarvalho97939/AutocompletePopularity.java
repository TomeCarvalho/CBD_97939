package tomecarvalho97939;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class AutocompletePopularity implements Autocomplete {
    private final Jedis jedis;

    public AutocompletePopularity() {
        jedis = new Jedis();
    }

    @Override
    public boolean loadNames() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("target/classes/nomes-pt-2021.csv")));
            String line = reader.readLine();
            String name;
            int popularity;
            while (line != null) {
                String[] lineSplit = line.split(";");
                name = lineSplit[0];
                popularity = Integer.parseInt(lineSplit[1]);
                jedis.set(name, Integer.toString(popularity));
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
        Set<String> keys = jedis.keys(prompt + "*");
        Map<String, Integer> nameMap = new HashMap<>();
        keys.forEach(key -> nameMap.put(key, Integer.parseInt(jedis.get(key))));
        Stream<Map.Entry<String, Integer>> sorted = nameMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        sorted.forEach(entry -> System.out.println(entry.getKey()));
    }
}
