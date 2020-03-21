package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter03.JdbcContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by yusik on 2020/03/09.
 */
public class UserDao {

    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    public UserDao() {
    }

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);

        this.dataSource = dataSource;//
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);

        this.dataSource = dataSource;//
    }

    public void deleteAll() {
//        jdbcTemplate.update(con -> con.prepareStatement("delete from toby.users"));
        jdbcTemplate.update("delete from toby.users");
    }

    public void add(User user) {
        jdbcTemplate.update(
                "insert into toby.users(id, name, password) values(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public int getCount() {
        Integer count = jdbcTemplate.query(

                con -> con.prepareStatement("select count(*) from toby.users"),

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

                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                });
    }
}


