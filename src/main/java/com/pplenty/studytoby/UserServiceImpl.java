package com.pplenty.studytoby;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;
    private final MailSender mailSender;

    public UserServiceImpl(UserDao userDao,
                           UserLevelUpgradePolicy policy,
                           MailSender mailSender) {
        this.userDao = userDao;
        this.policy = policy;
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

        // 비지니스 로직
        List<User> users = userDao.getAll();
        for (User user : users) {
            upgradeLevel(user);
        }
    }

    public void upgradeLevel(User user) {
        if (policy.canUpgradeLevel(user)) {
            policy.upgradeLevel(user);
            userDao.update(user);
            sendUpgradeEmail(user);
        }
    }

    private void sendUpgradeEmail(User user) {

        if (user.getEmail() == null) {
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jason.parsing@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText(user.getName() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

        mailSender.send(mailMessage);
    }
}
