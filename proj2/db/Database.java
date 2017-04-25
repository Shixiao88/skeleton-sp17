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
        return table_record.get(table_name);
    }

    public void removeTableByName(String table_name) {
        if (table_record.containsKey(table_name))
        table_record.remove(table_name);
    }

    public void addTable(String name, Table t) {
        table_record.put(name,t);
    }
}
