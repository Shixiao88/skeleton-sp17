package db;

import java.util.HashMap;

public class Database {

    private HashMap <String, Table> table_record;

    public Database() {
        table_record = new HashMap<>();
    }

    public String transact(String query) {

        String[] args = {query};
        return Parse.main(args, this);

    }

    public Table selectTableByName(String table_name) {
        try
        {
            return table_record.get(table_name);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot find table name: " + table_name);
        }
    }

    public void removeTableByName(String table_name) {
        if (table_record.containsKey(table_name)) {
            table_record.remove(table_name);
        } else {
            throw new RuntimeException("Cannot find table name: "  + table_name);
        }
    }

    public void addTableByName(String name, Table t) {
        table_record.put(name,t);
    }

}
