package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractUpdatableSqlRegistryTest {

    private UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Test
    void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    protected void checkFindResult(String expected1, String expected2, String expected3) {
        assertEquals(sqlRegistry.findSql("KEY1"), expected1);
        assertEquals(sqlRegistry.findSql("KEY2"), expected2);
        assertEquals(sqlRegistry.findSql("KEY3"), expected3);
    }

    @Test
    void unknownKey() {
        assertThrows(SqlNotFoundException.class, () -> sqlRegistry.findSql("SQL999"));
    }

    @Test
    void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

}
