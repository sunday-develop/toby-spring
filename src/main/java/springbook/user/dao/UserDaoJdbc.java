package springbook.user.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
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
    private final SqlService sqlService;

    public UserDaoJdbc(DataSource dataSource, SqlService sqlService) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.sqlService = sqlService;
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(sqlService.getSql("userAdd"), USER_PARAM_CREATOR.apply(user));
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), Map.<String, Object>of("id", id), USER_MAPPER);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("userDeleteAll"), Map.of());
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Map.of(), Integer.class);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("userGetAll"), USER_MAPPER);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlService.getSql("userUpdate"), USER_PARAM_CREATOR.apply(user));
    }

}
