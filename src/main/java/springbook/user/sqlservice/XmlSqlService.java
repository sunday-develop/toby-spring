package springbook.user.sqlservice;

import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService {

    private final Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        final String contextPath = Sqlmap.class.getPackageName();
        try {
            final JAXBContext context = JAXBContext.newInstance(contextPath);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final InputStream is = getClass().getClassLoader().getResourceAsStream("sqlmap.xml");
            final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        final String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }

}
