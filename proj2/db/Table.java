package db;

import edu.princeton.cs.introcs.In;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class Table {

    private String name;
    private TableBuilder tb;
    private Map<MSQColName, Integer> title;
    private ArrayList<ArrayList<MSQContainer>> body;

    Table(String name, String source) {
        this.name = name;
        ArrayList<String> rs = ReadSource.readSource(new In(source));
        ParseSource ps = new ParseSource(rs);
        TableBuilder tb = new TableBuilder("test table", ps);
        this.title = tb.gettaTitle();
        this.body = tb.gettaBody();
    }

    Table(String name, Map<MSQColName, Integer> t, ArrayList<ArrayList<MSQContainer>> b) {
        title = t;
        body = b;
        this.name = name;
    }

    String getname(){
        return this.name;
    }

    Map<MSQColName, Integer> gettitle() { return this.title; }

    ArrayList<ArrayList<MSQContainer>> getbody() {
        return this.body;
    }

    int getColumnNum() {
        return title.keySet().size();
    }

    int getRowNum() {
        return body.size();
    }

    /* because the title is the MSQColname instance, can only compare its value (the real col name string to the given
     * parameter
     */
    boolean titleIn(String name) {
        ArrayList<MSQColName> t_key = new ArrayList<>(title.keySet());
        for (MSQColName key : t_key) {
            if (key.getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /* get the index of a title name, if no exist return null*/
    int getTitleIndex(String name) {
        ArrayList<MSQColName> t_key = new ArrayList<>(title.keySet());
        for (MSQColName key : t_key) {
            if (key.getValue().equals(name)) {
                return title.get(key);
            }
        }
        throw new RuntimeException("the given title is not in the table.");
    }

    /* return a column selected by its column title,
*  raise an error if the given title name is not in the table*/
    public ArrayList<MSQContainer> columnGet (String title_name) {
        try {
            int index = getTitleIndex(title_name);
            ArrayList<MSQContainer> col = new ArrayList<>();
            int rowNum = getRowNum();
            for (int i = 0; i < rowNum; i += 1) {
                col.add(body.get(i).get(index));
            }
            return col;
        } catch (RuntimeException e) {
            System.out.print("Must select a title from the table, not a title you made up.");
            return null;
        }
    }

    /* must do the copy to every instance that the table contains, not only copy the address */
    public ArrayList<MSQContainer> rowGet (int row_index) {
        try {
            ArrayList<MSQContainer> rtn_row = new ArrayList<>();
            for (MSQContainer ctn : body.get(row_index)) {
                rtn_row.add(ctn.copy());
            }
            return rtn_row;
        } catch (RuntimeException e) {
            System.out.print("Row number is larger thant he larges row number of the table");
            return null;
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
            MSQColName res_name = new MSQColName(name);
            title.put(res_name, size);
        }
    }

    /* helper method for columneAdd and rowAdd.
    *  to check if the added in element's lengh and the table corresponding length:
    *  - if the added in element is too short, compelte the rest as MSQNovalue instance;
    *  - if the added in element is too long, raise an error
    *  - else do nothing */
    private void checkSize(ArrayList<MSQContainer> to_check, int std) {
        if (to_check.size() < std) {
            int to_check_size = to_check.size();
            int discranpcy = std - to_check_size;
            ArrayList<MSQColName> title_keys = new ArrayList<>(title.keySet());
            for (int i = 0; i < discranpcy; i += 1) {
                String col_type = title_keys.get(i).getColType();
                MSQContainer nothing = new MSQContainer("", col_type);
                to_check.add(nothing);
            }
        } else if (to_check.size() > std) {
            throw new RuntimeException("the added element is too long!");
        }
    }

    /* add horizontal column to the body
    *  if the name exists in the title, replace the old one
    *  if does not exists in the title, add to the end
    *  if the list's length is not equal to the body, leave the rest in blank */
    void columnAdd(String title_name, ArrayList<MSQContainer> col) {
        checkSize(col, body.size());
        titleAdd(title_name);
        try {
            int index = getTitleIndex(title_name);
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).set(index,col.get(i));
            }
        }
        catch (RuntimeException e) {
            for (int i = 0; i < getRowNum(); i += 1) {
                body.get(i).add(col.get(i));
            }
        }
    }

    /* helper method for COLUMNDEL, if you delete one title, all the title after that should be shifted
     * it means that all titles has greater values should have value minus one*/
    private void titleDel(String title_name) {
        int index = getTitleIndex(title_name);
        for (MSQColName t : title.keySet()) {
            if (title.get(t) == index) {
                title.remove(t);
            } else if (title.get(t) > index) {
                title.put(t, title.get(t) - 1);
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

    /* add a row to the end of body
    *  if the row's length is not equal to the body, leave the rest in blank */
    void rowAdd(ArrayList<MSQContainer> row){
        checkSize(row, getColumnNum());
        body.add(row);
    }

    /* add a row just in front of the given index
     * if the row's length is not equal to the body, leave the rest in blank */
    void rowAdd(ArrayList<MSQContainer> row, int index) {
        checkSize(row, getColumnNum());
        body.add(index, row);

    }

    /* delete the row with selected row number*/
    void rowDel(int row_index) {
        try {
            body.remove(row_index);
        } catch (IndexOutOfBoundsException e) {}

    }

    public boolean isEmpty() {
        return body.size() == 0;
    }

    public Table copy(String new_name) {
        // create a copy of body
        ArrayList<ArrayList<MSQContainer>> copy_body  = new ArrayList<>();
        for (int row=0; row < getRowNum(); row += 1){
            ArrayList<MSQContainer> line = new ArrayList<>();
            line.addAll(rowGet(row));
            copy_body.add(line);
        }

        // create a copy of title
        Map<MSQColName, Integer> copy_title = new LinkedHashMap<>();
        for (MSQColName k : title.keySet()) {
            copy_title.put(k.copy(), title.get(k));
        }

        Table copy_table = new Table(new_name, copy_title, copy_body);
        return copy_table;
    }

    /* repr of the whole table*/
    public String toString() {
        return "";
    }
}
