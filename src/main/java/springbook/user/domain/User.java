package springbook.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    @Builder
    private User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.password = Objects.requireNonNull(password);
        this.level = Objects.requireNonNull(level);
        this.login = Objects.requireNonNull(login);
        this.recommend = Objects.requireNonNull(recommend);
    }

    public static User of(String id, String name, String password, Level level, int login, int recommend) {
        return new User(id, name, password, level, login, recommend);
    }

}
