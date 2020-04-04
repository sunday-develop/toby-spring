package chap5;

import chap5.domain.Level;
import chap5.domain.User;

import java.util.Arrays;
import java.util.List;

public class Fixture {

    public static User getServerwizardUser() {
        return User.of("wizard", "홍종완", "test", Level.BASIC, 1, 0);
    }

    public static User getJavajigiUser() {
        return User.of("javajigi", "자바지기", "test2", Level.SILVER, 55, 10);
    }

    public static User getSlippUser() {
        return User.of("slipp", "포비", "test3", Level.GOLD, 100, 40);
    }

    public static List<User> getUsersForUpgradeLevelsTest() {
        return Arrays.asList(
                User.of("test1", "테스트사용자1", "password1", Level.BASIC, 49, 0),
                User.of("test2", "테스트사용자2", "password2", Level.BASIC, 50, 0),
                User.of("test3", "테스트사용자3", "password3", Level.SILVER, 60, 29),
                User.of("test4", "테스트사용자4", "password4", Level.SILVER, 60, 30),
                User.of("test5", "테스트사용자5", "password5", Level.GOLD, 100, 100)
        );
    }
}
