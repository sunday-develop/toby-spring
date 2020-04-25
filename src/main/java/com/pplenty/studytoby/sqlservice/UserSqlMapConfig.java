package com.pplenty.studytoby.sqlservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by yusik on 2020/04/26.
 */
public class UserSqlMapConfig implements SqlMapConfig{
    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("src/test/resources/test-sqlmap.xml");
    }
}
