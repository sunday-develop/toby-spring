package com.pplenty.studytoby.chapter03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/22.
 */
public interface StatementStrategy {

    PreparedStatement makePreparedStatement(Connection con) throws SQLException;
}
