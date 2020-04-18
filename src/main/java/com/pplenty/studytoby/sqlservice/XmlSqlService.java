package com.pplenty.studytoby.sqlservice;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yusik on 2020/04/18.
 */
public class XmlSqlService implements SqlService {

    private final Map<String, String> sqlMap = new HashMap<>();

    private String sqlMapFile;

    public void loadSql() {

        try {
            String contextPath = Sqlmap.class.getPackage().getName();
            JAXBContext context;
            context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileInputStream(new File(this.sqlMapFile)));

            List<SqlType> sqlList = sqlmap.getSql();
            for (SqlType sqlType : sqlList) {
                this.sqlMap.put(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = this.sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }
}
