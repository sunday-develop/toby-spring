package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao;
    UserUpgradePolicy userUpgradePolicy;
    private MailSender mailSender;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserUpgradePolicy(UserUpgradePolicy userUpgradePolicy) {
        this.userUpgradePolicy = userUpgradePolicy;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void upgrades() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (userUpgradePolicy.canUpgrade(user)) {
                upgrade(user);
            }
        }
    }

    protected void upgrade(User user) {
        user.upgrade();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("dahye@dahye.com");
        mailMessage.setSubject("upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getGrade().name());

        mailSender.send(mailMessage);
    }

    public void add(User user) {
        if (user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
