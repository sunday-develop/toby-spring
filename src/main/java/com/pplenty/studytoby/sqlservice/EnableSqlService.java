package com.pplenty.studytoby.sqlservice;

import org.springframework.context.annotation.Import;

/**
 * Created by yusik on 2020/04/26.
 */
@Import(SqlServiceContext.class)
public @interface EnableSqlService {
}
