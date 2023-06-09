package com.software_engineering_course_work;
import java.sql.*;

/**
 * Class that provides connection to the database.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class DBConnect {

    /** The instance of the class. */
    private static DBConnect instance;
    /** A database url of the form jdbc:subprotocol:subname. */
    private final String url;
    /** The database user on whose behalf the connection is being made. */
    private final String user;
    /** The user's password. */
    private final String pass;
    /** The object that execute SQL requests. */
    private final Executor executor = new Executor();
    /** The object of {@link Connection} */
    private Connection connection;
    /** The object of {@link Statement} */
    private Statement statement;

    /**
     * The lone constructor.
     * @param url url
     * @param user user
     * @param pass password
     */
    private DBConnect(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Returns the sole instance of the class bases on Singleton pattern.
     * @param url url
     * @param user user
     * @param pass password
     * @return instance
     */
    public static DBConnect getInstance(String url, String user, String pass) {
        if (instance == null) {
            instance = new DBConnect(url, user, pass);
        }
        return instance;
    }

    /**
     * Connects the object to the database.
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        statement = connection.createStatement();
    }

    /**
     * Close the connection to the database.
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Returns the executor of SQL requests.
     * @return the object of {@link Executor}
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * @return the object of {@link Connection}
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return the object of {@link Statement}
     */
    public Statement getStatement() {
        return statement;
    }
}
