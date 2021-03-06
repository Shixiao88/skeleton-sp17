package db;

import edu.princeton.cs.introcs.In;

import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;
import java.io.File;


/**
 * Created by Xiao Shi on 2017/4/17.
 */
public class Table {

    private String name;
    private TableBuilder tb;
    private Map<MSQColName, Integer> title;
    private ArrayList<ArrayList<MSQContainer>> body;
    private String path = "test/";
    private String prefix = ".tbl";

    Table(String table_name) {
        try {
            String source = path + table_name + prefix;
            this.name = table_name;
            ArrayList<String> rs = ReadSource.readSource(new In(source));
            ParseSource ps = new ParseSource(rs);
            tb = new TableBuilder("test table", ps);
            this.title = tb.gettaTitle();
            this.body = tb.gettaBody();
        } catch (IOError e) {
            System.out.println("ERROR: TBL file not found: " + name + prefix);
        }
    }

    Table(String name, String src) {
        this.name = name;
        String source = path + src + prefix;
        ArrayList<String> rs = ReadSource.readSource(new In(source));
        ParseSource ps = new ParseSource(rs);
        tb = new TableBuilder("test table", ps);
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

    String getFullTitleNameByRealName (String title_real_name) {
        for (MSQColName c : title.keySet()) {
            if (title_real_name.equals(c.getTitleName())) {
                return c.getValue();
            }
        } return null;
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
        return titleGet(name) >= 0;
    }

    /* get the index of a title name, if no exist return null*/
    int getTitleIndex(String name) {
        if (titleIn(name)) {
            return titleGet(name);
        } else {
            throw new RuntimeException("the given title is not in the table. " + name);
        }
    }

    /* get the MSQColname instance given a string name (the name is without the coltype expr */
    public MSQColName getColNameByName (String title_name_no_type) {
        for (MSQColName k : title.keySet()) {
            if (title_name_no_type == k.getTitleName()) {
                return k;
            }
        } throw new RuntimeException("Must select a title from the table, not a title you made up.");
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
            throw new RuntimeException("Must select a title from the table, not a title you made up.");
        }
    }

    /* the parameter is the first word of the title, I.E. title "X int", it will only pass "X"*/
    public ArrayList<MSQContainer> columnGetbyRealName (String title_name) {
        try {
            String title_full_name = getFullTitleNameByRealName(title_name);
            return columnGet(title_full_name);
        } catch (NullPointerException e) {
            throw new RuntimeException("Must select a title from the table, not a title you made up.");
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
            System.out.print("Row number is larger than the larges row number of the table");
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

    /* helper method for columneAdd.
    *  to check if the added in element's lengh and the table corresponding length:
    *  - if the added in element is too short, compelte the rest as MSQNovalue instance;
    *  - if the added in element is too long, raise an error
    *  - else do nothing */
    private void checkColSize(ArrayList<MSQContainer> to_check, int std, String col_type) {
        if (to_check.size() < std) {
            int to_check_size = to_check.size();
            int discranpcy = std - to_check_size;

            for (int i = 0; i < discranpcy; i += 1) {
                MSQContainer nothing = new MSQContainer("", col_type);
                to_check.add(nothing);
            }
        }
        for (MSQContainer k : to_check) {
            if (k.getContainedElement() instanceof MSQNan ||
                    k.getContainedElement() instanceof MSQNovalue) {
                k.getContainedElement().setType(col_type);
            } else if (! k.getRealType().equals(col_type)) {
                throw new RuntimeException("added in column must be the right type");
            }
        }
    }

        /* helper method for rowAdd.
    *  to check if the added in element's lengh and the table corresponding length:
    *  - if the added in element is too short, compelte the rest as MSQNovalue instance;
    *  - if the added in element is too long, raise an error
    *  - else do nothing */
    private void checkRowSize(ArrayList<MSQContainer> to_check, int std, Set<MSQColName> t_k) {
        if (to_check.size() <= std) {
            int to_check_size = to_check.size();
            int discranpcy = std - to_check_size;
            ArrayList<MSQColName> title_key_lst = new ArrayList<>(t_k);
            // check if the added in row is the tight type
            for (int index = 0; index < to_check_size; index += 1) {
                String t = to_check.get(index).getRealType();

                String t1 = title_key_lst.get(index).getColType();
                MSQContainer ctn = to_check.get(index);
                // to deal with the insert in row is NOVALUE or NaN
                if (ctn.getContainedElement() instanceof MSQNovalue ||
                        ctn.getContainedElement() instanceof MSQNan) {
                    String col_type = title_key_lst.get(index).getColType();
                    ctn.getContainedElement().setType(col_type);
                } else if (! to_check.get(index).getRealType().equals(title_key_lst.get(index).getColType())) {
                    throw new RuntimeException("added in row must be the right type");
                }
            }
            for (int i = 0; i < discranpcy; i += 1) {
                String col_type = title_key_lst.get(to_check_size + i).getColType();
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
        String col_type = new MSQColName(title_name).getColType();
        checkColSize(col, body.size(), col_type);
        try {
            int index = getTitleIndex(title_name);
            for (int i = 0; i < col.size(); i += 1) {
                body.get(i).set(index,col.get(i));
            }
        }
        catch (RuntimeException e) {
            titleAdd(title_name);
            for (int i = 0; i < col.size(); i += 1) {
                try {
                    getbody();
                    body.get(i).add(col.get(i));
                } catch (IndexOutOfBoundsException e1) {
                    ArrayList<MSQContainer> line = new ArrayList<>();
                    line.add(col.get(i));
                    body.add(line);
                }
            }
        }
    }


    /* helper method for title.get(string title)*/
    private int titleGet(String title_name) {
        for (MSQColName k : this.title.keySet()) {
            if (k.getValue().equals(title_name)) {
                return this.title.get(k);
            }
        } return -1;
    }

    /* helper method for COLUMNDEL, if you delete one title, all the title after that should be shifted
     * it means that all titles has greater values should have value minus one*/
    private void titleDel(String title_name) {
        int index = getTitleIndex(title_name);
        ArrayList<MSQColName> copy = new ArrayList<>(title.keySet());
        for ( MSQColName k : copy) {
            if (title.get(k) == index) {
                title.remove(k);
            } else if (title.get(k) > index) {
                title.put(k, title.get(k) - 1);
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
        checkRowSize(row, getColumnNum(), this.title.keySet());
        body.add(row);
    }

    /* add a row just in front of the given index
     * if the row's length is not equal to the body, leave the rest in blank */
    void rowAdd(ArrayList<MSQContainer> row, int index) {
        checkRowSize(row, getColumnNum(),this.title.keySet());
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
            ArrayList<MSQContainer> r = rowGet(row);
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

    public void saveTableToFile(String file_name) {
        try {
            File file = new File(path + file_name + prefix);
            FileWriter file_writer = new FileWriter(file);
            file_writer.write(toString());
            file_writer.flush();
            file_writer.close();
        } catch (IOException e) {
            throw new RuntimeException("error with create file " + file_name);
        }
    }

    /* repr of the whole table*/
    public String toString() {
        StringJoiner title_joiner = new StringJoiner(",");
        String output = "";
        try {
            for (MSQColName n : title.keySet()) {
                title_joiner.add(n.toString());
            }
            output += title_joiner.toString();

            for (ArrayList<MSQContainer> l : body) {
                StringJoiner ctn_joiner = new StringJoiner(",");
                for (MSQContainer ctn : l) {
                    ctn_joiner.add(ctn.toString());
                }
                output += "\n";
                output += ctn_joiner.toString();
            }
        } catch (NullPointerException e) { }
        return output;

    }
}
