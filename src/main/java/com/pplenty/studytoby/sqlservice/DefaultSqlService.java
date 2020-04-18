package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/19.
 */
public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
