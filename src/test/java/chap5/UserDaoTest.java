package chap5;

import org.junit.jupiter.api.BeforeEach;

class UserDaoTest {
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        this.user1 = Fixture.getUser("serverwizard", "홍종완", "test", Level.BASIC, 1, 0);
        this.user2 = Fixture.getUser("javajigi", "자바지기", "test2", Level.SILVER, 55, 10);
        this.user3 = Fixture.getUser("slipp", "포비", "test3", Level.GOLD, 100, 40);
    }
}
