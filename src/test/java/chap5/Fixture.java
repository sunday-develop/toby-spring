package chap5;

import chap5.domain.Level;
import chap5.domain.User;

public class Fixture {

    public static User getUser(String id,
                               String name,
                               String password,
                               Level level,
                               int login,
                               int recommend) {

        return User.of(id, name, password, level, login, recommend);
    }
}
