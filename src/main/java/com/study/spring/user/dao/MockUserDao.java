package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {

    private List<User> userList;
    private List<User> updatedList;

    public MockUserDao(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUpdatedList() {
        return updatedList;
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAll() {
        return userList;
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(User user) {

        if (CollectionUtils.isEmpty(updatedList)) {
            updatedList = new ArrayList<>();
        }

        updatedList.add(user);
    }
}
