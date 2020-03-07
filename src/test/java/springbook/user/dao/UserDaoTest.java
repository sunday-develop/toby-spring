package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.config.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

class UserDaoTest {

    public static void main(String[] args) throws SQLException {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao dao = context.getBean(UserDao.class);

        final User user = User.of("whiteship", "백기선", "married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

}