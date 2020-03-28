package com.pplenty.studytoby;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by yusik on 2020/03/09.
 */
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("loing"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    };

    public UserDaoJdbc() {
    }

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from toby.users");
    }

    public void add(User user) {
        jdbcTemplate.update(
                "insert into toby.users(id, name, password, level, login, recommend) " +
                        "values(?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend()
        );
    }

    public int getCount() {
        Integer count = jdbcTemplate.query(
                "select count(*) from toby.users",
                (rs) -> {
                    rs.next();
                    return rs.getInt(1);
                });
        return count;
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(
                "select * from toby.users where id = ?",
                new Object[]{id},
                userRowMapper);
    }

    public List<User> getAll() {

        return jdbcTemplate.query(
                "select * from toby.users order by id",
                userRowMapper);
    }
}


