package com.example.demo.database;

import com.example.demo.classes.CodeSnippet;
import com.example.demo.classes.User;
import utils.PasswordHasher;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.database.MySQLConnection.*;

public class DatabaseHandler {

    public static User authenticateUser(String username, String passwordAttempt) {
        String query = "SELECT * FROM tbluser WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    if (PasswordHasher.verifyPassword(passwordAttempt, dbPassword)) {
                        return new User(rs.getInt("userid"), rs.getString("username"),
                                rs.getString("fname"), rs.getString("lname"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String fname, String lname) {
        if (doesUsernameExists(username)) return false;

        String query = "INSERT INTO tbluser (username, password, fname, lname) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String hashedPassword = PasswordHasher.hashPassword(password, PasswordHasher.getSalt());

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, fname);
            stmt.setString(4, lname);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean doesUsernameExists(String username) {
        String query = "SELECT 1 FROM tbluser WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveCode(String title, String code, String runtime, String language, int userid) {
        String query = "INSERT INTO tblcode (title, code, runtime, language, datecreated, userid) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, code);
            stmt.setString(3, runtime);
            stmt.setString(4, language);
            stmt.setDate(5, Date.valueOf(LocalDate.now()));
            stmt.setInt(6, userid);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<CodeSnippet> getAllCodeSnippets() {
        ArrayList<CodeSnippet> rows = new ArrayList<>();

        String query = "SELECT tblcode.codeid, tblcode.title, tblcode.code, tblcode.runtime, tblcode.language, tbluser.username, tblcode.datecreated, tblcode.userid " +
                "FROM tblcode " +
                "LEFT JOIN tbluser ON tblcode.userid = tbluser.userid";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    int cid = rs.getInt("codeid");
                    String title = rs.getString("title");
                    String code = rs.getString("code");
                    String runtime = rs.getString("runtime");
                    String language = rs.getString("language");
                    String createdBy = rs.getString("username");
                    String dateCreated = rs.getString("datecreated");
                    int uid = rs.getInt("userid");

                    CodeSnippet codeSnippet = new CodeSnippet(cid, title, code, runtime, language, createdBy, dateCreated, uid);
                    rows.add(codeSnippet);
                }
                return rows;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}