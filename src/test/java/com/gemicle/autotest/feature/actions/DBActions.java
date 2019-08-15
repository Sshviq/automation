package com.gemicle.autotest.feature.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBActions {
    private static final String DATABASE_FILE = "jdbc:h2:./test";
    private static final String USER_NAME = "user";
    private Connection con;
    private Statement stmt;

    public void writeTimeResponse(String host, int responseDuration) throws SQLException {
        try {
            con = DriverManager.getConnection(DATABASE_FILE, USER_NAME, "");
            stmt = con.createStatement();
            stmt.executeUpdate("create table if not exists response ( host varchar(255) not null," +
                                       " request_time timestamp not null, response_duration int)");
            String sql = String.format("insert into response (host, request_time, response_duration) values ('%s', CURRENT_TIMESTAMP, %d)", host,
                                       responseDuration);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            if (con != null) {
                con.close();
            }
        }
    }

    public int getAverageAndUpdateData(String host, int responseDuration) throws SQLException {
        try {
            con = DriverManager.getConnection(DATABASE_FILE, USER_NAME, "");
            stmt = con.createStatement();
            stmt.executeUpdate("create table if not exists general_stat( host varchar(255) unique not null, total_time int, requests_number long)");
            String sql = String.format("select total_time, requests_number from general_stat where host = '%s'", host);
            ResultSet resultSet = stmt.executeQuery(sql);
            if (!resultSet.next()) {
                String sqlInsert = String.format("insert into general_stat (host, total_time, requests_number) values ('%s', %d, 1)", host, responseDuration);
                con.createStatement().executeUpdate(sqlInsert);
                return responseDuration;
            } else {
                String sqlUpdate = String.format("update general_stat set total_time = total_time + %d, requests_number = requests_number + 1 where host = " +
                                                         "'%s'", responseDuration, host);
                con.createStatement().executeUpdate(sqlUpdate);
                long durationSum = resultSet.getInt("total_time");
                long requestNumber = resultSet.getLong("requests_number");
                return (int) (durationSum / requestNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            if (con != null) {
                con.close();
            }
        }
        throw new SQLException("Pre-calculated average time has not been read");
    }
}
