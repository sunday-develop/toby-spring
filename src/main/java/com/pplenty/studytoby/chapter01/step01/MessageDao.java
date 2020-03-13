package com.pplenty.studytoby.chapter01.step01;

/**
 * Created by yusik on 2020/03/13.
 */
public class MessageDao {

    private ConnectionMaker connectionMaker;

    public MessageDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
