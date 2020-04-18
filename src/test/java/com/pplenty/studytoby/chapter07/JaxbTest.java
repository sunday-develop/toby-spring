package com.pplenty.studytoby.chapter07;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/04/18.
 */
public class JaxbTest {

    @DisplayName("언마샬링 학습")
    @Test
    void readSqlmap() throws JAXBException, IOException {

        // given
        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);
        Unmarshaller unmarshaller = context.createUnmarshaller();
//        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("src/test/resources/test-sqlmap.xml"));
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileInputStream(new File("src/test/resources/test-sqlmap.xml")));

        // when
        List<SqlType> sqlList = sqlmap.getSql();
        for (SqlType sqlType : sqlList) {
            System.out.printf("key: %s, value: %s\n", sqlType.getKey(), sqlType.getValue());
        }

        // then
        assertThat(sqlList.size()).isEqualTo(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(0).getValue()).isEqualTo("insert");
        assertThat(sqlList.get(1).getKey()).isEqualTo("get");
        assertThat(sqlList.get(1).getValue()).isEqualTo("select");
        assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
        assertThat(sqlList.get(2).getValue()).isEqualTo("delete");
    }
}
