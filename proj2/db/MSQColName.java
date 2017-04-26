package db;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * Created by Administrator on 2017/4/21.
 */
public class MSQColName {

    private String Value;
    private String titleName;
    private String ColType;
    private String Type;
    private final Pattern _COLTITLE = Pattern.compile("([a-zA-z]:?\\w*)\\s+(\\w+)");
    private ArrayList<String> ColTypePattern = new ArrayList<>(Arrays.asList("string", "int", "float"));

    MSQColName (String col_name) {
        Matcher m;
        if ((m = _COLTITLE.matcher(col_name)).matches() &&
                (ColTypePattern.contains(m.group(2)))) {
                titleName = m.group(1);
                ColType = m.group(2);
                Value = titleName + " " + ColType;
                Type = "_MSQColName";
        } else {
            throw new RuntimeException("Bad formed column names");
        }
    }

    public String getTitleName() { return titleName; }

    public String getValue() {
        return Value;
    }

    public String getColType() {
        return ColType;
    }

    public String getType() {return Type; }

    public MSQColName copy() {
        return new MSQColName(Value);
    }

    public String toString() {
        return Value;
    }


    /* helper method to fill in the any list that is shorter with NOVALUE in the blank */
    private void balanceCols (ArrayList<MSQContainer> l1, String col1_type
            , ArrayList<MSQContainer> l2, String col2_type) {
        if (l1.size() < l2.size()) {
            for (int i = (l1.size()-1); i < l2.size(); i +=1 ){
                MSQContainer nothing= new MSQContainer("", col1_type);
                l1.add(nothing);
            }
        } else if(l1.size() > l2.size()) {
            for (int i = (l2.size()-1); i < l1.size(); i += 1) {
                MSQContainer nothing= new MSQContainer("", col2_type);
                l2.add(nothing);
            }
        }
        return;
    }

    public ArrayList<MSQContainer> add(Table t_self, MSQColName other, Table t_other, MSQColName new_col) {
        ArrayList<MSQContainer> column_self = t_self.columnGet(this.getValue());
        ArrayList<MSQContainer> column_other = t_other.columnGet(other.getValue());
        balanceCols(column_self, getColType(), column_other, other.getColType());

        ArrayList<MSQContainer> column_result = new ArrayList<>();
        for (int i=0; i<column_self.size(); i+=1) {
            MSQOperable res = column_self.get(i).add(column_other.get(i).getContainedElement());
            res.setType(new_col.getColType());
            MSQContainer result = new MSQContainer(res, new_col.getColType());
            column_result.add(result);
        }
        return column_result;
    }

    public ArrayList<MSQContainer> add(Table t_self, MSQOperable opr, MSQColName new_col) {
        ArrayList<MSQContainer> column_self = t_self.columnGet(this.getValue());
        ArrayList<MSQContainer> column_result = new ArrayList<>();

        for (MSQContainer ctn : column_self) {
            MSQOperable res = ctn.add(opr);
            MSQContainer result = new MSQContainer(res, new_col.getColType());
            column_result.add(result);
        }
        return column_result;
    }


    public  ArrayList<MSQContainer> minus(Table t_self, MSQColName other, Table t_other, MSQColName new_col) {
        ArrayList<MSQContainer> column_self = t_self.columnGet(this.getValue());
        ArrayList<MSQContainer> column_other = t_other.columnGet(other.getValue());
        balanceCols(column_self, getColType(), column_other, other.getColType());

        ArrayList<MSQContainer> column_result = new ArrayList<>();
        for (int i=0; i<column_self.size(); i+=1) {
            MSQOperable res = column_self.get(i).minus(column_other.get(i).getContainedElement());
            res.setType(new_col.getColType());
            MSQContainer result = new MSQContainer(res, new_col.getColType());
            column_result.add(result);
        }
        return column_result;
    }


    public MSQOperable mul(MSQOperable other) {
        throw new RuntimeException("Malformed multiply elements");
    }


    public MSQOperable divide(MSQOperable other) {
        throw new RuntimeException("Malformed multiply elements");
    }

    public boolean compare(MSQOperable other) {
        throw new RuntimeException("Malformed comparison elements");
    }

}
