package com.pplenty.studytoby.chapter03;

import com.pplenty.studytoby.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/22.
 */
public class AddStatement implements StatementStrategy {

    User user;

    public AddStatement(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection con) throws SQLException {

        PreparedStatement ps = con.prepareStatement("insert into toby.users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        return ps;

    }
}
