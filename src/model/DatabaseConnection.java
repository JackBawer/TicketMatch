package model;

import java.sql.*;

public class DatabaseConnection{
    private static final String URL = "jdbc:postgresql://localhost:5432/ticketmatch";
    private static final String USER = "jack";
    private static final String PASSWORD = "info";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}