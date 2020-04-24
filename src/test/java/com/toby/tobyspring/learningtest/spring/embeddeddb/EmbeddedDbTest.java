package com.toby.tobyspring.learningtest.spring.embeddeddb;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

@DisplayName("내장형 DB 학습 테스트")
public class EmbeddedDbTest {
    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:/sql/schema.sql")
                .addScript("classpath:/sql/data.sql")
                .build();
        template = new JdbcTemplate(db);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    @DisplayName("스크립트 검증 테스트")
    public void initData() {
        Assertions.assertEquals(2, (int) template.queryForObject("select count(*) from sqlmap", Integer.class));

        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
        Assertions.assertEquals("KEY1", list.get(0).get("key_"));
        Assertions.assertEquals("SQL1", list.get(0).get("sql_"));
        Assertions.assertEquals("KEY2", list.get(1).get("key_"));
        Assertions.assertEquals("SQL2", list.get(1).get("sql_"));
    }

    @Test
    @DisplayName("새로운 데이터를 추가하고 확인")
    public void insert() {
        template.update("insert into sqlmap(key_, sql_) values(?,?)", "KEY3", "SQL3");
        Assertions.assertEquals(3, (int) template.queryForObject("select count(*) from sqlmap", Integer.class));
    }
}
