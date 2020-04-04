package chap5.domain;

import chap5.domain.User;

public interface UserDao {
    void add(User user);

    User get(String id);

    void deleteAll();

    int getCount();

    void update(User user);
}
