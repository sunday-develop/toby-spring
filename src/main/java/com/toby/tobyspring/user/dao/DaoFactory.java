package com.toby.tobyspring.user.dao;

public class DaoFactory {
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DUserConnectionMaker();
    }
}
