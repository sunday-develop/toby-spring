package com.pplenty.studytoby.sqlservice;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by yusik on 2020/04/19.
 */
public class JaxbXmlSqlReader implements SqlReader {

    private String sqlMapFile;

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        try {
            String contextPath = Sqlmap.class.getPackage().getName();
            JAXBContext context;
            context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileInputStream(new File(this.sqlMapFile)));

            List<SqlType> sqlList = sqlmap.getSql();
            for (SqlType sqlType : sqlList) {
                sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
