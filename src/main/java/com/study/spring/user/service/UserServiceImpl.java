package com.study.spring.user.service;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    @Autowired
    private MailSender mailSender;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void add(User user) {

        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void upgradeLevels() {
        List<User> userList = userDao.getAll();
        for (User user : userList) {
            if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                userLevelUpgradePolicy.upgradeLevel(user);
                userDao.update(user);
                sendUpgradeEmail(user);
            }
        }
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("vvshinevv@naver.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

        this.mailSender.send(mailMessage);
    }

}
