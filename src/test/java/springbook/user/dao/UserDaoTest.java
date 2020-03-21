package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.user.domain.User;
import springbook.user.exeception.DuplicateUserIdException;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDaoTest {

    private UserDao userDao;

    private final User user1 = User.of("gyumee", "박성철", "springno1");
    private final User user2 = User.of("leegw700", "이길원", "springno2");
    private final User user3 = User.of("bumjin", "박범진", "springno3");

    @BeforeEach
    void setup() {
        final DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/springbook", "spring", "book", true
        );

        userDao = new UserDao(dataSource);
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
        assertThat(userGet1.getName()).isEqualTo(user1.getName());
        assertThat(userGet1.getPassword()).isEqualTo(user1.getPassword());

        User userGet2 = userDao.get(user2.getId());
        assertThat(userGet2.getName()).isEqualTo(user2.getName());
        assertThat(userGet2.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    void addDuplicateUserId() throws Exception {
        userDao.add(user1);

        assertThatThrownBy(() -> userDao.add(user1))
                .isInstanceOf(DuplicateUserIdException.class);
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

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }

}