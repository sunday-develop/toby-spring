package com.pplenty.studytoby.chapter01.step01;

/**
 * Created by yusik on 2020/03/14.
 */
public class SingleUserDao {

    private static SingleUserDao INSTANCE;
    private final ConnectionMaker connectionMaker;

    private SingleUserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public static synchronized SingleUserDao getInstance() {
        if (INSTANCE == null) {
            return new SingleUserDao(() -> null);
        }
        return INSTANCE;
    }
}
