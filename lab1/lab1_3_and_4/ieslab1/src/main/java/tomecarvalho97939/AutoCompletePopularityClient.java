package tomecarvalho97939;

import java.util.Scanner;

public class AutoCompletePopularityClient {
    public static void main(String[] args) {
        Autocomplete ac = new AutocompletePopularity();
        ac.loadNames();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Search for ('Enter' for quit): ");
            String prompt = sc.nextLine();
            if (prompt.equals(""))
                break;
            ac.search(prompt);
        }
    }
}
