package com.pplenty.studytoby.chapter01.step01;

/**
 * Created by yusik on 2020/03/13.
 */
public class DaoFactory {

    /**
     * 팩토리의 메소드는 UserDao 타입의 오브젝트를
     * 어떻게 만들고 어떻게 준비시킬지를 결정한다.
     */
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    private DConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
