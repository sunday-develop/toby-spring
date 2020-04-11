package com.pplenty.studytoby;

import java.util.List;

/**
 * Created by yusik on 2020/04/04.
 */
public interface UserService {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    void upgradeLevels();
}
