package com.pplenty.studytoby.chapter03;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/22.
 */
public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(final String sql) throws SQLException {
        workWithStatementStrategy(con -> con.prepareStatement(sql));
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        try {

            con = this.dataSource.getConnection();

            ps = stmt.makePreparedStatement(con);

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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignored) {
                }
            }
        }

    }
}
