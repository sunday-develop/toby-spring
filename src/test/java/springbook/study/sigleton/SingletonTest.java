package springbook.study.sigleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.config.Config;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;

public class SingletonTest {

    public static void main(String[] args) {
        final Config factory = new Config();
        final UserDao dao1 = factory.userDao();
        final UserDao dao2 = factory.userDao();

        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2);

        System.out.println("------------------------------------");

        final ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        final UserDao dao3 = context.getBean(UserDaoJdbc.class);
        final UserDao dao4 = context.getBean(UserDaoJdbc.class);

        System.out.println(dao3);
        System.out.println(dao4);
        System.out.println(dao3 == dao4);
    }

}
