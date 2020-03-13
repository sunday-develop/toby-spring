package com.pplenty.studytoby.chapter01.step01;

/**
 * Created by yusik on 2020/03/13.
 */
public class AccountDao {

    private ConnectionMaker connectionMaker;

    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
