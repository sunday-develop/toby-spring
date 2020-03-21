package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import com.study.spring.user.exception.DuplicationUserIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-test.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        userDao = context.getBean("userDao", UserDao.class);
        user1 = new User("user1", "username1", "username11");
        user2 = new User("user2", "username2", "username22");
        user3 = new User("user3", "username3", "username33");

        System.out.println(this.context);
        System.out.println(this);
    }

    @DisplayName("getAll()에 대한 테스트")
    @Test
    public void getAll() {
        userDao.deleteAll();

        List<User> userList0 = userDao.getAll();
        assertEquals(userList0.size(), 0);

        userDao.add(user1);
        List<User> userList1 = userDao.getAll();
        assertEquals(userList1.size(), 1);
        checkSameUser(user1, userList1.get(0));

        userDao.add(user2);
        List<User> userList2 = userDao.getAll();
        assertEquals(userList2.size(), 2);
        checkSameUser(user1, userList2.get(0));
        checkSameUser(user2, userList2.get(1));

        userDao.add(user3);
        List<User> userList3 = userDao.getAll();
        assertEquals(userList3.size(), 3);
        checkSameUser(user1, userList3.get(0));
        checkSameUser(user2, userList3.get(1));
        checkSameUser(user3, userList3.get(2));

    }

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    public void addAndGet() {

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        userDao.add(user2);
        assertEquals(userDao.getCount(), 2);

        User userTarget1 = userDao.get(user1.getId());
        assertEquals(userTarget1.getName(), user1.getName());
        assertEquals(userTarget1.getPassword(), user1.getPassword());

        User userTarget2 = userDao.get(user2.getId());
        assertEquals(userTarget2.getName(), user2.getName());
        assertEquals(userTarget2.getPassword(), user2.getPassword());
    }

    @DisplayName("get() 메소드의 예외 상황에 대한 테스트")
    @Test
    public void getUserFailure() {

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown"));
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

    public void useUserDaoAddMethod() throws DuplicateKeyException {
        try {
            userDao.add(new User());
        } catch (DuplicateKeyException e) {
            throw new DuplicationUserIdException(e); // 예외를 전환할 때는 원인이 되는 예외를 중첩하는 것이 좋다.
        }
    }
}
