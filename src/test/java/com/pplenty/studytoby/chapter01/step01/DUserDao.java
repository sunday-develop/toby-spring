package com.pplenty.studytoby.chapter01.step01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/13.
 */
public class DUserDao extends UserDao {
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");
    }
}
