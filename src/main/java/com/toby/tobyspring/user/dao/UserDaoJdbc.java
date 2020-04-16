package com.toby.tobyspring.user.dao;

import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import com.toby.tobyspring.user.exception.DuplicateUserIdException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private String sqlAdd;
    private String sqlGet;
    private String sqlGetAll;
    private String sqlGetCount;
    private String sqlDeleteAll;
    private String sqlUpdate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    public void setSqlGet(String sqlGet) {
        this.sqlGet = sqlGet;
    }

    public void setSqlGetAll(String sqlGetAll) {
        this.sqlGetAll = sqlGetAll;
    }

    public void setSqlGetCount(String sqlGetCount) {
        this.sqlGetCount = sqlGetCount;
    }

    public void setSqlDeleteAll(String sqlDeleteAll) {
        this.sqlDeleteAll = sqlDeleteAll;
    }

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    public void add(final User user) {
        try {
            this.jdbcTemplate.update(
                    this.sqlAdd,
                    user.getId(), user.getName(), user.getPassword(), user.getGrade().intValue(),
                    user.getLogin(), user.getRecommend(), user.getEmail());
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserIdException(e);
        }
    }

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setGrade(Grade.valueOf(rs.getInt("grade")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(this.sqlGet,
                new Object[]{id}, this.userRowMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlGetAll, this.userRowMapper);
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject(this.sqlGetCount, Integer.class);
    }

    public void deleteAll() {
        this.jdbcTemplate.update(this.sqlDeleteAll);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                this.sqlUpdate,
                user.getName(), user.getPassword(), user.getGrade().intValue(), user.getLogin(),
                user.getRecommend(), user.getEmail(), user.getId());
    }
}
