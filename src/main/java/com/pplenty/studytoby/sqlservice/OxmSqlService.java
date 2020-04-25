package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/25.
 */
public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return null;
    }

    private class OxmSqlReader implements SqlReader {
        @Override
        public void read(SqlRegistry sqlRegistry) {

        }
    }
}
