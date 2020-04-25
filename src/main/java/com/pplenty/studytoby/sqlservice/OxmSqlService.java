package com.pplenty.studytoby.sqlservice;

import org.springframework.oxm.Unmarshaller;

/**
 * Created by yusik on 2020/04/25.
 */
public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return null;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }


    private static class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private String sqlmapFile;

        @Override
        public void read(SqlRegistry sqlRegistry) {

        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }
    }
}
