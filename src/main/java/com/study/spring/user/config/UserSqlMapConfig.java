package com.study.spring.user.config;

import com.study.spring.user.dao.UserDao;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class UserSqlMapConfig implements SqlMapConfig {

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap/sqlmap.xml", UserDao.class);
    }
}
