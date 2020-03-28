package com.study.spring.user.dao;

import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (resultSet, i) -> {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setLevel(Level.valuesOf(resultSet.getInt("level")));
        user.setLogin(resultSet.getInt("login"));
        user.setRecommend(resultSet.getInt("recommend"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)", user.getId(), user.getName(),
                user.getPassword(), user.getLevel().intValue() , user.getLogin(), user.getRecommend());
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
}