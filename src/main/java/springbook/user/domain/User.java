package springbook.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private String id;
    private String name;
    private String password;

    @Builder
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static User of(String id, String name, String password) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.password = password;
        return user;
    }

}
