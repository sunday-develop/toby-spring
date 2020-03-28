package springbook.user.domain;

import lombok.*;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Setter(AccessLevel.PRIVATE)
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

    public void upgradeLevel() {
        final Level nextLevel = level.nextLevel()
                .orElseThrow(() -> new IllegalStateException(level + "은 업그레이드가 불가능 합니다."));

        this.level = nextLevel;
    }

}
