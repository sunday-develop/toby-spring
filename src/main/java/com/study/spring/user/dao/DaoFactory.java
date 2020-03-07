package com.study.spring.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public AccountDao accountDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new AccountDao(connectionMaker);
    }

    public MessageDao messageDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new MessageDao(connectionMaker);
    }

    private ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
