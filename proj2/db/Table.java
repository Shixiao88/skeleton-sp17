package db;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class Table {

    private String name;
    private TableBuilder tb;
    private Map<String, Integer> title;
    private ArrayList<ArrayList<String>> body;

    Table(TableBuilder tb) {
        this.title = tb.gettaTitle();
        this.body = tb.gettaBody();
        this.name = tb.gettaName();
    }

    Table(String name, Map<String, Integer> t, ArrayList<ArrayList<String>> b) {
        title = t;
        body = b;
        this.name = name;
    }

    String getname(){
        return this.name;
    }

    Map<String, Integer> gettitle() {
        return this.title;
    }

    ArrayList<ArrayList<String>> getbody() {
        return this.body;
    }

    int getColumnNum() {
        return title.keySet().size();
    }

    int getRowNum() {
        return body.size();
    }

    boolean titleIn(String name) {
        return title.containsKey(name);
    }

    /* get the index of a title name, if no exist return null*/
    int getTitleIndex(String name) {
        if (titleIn(name)) {
            return title.get(name);
        }
        else {
            throw new RuntimeException("the given title is not in the table.");
        }
    }

    /* helper method for columnAdd and columnDel.
     * also helper method for Join class
     * if title already in title map, nothing change
     * if no, append title in the last and add corresponding index, add an empty column below the new title*/
    private void titleAdd(String name){
        try {getTitleIndex(name);}
        catch (RuntimeException e) {
            int size = title.size();
            title.put(name, size);
        }
    }

    /* helper method for columneAdd and rowAdd.
    *  to check if the added in element's lengh and the table corresponding length:
    *  - if the added in element is too short, compelte the rest as null elements;
    *  - if the added in element is too long, raise an error
    *  - else do nothing */
    private void checkSize(ArrayList<String> to_check, int std) {
        if (to_check.size() < std) {
            int to_check_size = to_check.size();
            int discranpcy = std - to_check_size;
            for (int i = 0; i < discranpcy; i += 1) {
                to_check.add((String)null);
            }
        } else if (to_check.size() > std) {
            throw new RuntimeException("the added element is too long!");
        }
    }

    /* add horizontal column to the body
    *  if the name exists in the title, replace the old one
    *  if the list's length is not equal to the body, leave the rest in blank */
    void columnAdd(String title_name, ArrayList<String> col) {
        checkSize(col, body.size());
        titleAdd(title_name);
        try {
            int index = getTitleIndex(title_name);
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).set(index,col.get(i));
            }
        }
        catch (RuntimeException e) {
            int index = title.size();
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).add(col.get(i));
            }
        }
    }

    /* delete horizontal column according to the list
    *  if the column do not exist in titles, raise an error*/
    void columnDel(String title_name) {
        try {
            int index = getTitleIndex(title_name);
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).remove(index);
            }
            titleDel(title_name);
        }
        catch (RuntimeException e) {
            throw new RuntimeException
                    ("Can not delete the title because does not exist in the table");
        }
    }

    /* helper method for COLUMNDEL, if you delete one title, all the title after that should be shifted
     *  it means that all titles has greater values should have value minus one*/
    void titleDel(String title_name) {
        int index = getTitleIndex(title_name);
        title.remove(title_name);
        for (String t : title.keySet()) {
            if (title.get(t) > index) {
                title.put(t, title.get(t) - 1);
            }
        }
    }

    /* add a row to the end of body
    *  if the row's length is not equal to the body, leave the rest in blank */
    void rowAdd(ArrayList<String> row){
        checkSize(row, getColumnNum());
        body.add(row);
    }

    /* add a row just in front of the given index
     * if the row's length is not equal to the body, leave the rest in blank */
    void rowAdd(ArrayList<String> row, int index) {
        checkSize(row, getColumnNum());
        body.add(index, row);

    }

    /* delete the last row??*/
    void rowDel() {}

    /* repr of the whole table*/
    void String() {}
}
