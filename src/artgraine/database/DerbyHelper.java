package artgraine.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyHelper {

    private Connection dbConnetion;

    public DerbyHelper(Connection dbConnetion) {
        this.dbConnetion = dbConnetion;
    }

    public Boolean tableExist(String tableName){

        try {
            ResultSet rs = dbConnetion.getMetaData().getTables(null, "APP", tableName, null);
            return ! rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTable(String tableName, String sql) throws SQLException {
        if(tableExist(tableName)){
            deleteTable(tableName);
        }

        executeSQL(sql);
    }

    public void deleteTable(String tableName) throws SQLException {
        String sql = "DROP TABLE " + tableName;
        executeSQL(sql);
    }

    public void executeSQL(String sql) throws SQLException {
        Statement statement = dbConnetion.createStatement();
        statement.executeUpdate(sql);
    }
}
