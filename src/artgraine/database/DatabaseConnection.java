package artgraine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String dsn = "jdbc:derby:artgraine.db;create=true";
    private static Connection dbConnection;

    public static Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection connection = DriverManager.getConnection(dsn);
        return connection;
    }

    public static Connection getInstance() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if(dbConnection == null){
            dbConnection = getConnection();
        }

        return dbConnection;
    }
}

