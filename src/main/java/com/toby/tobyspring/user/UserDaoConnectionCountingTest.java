package com.toby.tobyspring.user;

import com.toby.tobyspring.user.dao.CountingConnectionMaker;
import com.toby.tobyspring.user.dao.CountingDaoFactory;
import com.toby.tobyspring.user.dao.UserDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoConnectionCountingTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.get("dahyekim");
        userDao.get("dahyekim");
        userDao.get("dahyekim");

        CountingConnectionMaker countingConnectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("connection counter : " + countingConnectionMaker.getCounter());
    }
}
