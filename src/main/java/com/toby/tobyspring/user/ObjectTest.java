package com.toby.tobyspring.user;

import com.toby.tobyspring.user.dao.DaoFactory;
import com.toby.tobyspring.user.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ObjectTest {
    public static void main(String[] args) {
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        UserDao dao2 = daoFactory.userDao();

        System.out.println(dao1);
        System.out.println(dao2);

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println(dao3);
        System.out.println(dao4);

    }
}
