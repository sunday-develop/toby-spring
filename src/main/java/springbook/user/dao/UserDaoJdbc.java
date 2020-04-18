package springbook.user.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UserDaoJdbc implements UserDao {

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> User.builder()
            .id(rs.getString("id"))
            .name(rs.getString("name"))
            .password(rs.getString("password"))
            .level(Level.valueOf(rs.getInt("level")))
            .login(rs.getInt("login"))
            .recommend(rs.getInt("recommend"))
            .email(rs.getString("email"))
            .build();

    private static final String USER_GET = "select * from users where id = :id";
    private static final String USER_DELETE_ALL = "delete from users";
    private static final String USER_GET_COUNT = "select count(*) from users";
    private static final String USER_GET_ALL = "select * from users order by id";
    private static final String USER_UPDATE = "update users \n" +
            "set name = :name, password = :password, level = :level, login = :login, recommend = :recommend\n" +
            "where id = :id";

    private static final Function<User, Map<String, ?>> USER_PARAM_CREATOR = user -> Map.<String, Object>of(
            "id", user.getId(),
            "name", user.getName(),
            "password", user.getPassword(),
            "level", user.getLevel().intValue(),
            "login", user.getLogin(),
            "recommend", user.getRecommend(),
            "email", user.getEmail()
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String sqlAdd;

    public UserDaoJdbc(DataSource dataSource, String sqlAdd) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.sqlAdd = sqlAdd;
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(sqlAdd, USER_PARAM_CREATOR.apply(user));
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(USER_GET, Map.<String, Object>of("id", id), USER_MAPPER);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(USER_DELETE_ALL, Map.of());
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(USER_GET_COUNT, Map.of(), Integer.class);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(USER_GET_ALL, USER_MAPPER);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(USER_UPDATE, USER_PARAM_CREATOR.apply(user));
    }

}
