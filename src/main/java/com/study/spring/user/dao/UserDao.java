package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataSource dataSource;
    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            return count;
        } catch (SQLException e) {
            throw e;
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {

                }

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
}
