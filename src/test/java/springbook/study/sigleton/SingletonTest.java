package springbook.study.sigleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.config.DaoFactory;
import springbook.user.dao.UserDao;

public class SingletonTest {

    public static void main(String[] args) {
        final DaoFactory factory = new DaoFactory();
        final UserDao dao1 = factory.userDao();
        final UserDao dao2 = factory.userDao();

        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2);

        System.out.println("------------------------------------");

        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao dao3 = context.getBean(UserDao.class);
        final UserDao dao4 = context.getBean(UserDao.class);

        System.out.println(dao3);
        System.out.println(dao4);
        System.out.println(dao3 == dao4);
    }

}
