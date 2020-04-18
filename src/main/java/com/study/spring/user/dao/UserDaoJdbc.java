package com.study.spring.user.dao;

import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    private RowMapper<User> userRowMapper = (resultSet, i) -> {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setLevel(Level.valuesOf(resultSet.getInt("level")));
        user.setLogin(resultSet.getInt("login"));
        user.setRecommend(resultSet.getInt("recommend"));
        user.setEmail(resultSet.getString("email"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) {
        this.jdbcTemplate
                .update(sqlMap.get("add"), user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                        user.getEmail());
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject(sqlMap.get("get"), new Object[] { id }, this.userRowMapper);
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update(sqlMap.get("delete"));
    }

    @Override
    public Integer getCount() {
        return this.jdbcTemplate.queryForObject(sqlMap.get("getCount"), Integer.class);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(sqlMap.get("getAll"), this.userRowMapper);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(sqlMap.get("update"), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                user.getEmail(), user.getId());
    }
}