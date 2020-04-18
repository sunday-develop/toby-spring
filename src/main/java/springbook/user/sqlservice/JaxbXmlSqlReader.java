package springbook.user.sqlservice;

import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class JaxbXmlSqlReader implements SqlReader {

    private final String sqlmapFile;

    public JaxbXmlSqlReader(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        final String contextPath = Sqlmap.class.getPackageName();
        try {
            final JAXBContext context = JAXBContext.newInstance(contextPath);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final InputStream is = getClass().getClassLoader().getResourceAsStream(sqlmapFile);
            final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
