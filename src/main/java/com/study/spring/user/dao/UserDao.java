package com.study.spring.user.dao;

import com.mysql.jdbc.MysqlErrorNumbers;
import com.study.spring.user.domain.User;
import com.study.spring.user.exception.DuplicationUserIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (resultSet, i) -> {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new Object[] { id }, this.userRowMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM users");
    }

    public Integer getCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM users ORDER by id", this.userRowMapper);
    }

    public void add() throws DuplicationUserIdException {

        try {
            DataSource dataSource = new SingleConnectionDataSource();
            Connection c = dataSource.getConnection();
        } catch (SQLException e) {

            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw new DuplicationUserIdException(e); // 예외 전환
            } else {
                throw new RuntimeException(e); // 예외 포장
            }
        }
    }
}