package springbook.user.sqlservice;

public class DefaultSqlService extends BaseSqlService {

    private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";

    public DefaultSqlService() {
        super(new JaxbXmlSqlReader(DEFAULT_SQLMAP_FILE), new HashMapSqlRegistry());
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

}
