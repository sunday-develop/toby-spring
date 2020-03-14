package springbook.user.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.config.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    @Test
    void addAndGet() throws Exception {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao dao = context.getBean(UserDao.class);

        final User user = User.builder()
                .id("gyumee")
                .name("박성철")
                .password("springno1")
                .build();

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());

        assertThat(user2.getName()).isEqualTo(user.getName());
        assertThat(user2.getPassword()).isEqualTo(user.getPassword());
    }

}