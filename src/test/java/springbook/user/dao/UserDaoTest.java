package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDaoTest {

    private UserDao userDao;
    private DataSource dataSource;

    private final User user1 = User.of("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
    private final User user2 = User.of("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
    private final User user3 = User.of("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);

    @BeforeEach
    void setup() {
        dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/springbook", "spring", "book", true
        );

        userDao = new UserDaoJdbc(dataSource);
        userDao.deleteAll();
    }

    @Test
    void count() throws Exception {
        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);
        assertThat(userDao.getCount()).isOne();

        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        assertThat(userDao.getCount()).isEqualTo(3);
    }

    @Test
    void addAndGet() throws Exception {
        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        User userGet1 = userDao.get(user1.getId());
        checkSameUser(userGet1, user1);

        User userGet2 = userDao.get(user2.getId());
        checkSameUser(userGet2, user2);
    }

    @Test
    void sqlExceptionTranslate() throws Exception {
        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            assertThat(set.translate(null, null, sqlEx)).isExactlyInstanceOf(DuplicateKeyException.class);
        }
    }

    @Test
    void getUserFailure() throws Exception {
        assertThat(userDao.getCount()).isZero();

        assertThatThrownBy(() -> userDao.get("unknown_id"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void getAll() throws Exception {

        final List<User> users0 = userDao.getAll();
        assertThat(users0).isEmpty();

        userDao.add(user1);
        final List<User> users1 = userDao.getAll();
        assertThat(users1).hasSize(1);
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        final List<User> users2 = userDao.getAll();
        assertThat(users2).hasSize(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        userDao.add(user3);
        final List<User> users3 = userDao.getAll();
        assertThat(users3).hasSize(3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    @Test
    void update() throws Exception {
        userDao.add(user1);
        userDao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(100);
        user1.setRecommend(999);
        userDao.update(user1);

        final User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);

        final User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

}