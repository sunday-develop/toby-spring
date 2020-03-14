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
        ApplicationContext context = new GenericXmlApplicationContext("/di/applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();

        // when
        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        userDao.add(user);
        User user2 = userDao.get(user.getId());

        // then
        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }
    }
}