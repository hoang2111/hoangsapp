package com.github.hoang2111.hoangsapp;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
        }
    }

    private Connection connect() {
        String url = "jdbc:sqlite:session_data.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sessions (\n"
                + " id integer PRIMARY KEY,\n"
                + " sessionType text NOT NULL,\n"
                + " duration integer,\n"
                + " sessionDate text\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertSession(SessionData session) {
        String sql = "INSERT INTO sessions(sessionType, duration, sessionDate) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, session.getSessionType());
            pstmt.setInt(2, session.getDuration());
            pstmt.setString(3, session.getSessionDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<SessionData> getAllSessions() {
        String sql = "SELECT sessionType, duration, sessionDate FROM sessions";

        List<SessionData> sessions = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sessions.add(new SessionData(
                        rs.getString("sessionType"),
                        rs.getInt("duration"),
                        LocalDateTime.parse(rs.getString("sessionDate"))
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sessions;
    }
}