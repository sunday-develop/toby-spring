package com.pplenty.studytoby.chapter03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/22.
 */
@Deprecated
public class DeleteAllStatement implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement("delete from toby.users");
    }
}
