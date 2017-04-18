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
    public ArrayList<ArrayList<String>> body;

    Table(String name, ParseSource ps) {
        tb = new TableBuilder(name, ps);
        this.title = tb.gettaTitle();
        this.body = tb.gettaBody();
        this.name = name;
    }

    Table(String name, Map<String, Integer> t, ArrayList<ArrayList<String>> b) {
        title = t;
        body = b;
        this.name = name;
    }

    String getname(){
        return this.name;
    }

    int getColumnNum() {
        System.out.println(title);
        System.out.println(title.keySet());
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
     * if title already in title map, nothing change
     * if no, append title in the last and add corresponding index, add a ampty column below the new title*/
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
            for (int i = 0; i < to_check.size() - std; i += 1) {
                to_check.add((String)null);
            }
        } else if (to_check.size() > std) {
            throw new RuntimeException("the added element is too lone!");
        }
    }

    /* add horizontal column to the body
    *  if the name exists in the title, replace the old one
    *  if the list's length is not equal to the body, leave the rest in blank */
    void columnAdd(String title_name, ArrayList<String> col) {
        checkSize(col, body.size());
        titleAdd(title_name);
        try {
            int index = getTitleIndex(name);
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
            int index = getTitleIndex(name);
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).remove(index);
            }
            title.remove(name);
        }
        catch (RuntimeException e) {
            throw new RuntimeException
                    ("Can not delete the title because does not exist in the table");
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
