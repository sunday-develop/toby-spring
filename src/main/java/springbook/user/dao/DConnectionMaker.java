package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/springbook";
        final String user = "spring";
        final String password = "book";
        return DriverManager.getConnection(url, user, password);
    }

}
