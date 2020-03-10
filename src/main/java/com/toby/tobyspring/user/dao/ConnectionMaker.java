package com.toby.tobyspring.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    public DataSource makeNewConnection() throws ClassNotFoundException, SQLException;
}
