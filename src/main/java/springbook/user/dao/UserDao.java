package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
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

    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

}
