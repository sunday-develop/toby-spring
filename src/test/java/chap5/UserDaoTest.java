package chap5;

import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

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

    private void checkSameUser(User user1,
                               User user2) {

        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }
}
