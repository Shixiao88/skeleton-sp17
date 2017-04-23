package db;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/18.
 */

/* return a Table Object */
public class TableBuilder {

    private String name;
    private Map<MSQColName, Integer> title;
    private ArrayList<ArrayList<MSQContainer>> body;

    TableBuilder(String name, ParseSource ps) {

        ArrayList<String> origin_title = ps.parseSourceTitle();
        ArrayList<ArrayList<String>> original_body = ps.parseSourceBody();
        this.name = name;
        this.title = convertTitle(origin_title);
        this.body = convertBody(original_body);
    }

    TableBuilder(String name, Map<MSQColName, Integer> title, ArrayList<ArrayList<MSQContainer>> body) {
        this.name = name;
        this.body = body;
        this.title = title;
    }

    String gettaName() {
        return this.name;
    }

    Map<MSQColName, Integer> gettaTitle() {
        return this.title;
    }

    ArrayList<ArrayList<MSQContainer>> gettaBody() {
        return this.body;
    }

    Map<MSQColName, Integer> convertTitle(ArrayList<String> origin_title) {
        Map<MSQColName, Integer> temp_title = new LinkedHashMap<>();
        try {
            for (int index = 0; index < origin_title.size(); index += 1 ) {
                MSQColName col = new MSQColName(origin_title.get(index));
                temp_title.put(col, index);
            }
            return temp_title;
        } catch (NullPointerException e) {
            System.out.println ("there is no title");
            return temp_title;
        }
    }

    ArrayList<ArrayList<MSQContainer>> convertBody(ArrayList<ArrayList<String>> original_body) {
        ArrayList<ArrayList<MSQContainer>> temp_body = new ArrayList<>();

        for (int line_index = 0; line_index<original_body.size(); line_index += 1) {
            for (int i = 0; i < original_body.get(line_index).size(); i += 1) {
                String contained = original_body.get(line_index).get(i);
                ArrayList<MSQColName> cols_keys= new ArrayList<>(title.keySet());
                String col_type = cols_keys.get(i).getColType();
                temp_body.get(line_index).set(i, new MSQContainer(contained, col_type));
            }
        }
        return temp_body;
    }
}
