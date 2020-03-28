package com.pplenty.studytoby;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserService {

    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;
    private final PlatformTransactionManager transactionManager;
    private final MailSender mailSender;

    public UserService(UserDao userDao,
                       UserLevelUpgradePolicy policy,
                       PlatformTransactionManager transactionManager,
                       MailSender mailSender) {
        this.userDao = userDao;
        this.policy = policy;
        this.transactionManager = transactionManager;
        this.mailSender = mailSender;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        try {

            List<User> users = userDao.getAll();
            for (User user : users) {
                upgradeLevel(user);
            }

            // 커밋
            transactionManager.commit(status);
        } catch (Exception e) {
            // 롤백
            transactionManager.rollback(status);
            throw e;
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
