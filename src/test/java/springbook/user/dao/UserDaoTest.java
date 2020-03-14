package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.user.domain.User;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDaoTest {

    private UserDao dao;

    private final User user1 = User.of("gyumee", "박성철", "springno1");
    private final User user2 = User.of("leegw700", "이길원", "springno2");
    private final User user3 = User.of("bumjin", "박범진", "springno3");

    @BeforeEach
    void setup() {
        final DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/springbook", "spring", "book", true
        );

        dao = new UserDao(dataSource, new JdbcContext(dataSource));
    }

    @Test
    void count() throws Exception {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(user1);
        assertThat(dao.getCount()).isOne();

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    @Test
    void addAndGet() throws Exception {
        final User user1 = User.of("gyumee", "박성철", "springno1");
        final User user2 = User.of("leegw700", "이길원", "springno2");

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName()).isEqualTo(user1.getName());
        assertThat(userGet1.getPassword()).isEqualTo(user1.getPassword());

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName()).isEqualTo(user2.getName());
        assertThat(userGet2.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    void getUserFailure() throws Exception {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        assertThatThrownBy(() -> dao.get("unknown_id"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

}