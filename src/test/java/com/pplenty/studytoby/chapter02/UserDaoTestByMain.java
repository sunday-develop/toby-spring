package com.pplenty.studytoby.chapter02;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by yusik on 2020/03/14.
 */
class UserDaoTestByMain {

    public static void main(String[] args) throws SQLException {

        // given
        ApplicationContext context = new GenericXmlApplicationContext("/di/userDaoFactory.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();

        // when
        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        // then
        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회");
    }
}