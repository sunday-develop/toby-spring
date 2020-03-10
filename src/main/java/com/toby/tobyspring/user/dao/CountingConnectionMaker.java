package com.toby.tobyspring.user.dao;

import javax.sql.DataSource;

public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource makeNewConnection() {
        this.counter++;
        return dataSource;
    }

    public int getCounter() {
        return this.counter;
    }
}
