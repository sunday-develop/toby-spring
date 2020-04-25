package springbook.config.annotation;

import org.springframework.context.annotation.Import;
import springbook.config.SqlServiceConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(SqlServiceConfig.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableSqlService {

}
