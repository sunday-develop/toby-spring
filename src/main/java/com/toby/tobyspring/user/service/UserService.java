package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.domain.User;

public interface UserService {
    void add(User user);

    void upgrades();
}
