import com.study.spring.user.dao.ConnectionMaker;
import com.study.spring.user.dao.DConnectionMaker;
import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);

        User user1 = new User();
        user1.setId("whiteship");
        user1.setName("백기선");
        user1.setPassword("married");

        userDao.add(user1);

        System.out.println(user1.getId() + " 등록 성공");

        User user2 = userDao.get(user1.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
