package springbook.learningtest.jdk.jaxb;

import org.junit.jupiter.api.Test;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JaxbTest {

    @Test
    void readSqlmap() throws Exception {
        final String contextPath = Sqlmap.class.getPackageName();
        final JAXBContext context = JAXBContext.newInstance(contextPath);

        final Unmarshaller unmarshaller = context.createUnmarshaller();

        final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getClassLoader().getResource("sqlmap.xml"));

        final List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList).hasSize(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(0).getValue()).isEqualTo("insert");
        assertThat(sqlList.get(1).getKey()).isEqualTo("get");
        assertThat(sqlList.get(1).getValue()).isEqualTo("select");
        assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
        assertThat(sqlList.get(2).getValue()).isEqualTo("delete");
    }

}
