package com.pplenty.studytoby;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yusik on 2020/04/04.
 */
@Transactional
public interface UserService {

    void add(User user);

    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();

    void deleteAll();

    void update(User user);

    void upgradeLevels();
}
