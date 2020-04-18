package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlRetrievalFailureException;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XmlSqlService implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {

        String contextPath = jaxb.Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);

            ClassLoader classLoader = getClass().getClassLoader();
            File sqlMapFile = new File(Objects.requireNonNull(classLoader.getResource("sqlmap/sqlmap.xml")).getFile());

            Unmarshaller unmarshaller = context.createUnmarshaller();
            jaxb.Sqlmap sqlmap = (jaxb.Sqlmap) unmarshaller.unmarshal(sqlMapFile);

            for (jaxb.SqlType sqlType : sqlmap.getSql()) {
                sqlMap.put(sqlType.getKey(), sqlType.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if (StringUtils.isEmpty(sql)) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
