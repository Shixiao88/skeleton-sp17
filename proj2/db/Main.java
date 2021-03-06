package db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Xiao Shi on 2017/4/21.
 */
public class Main {

    private static final String EXIT   = "exit";
    private static final String PROMPT = "> ";

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Database db = new Database();
        System.out.print(PROMPT);

        String line;
        while ((line = in.readLine()) != null) {
            if (EXIT.equals(line)) {
                break;
            }
            try {
                if (!line.trim().isEmpty()) {
                    String result = db.transact(line);
                    if (result.length() > 0) {
                        System.out.println(result);
                    }
                }
                System.out.print(PROMPT);
            } catch (RuntimeException e) {
                System.out.println("ERROR: " + e);
                System.out.print(PROMPT);
            }
        }
        in.close();
    }
}
