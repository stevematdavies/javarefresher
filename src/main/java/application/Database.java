package application;

import handlers.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {

    private String getConnectionString(Properties p){
        return String.format("jdbc:mysql://%s:%s/%s",
                p.getProperty("host"),
                p.getProperty("port"),
                p.getProperty("database"));
    }
    private static Database database = new Database();
    private Database(){}

    private Connection connection;

    public static Database getInstance(){
        return database;
    }


    public Connection getConnection(){
        return connection;
    }

    public void connect(Properties p) throws SQLException {
        connection = DriverManager.getConnection(getConnectionString(p), p.getProperty("user"),p.getProperty("password"));
    }

    public void close() throws SQLException {
        connection.close();
    }
}
