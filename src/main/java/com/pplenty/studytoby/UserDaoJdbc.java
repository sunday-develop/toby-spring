package com.pplenty.studytoby;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by yusik on 2020/03/09.
 */
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private Map<String, String> sqlMap;

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
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

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(this.sqlMap.get("deleteAll"));
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(
                this.sqlMap.get("add"),
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend()
        );
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                this.sqlMap.get("update"),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
        );

    }

    @Override
    public int getCount() {
        Integer count = jdbcTemplate.query(
                this.sqlMap.get("getCount"),
                (rs) -> {
                    rs.next();
                    return rs.getInt(1);
                });
        return count;
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(
                this.sqlMap.get("get"),
                new Object[]{id},
                userRowMapper);
    }

    @Override
    public List<User> getAll() {

        return jdbcTemplate.query(
                this.sqlMap.get("getAll"),
                userRowMapper);
    }
}


