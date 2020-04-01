package com.study.spring.user.service;

import com.study.spring.user.domain.User;

public interface UserService {

    void add(User user);

    void upgradeLevels();
}
