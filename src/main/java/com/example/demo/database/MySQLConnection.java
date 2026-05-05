package com.example.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "dbtnspector";
    public static final String URL = SERVER_URL + DB_NAME;

    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

            stmt.executeUpdate("USE " + DB_NAME);

            String createUserTable = "CREATE TABLE IF NOT EXISTS tbluser (" +
                    "userid INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "fname VARCHAR(50), " +
                    "lname VARCHAR(50))";

            String createCodeTable = "CREATE TABLE IF NOT EXISTS tblcode (" +
                    "codeid INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(100), " +
                    "code TEXT, " +
                    "runtime VARCHAR(50), " +
                    "language VARCHAR(50), " +
                    "datecreated DATE, " +
                    "userid INT, " +
                    "FOREIGN KEY (userid) REFERENCES tbluser(userid))";

            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createCodeTable);

            System.out.println("Database and Tables validated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}