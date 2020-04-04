package chap5;

import chap5.config.ApplicationContextConfig;
import chap5.domain.Level;
import chap5.domain.User;
import chap5.domain.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContextConfig.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        this.user1 = Fixture.getUser("wizard", "홍종완", "test", Level.BASIC, 1, 0);
        this.user2 = Fixture.getUser("javajigi", "자바지기", "test2", Level.SILVER, 55, 10);
        this.user3 = Fixture.getUser("slipp", "포비", "test3", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        assertThat(userDao.getCount()).isEqualTo(3);

        checkSameUser(userDao.get(user1.getId()), user1);
        checkSameUser(userDao.get(user2.getId()), user2);
        checkSameUser(userDao.get(user3.getId()), user3);
    }

    private void checkSameUser(User user1,
                               User user2) {

        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    public void update() {
        this.userDao.deleteAll();

        this.userDao.add(this.user1);
        this.userDao.add(this.user2);

        this.user1.setName("종완");
        this.user1.setPassword("test2");
        this.user1.setLevel(Level.GOLD);
        this.user1.setLogin(1000);
        this.user1.setRecommend(999);
        this.userDao.update(this.user1);

        User updatedUser1 = this.userDao.get(this.user1.getId());
        this.checkSameUser(this.user1, updatedUser1);
        User savedUser = this.userDao.get(this.user2.getId());
        this.checkSameUser(this.user2, savedUser);
    }
}
