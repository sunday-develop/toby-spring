package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

    public void add(User user) throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/springbook";
        final String dbUser = "spring";
        final String dbPassword = "book";
        Connection con = DriverManager.getConnection(url, dbUser, dbPassword);

        final PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public User get(String id) throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/springbook";
        final String dbUser = "spring";
        final String dbPassword = "book";
        Connection con = DriverManager.getConnection(url, dbUser, dbPassword);

        final PreparedStatement ps = con.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        final ResultSet rs = ps.executeQuery();
        rs.next();

        final User user = User.of(rs.getString("id"), rs.getString("name"), rs.getString("password"));

        rs.close();
        con.close();

        return user;
    }

    public static void main(String[] args) throws SQLException {
        final UserDao dao = new UserDao();

        final User user = User.of("whiteship", "백기선", "married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

}
