package com.pplenty.studytoby.chapter03;

import com.pplenty.studytoby.UserDaoJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/22.
 */
@Deprecated
public class UserDaoDeleteAll extends UserDaoJdbc {

    protected PreparedStatement makeStatement(Connection con) throws SQLException {
        return con.prepareStatement("delete from toby.users");
    }
}
