package springbook.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import springbook.user.sqlservice.EmbeddedDbSqlRegistry;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;

@Configuration
public class SqlServiceConfig {

    @Bean
    public SqlService sqlService(SqlMapConfig sqlMapConfig) {
        final OxmSqlService oxmSqlService = new OxmSqlService(unmarshaller(), sqlRegistry());
        oxmSqlService.setSqlmap(sqlMapConfig.getSqlMapResource());
        return oxmSqlService;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        final Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("springbook.user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        return new EmbeddedDbSqlRegistry(embeddedDatabase());
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/embeddeddb/sqlRegistrySchema.sql")
                .build();
    }

}
