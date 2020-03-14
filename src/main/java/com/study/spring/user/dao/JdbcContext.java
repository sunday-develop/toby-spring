package com.study.spring.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(c -> c.prepareStatement(query));
    }

    public void executeSql(final String query, final String... params) throws SQLException {
        workWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement(query);
            int i = 1;
            for (String param : params) {
                ps.setString(i++, param);
            }

            return ps;
        });
    }

    void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {

                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignored) {

                }
            }
        }
    }
}
