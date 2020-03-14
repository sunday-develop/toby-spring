package com.toby.tobyspring.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();

            preparedStatement = statementStrategy.makePreparedStatement(connection);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignored) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public void executeSql(final String query) throws SQLException {
        this.workWithStatementStrategy(connection -> connection.prepareStatement(query));
    }

    public void executeSql(final String query, final String... params) throws SQLException {
        this.workWithStatementStrategy(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int i = 1;
            for (String param : params) {
                preparedStatement.setString(i++, param);
            }

            return preparedStatement;
        });
    }
}
