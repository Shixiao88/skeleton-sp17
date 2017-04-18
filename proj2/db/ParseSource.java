package db;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class ParseSource {

    private String title_container;
    private String body_container;
    private ArrayList<String> title;
    private ArrayList<ArrayList<String>> body;
    private ArrayList<String> s;

    public ParseSource(ArrayList<String> al) {
        title_container = "";
        body_container = "";
        title = new ArrayList<>();
        body = new ArrayList<ArrayList<String>>();
        s = (ArrayList<String>)al.clone();
    }
    /* receive a list of long strings and return
     * map as table title */
    ArrayList<String> parseSourceTitle() {
        try {
            String title_s = s.get(0);
            s.remove(0);
            for (int i=0; i<title_s.length(); i+=1) {
                Character c = title_s.charAt(i);
                if (c == ',') {
                    clearTitleBuffer();
                    continue;
                } else if(c == '\'' || c == '"') {
                    continue;
                }
                titleBuffer(c);
            } clearTitleBuffer();
        } catch (NullPointerException e) {
            System.out.println("the table is empty! error index of the title");
            return new ArrayList<String>();
        }
        return title;
    }

    ArrayList<ArrayList<String>> parseSourceBody() {
        try {
            for (int line_i=0; line_i<s.size(); line_i+=1) {
                String line = s.get(line_i);
                for (int i=0; i<line.length(); i+=1) {
                    Character c = line.charAt(i);
                    if (c == ',') {
                        clearBodyBuffer(line_i);
                        continue;
                    } else if (c == '\'' || c == '"') {
                        continue;
                    }
                    bodyBuffer(c);
                }
                clearBodyBuffer(line_i);
            }
        // there is NAN and no value exception that i need to complete later.
        } catch (NullPointerException e) {
            System.out.println ("error index of the body");
            return null;
        }
        return body;
    }

    void titleBuffer(Character word) {
        title_container += word;
    }

    void clearTitleBuffer() {
        title.add(title_container);
        title_container = "";
    }

    void bodyBuffer(Character word) {
        body_container += word;
    }

    void clearBodyBuffer(int index) {
        if (index == body.size()) {
            body.add(new ArrayList<String>());
        }
            body.get(index).add(body_container);
        body_container = "";
    }
}
