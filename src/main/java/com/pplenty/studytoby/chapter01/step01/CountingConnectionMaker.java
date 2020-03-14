package com.pplenty.studytoby.chapter01.step01;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/14.
 */
public class CountingConnectionMaker implements ConnectionMaker {

    private int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection makeConnection() throws SQLException {
        counter++;
        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return counter;
    }
}
