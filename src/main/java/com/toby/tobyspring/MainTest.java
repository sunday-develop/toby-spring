package com.toby.tobyspring;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.User;

import java.sql.SQLException;

public class MainTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();

        User user = new User();
        user.setId("dahyekim");
        user.setName("김다혜");
        user.setPassword("dahye");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get("dahyekim");
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
