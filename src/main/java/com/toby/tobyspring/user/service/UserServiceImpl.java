package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserUpgradePolicy userUpgradePolicy;
    @Autowired
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

    @Override
    public User get(String id) {
        return this.userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return this.userDao.getAll();
    }

    @Override
    public void deleteAll() {
        this.userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        this.userDao.update(user);
    }
}
