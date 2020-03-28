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

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        try {
            this.jdbcTemplate.update("insert into users(id, name, password, grade, login, recommend) " +
                            "values(?, ?, ?, ?, ?, ?)",
                    user.getId(), user.getName(), user.getPassword(), user.getGrade().intValue(), user.getLogin(), user.getRecomend());
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
        user.setRecomend(rs.getInt("recommend"));
        return user;
    };

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userRowMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userRowMapper);
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update("update users set name = ?, password = ?, grade = ?, login = ?, recommend = ? where id = ?",
                user.getName(), user.getPassword(), user.getGrade().intValue(), user.getLogin(), user.getRecomend(), user.getId());
    }
}
