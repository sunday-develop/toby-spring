package springbook.learningtest.oxm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OxmTestConfig.class)
public class OxmTest {

    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    void unmarshallSqlMap() throws Exception {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/sqlmap.xml");
        final Source xmlSource = new StreamSource(inputStream);

        final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

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
