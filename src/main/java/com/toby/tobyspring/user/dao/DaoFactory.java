package com.toby.tobyspring.user.dao;

public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DUserConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }
}
