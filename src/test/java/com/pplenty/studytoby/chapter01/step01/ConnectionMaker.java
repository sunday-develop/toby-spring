package com.pplenty.studytoby.chapter01.step01;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/13.
 */
public interface ConnectionMaker {

    public Connection makeConnection() throws SQLException;
    
}
