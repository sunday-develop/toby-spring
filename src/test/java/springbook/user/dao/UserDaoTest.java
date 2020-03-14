package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.config.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;
import java.util.Objects;

class UserDaoTest {

    public static void main(String[] args) throws SQLException {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao dao = context.getBean(UserDao.class);

        final User user = User.of("whiteship", "백기선", "married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());

        if (!Objects.equals(user.getName(), user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!Objects.equals(user.getPassword(), user.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("테스트 성공");
        }

    }

}