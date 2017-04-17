package db;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class Table {

    private String name;
    private Map<String, Integer> title;
    private ArrayList<String> body;

    void gettaname(){}

    void settaname(String name) {}

    boolean titleIn(String name) {
        return title.containsKey(name);
    }

    /* get the index of a title name, if no exist return null*/
    int getTitleIndex(String name) {
        if (titleIn(name)) {
            return title.get(name);
        }
        return (Integer)null;
    }

    /* if title already in title map, nothing change
    *  if no, append title in the last and add corresponding index*/
    void titleAdd(String name){}

    /* add horizontal column to the body
    *  if the name exists in the title, replace the old one
    *  if the list's length is not equal to the body, leave the rest in blank*/
    void columnAdd(String title_name, ArrayList<String> col){}

    /* delete horizontal column according to the list
    *  if the column do not exist in titles, raise an error*/
    void colunDel(String title_name) {}

    /* add a row to the end of body
    *  if the row's length is not equal to the body, leave the rest in blank*/
    void rowAdd(ArrayList<String> row){}

    /* delete the last row??*/
    void rowDel() {}

    /* repr of the whole table*/
    void String() {}
}
