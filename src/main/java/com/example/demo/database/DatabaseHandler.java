package com.example.demo.database;

import com.example.demo.classes.User;
import utils.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

import static com.example.demo.database.MySQLConnection.*;

public class DatabaseHandler {
    public static User authenticateUser(String username, String passwordAttempt) {
        String query = "SELECT * FROM tbluser WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if(PasswordHasher.verifyPassword(passwordAttempt, dbPassword)) {
                    return new User(rs.getInt("userid"), rs.getString("username"), rs.getString("fname"), rs.getString("lname"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String fname, String lname) {
        try {
            if(doesUsernameExists(username)) return false;
            String query = "INSERT INTO tbluser (username, password, fname, lname) VALUES (?, ?, ?, ?)";

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);

            String hashedPassword = PasswordHasher.hashPassword(password, PasswordHasher.getSalt());

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, fname);
            stmt.setString(4, lname);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean doesUsernameExists(String username) throws SQLException {
        String query = "SELECT username FROM tbluser WHERE username = ?";
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) return true;
        return false;
    }
}