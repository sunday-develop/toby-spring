package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        final Connection con = dataSource.getConnection();

        final PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public User get(String id) throws SQLException {
        User user = null;
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement ps = con.prepareStatement("select * from users where id = ?");) {

            ps.setString(1, id);

            try (final ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    user = User.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .password(rs.getString("password"))
                            .build();
                }
            }
        }

        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement ps = makeStatement(con)) {

            ps.executeUpdate();
        }
    }

    private PreparedStatement makeStatement(Connection con) throws SQLException {
        return con.prepareStatement("delete from users");
    }

    public int getCount() throws SQLException {
        final Connection con = dataSource.getConnection();

        PreparedStatement ps = con.prepareStatement("select count(*) from users");

        final ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        con.close();

        return count;
    }

}
