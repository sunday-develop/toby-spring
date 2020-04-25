package com.pplenty.studytoby.chapter07;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/04/25.
 */
@DisplayName("서비스 추상화")
@SpringBootTest
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class OxmTest {

    @Autowired
    Unmarshaller unmarshaller;

    @DisplayName("sqlMap ")
    @Test
    void unmarshallSqlMap() throws IOException {
        // given

        Source xmlSource = new StreamSource(new FileInputStream(new File("src/test/resources/test-sqlmap.xml")));
//        Source xmlSource = new StreamSource(getClass().getResourceAsStream("di/user-sqlmap.xml"));
        System.out.println(xmlSource);

        // when
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);
        List<SqlType> sqlList = sqlmap.getSql();
        System.out.println(sqlList);

        // then
        assertThat(sqlList.size()).isEqualTo(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(2).getValue()).isEqualTo("delete");
    }
}
