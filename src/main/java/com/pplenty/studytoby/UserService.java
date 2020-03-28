package com.pplenty.studytoby;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserService {

    private final PlatformTransactionManager transactionManager;
    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;

    public UserService(PlatformTransactionManager transactionManager, UserDao userDao, UserLevelUpgradePolicy policy) {
        this.transactionManager = transactionManager;
        this.userDao = userDao;
        this.policy = policy;
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

        Properties props = new Properties();
        props.put("mail.smtp.host", "alt1.gmail-smtp-in.l.google.com");
        Session session = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("jason.parsing@gmail.com"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText(user.getName() + "님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
