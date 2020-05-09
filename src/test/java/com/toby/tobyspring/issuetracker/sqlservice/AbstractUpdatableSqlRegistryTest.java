package com.toby.tobyspring.issuetracker.sqlservice;

import com.toby.tobyspring.issuetracker.exception.SqlUpdateFailureException;
import com.toby.tobyspring.user.sqlservice.SqlNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UpdatableSqlRegistry에 대한 테스트 추상 클래스")
abstract class AbstractUpdatableSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    public void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Test
    @DisplayName("setUp 메서드 데이터 셋팅 확인")
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    protected void checkFindResult(String sql1, String sql2, String sql3) {
        assertEquals(sql1, sqlRegistry.findSql("KEY1"));
        assertEquals(sql2, sqlRegistry.findSql("KEY2"));
        assertEquals(sql3, sqlRegistry.findSql("KEY3"));
    }

    @Test
    @DisplayName("주어진 key에 해당하는 sql을 찾을 수 없을 때 예외 확인 테스트")
    public void unknownKey() {
        assertThrows(SqlNotFoundException.class, () -> sqlRegistry.findSql("SQL123123123"));
    }

    @Test
    @DisplayName("하나의 sql만 변경하는 기능에 대한 테스트")
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "MODIFY2");
        checkFindResult("SQL1", "MODIFY2", "SQL3");
    }

    @Test
    @DisplayName("한 번에 여러 sql 수정하는 기능 검증")
    public void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "MODIFY1");
        sqlmap.put("KEY3", "MODIFY3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("MODIFY1", "SQL2", "MODIFY3");
    }

    @Test
    @DisplayName("존재하지 않는 키의 sql을 변경하려고 시도할 때 예외 검증")
    public void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> sqlRegistry.updateSql("SQL12341234", "MODIFY2"));
    }
}