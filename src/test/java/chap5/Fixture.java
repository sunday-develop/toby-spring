package chap5;

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
