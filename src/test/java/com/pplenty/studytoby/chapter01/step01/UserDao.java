package com.pplenty.studytoby.chapter01.step01;

import java.sql.*;

/**
 * Created by yusik on 2020/03/09.
 */
public class UserDao {

    public void add(User user) throws SQLException {

        Connection con = getConnection();

        PreparedStatement ps = con.prepareStatement("insert into toby.users(id, name, password) values(?, ?, ?)");

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public User get(String id) throws SQLException {

        Connection con = getConnection();

        PreparedStatement ps = con.prepareStatement("select * from toby.users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        con.close();

        return user;

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");
    }

}


