package db;

import sun.plugin.javascript.navig.Array;

import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */

/* return a Table Object */
public class TableBuilder {

    private String name;
    private ArrayList<String> origin_title;
    private Map<String, Integer> title;
    private ArrayList<ArrayList<String>> body;

    TableBuilder(String name, ParseSource ps) {
        this.name = name;
        this.origin_title = ps.parseSourceTitle();
        this.body = ps.parseSourceBody();
        convertTitle(origin_title);
    }

    TableBuilder(String name, Map<String, Integer> title, ArrayList<ArrayList<String>> body) {
        this.name = name;
        this.body = body;
        this.title = title;
    }

    String gettaName() {
        return name;
    }

    Map<String, Integer> gettaTitle() {
        return title;
    }

    ArrayList<ArrayList<String>> gettaBody() {
        return body;
    }

    void convertTitle(ArrayList<String> origin_title) {
        try {
            for (int index = 0; index < origin_title.size(); index += 1 ) {
                title.put(origin_title.get(index), index);
            }
        } catch (NullPointerException e) {
            System.out.println ("there is no title");
        }
    }
}
