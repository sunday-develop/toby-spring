package com.toby.tobyspring.user.dao;

import com.toby.tobyspring.user.domain.User;

import java.sql.*;

public abstract class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao() {
        this.connectionMaker = new DUserConnectionMaker();
    }
    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeNewConnection();
        // sql 실행
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();

        // 자원 회수
        preparedStatement.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeNewConnection();

        // sql 실행
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id = ?");
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        // 자원 회수
        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }
}
