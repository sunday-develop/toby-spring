package springbook.user.sqlservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractUpdatableSqlRegistryTest {

    protected UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setup() {
        sqlRegistry = createUpdatableSqlRegistry();

        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    protected abstract UpdatableSqlRegistry createUpdatableSqlRegistry();

    protected void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1);
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2);
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3);
    }

    @Test
    void find() throws Exception {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    @Test
    void unknownKey() throws Exception {
        assertThatThrownBy(() -> sqlRegistry.findSql("SQL9999!@#$"))
                .isInstanceOf(SqlNotFoundException.class);
    }

    @Test
    void updateSingle() throws Exception {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    void updateMulti() throws Exception {
        final Map<String, String> sqlmap = Map.of(
                "KEY1", "Modified1",
                "KEY3", "Modified3"
        );

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test
    void updateWithNotExistingKey() throws Exception {
        assertThatThrownBy(() -> sqlRegistry.updateSql("SQL9999!@#$", "Modified2"))
                .isInstanceOf(SqlUpdateFailureException.class);
    }

}
