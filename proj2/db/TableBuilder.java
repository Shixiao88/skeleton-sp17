package db;

import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */

/* return a Table Object */
public class TableBuilder {

    private String name;
    private Map<String, Integer> title;
    private ArrayList<ArrayList<String>> body;

    TableBuilder(String name, ParseSource ps) {
        this.name = name;
        ArrayList<String> origin_title = ps.parseSourceTitle();
        this.body = ps.parseSourceBody();
        this.title = convertTitle(origin_title);
    }

    TableBuilder(String name, Map<String, Integer> title, ArrayList<ArrayList<String>> body) {
        this.name = name;
        this.body = body;
        this.title = title;
    }

    String gettaName() {
        return this.name;
    }

    Map<String, Integer> gettaTitle() {
        return this.title;
    }

    ArrayList<ArrayList<String>> gettaBody() {
        return this.body;
    }

    Map<String, Integer> convertTitle(ArrayList<String> origin_title) {
        Map<String, Integer> temp_title = new LinkedHashMap<>();
        try {
            for (int index = 0; index < origin_title.size(); index += 1 ) {
                temp_title.put(origin_title.get(index), index);
            }
            return temp_title;
        } catch (NullPointerException e) {
            System.out.println ("there is no title");
            return temp_title;
        }
    }
}
