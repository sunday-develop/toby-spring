package springbook.user.dao;

import springbook.user.domain.User;
import springbook.user.exeception.DuplicateUserIdException;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    void add(User user) throws DuplicateUserIdException;

    User get(String id) throws SQLException;

    void deleteAll();

    int getCount();

    List<User> getAll();

}
