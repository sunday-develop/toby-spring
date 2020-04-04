package chap5;

public interface UserDao {
    void add(User user);

    User get(String id);

    void deleteAll();

    int getCount();

    void update(User user);
}
