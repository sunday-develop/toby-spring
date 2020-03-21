package com.pplenty.studytoby;

import java.util.List;

/**
 * Created by yusik on 2020/03/22.
 */
public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
