package com.github.nl4.money.config;

import com.github.nl4.money.controller.PersonController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
//import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

public class GuiceModule extends AbstractModule {

//    private Environment environment;

    @Override
    protected void configure() {
        bind(PersonController.class);
    }

//    @Provides
    @Singleton
    public DataSourceConnectionProvider connectionProvider() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/library?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow");
        dataSource.setUser("root");
        dataSource.setPassword("1234");
        /*  dataSource.setUrl(environment.getRequiredProperty("db.url"));
        dataSource.setUser(environment.getRequiredProperty("db.username"));
        dataSource.setPassword(environment.getRequiredProperty("db.password"));*/
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Provides
    public DSLContext dsl() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
//        jooqConfiguration.set(new DefaultExecuteListenerProvider(new ExceptionTranslator()));
        jooqConfiguration.set(SQLDialect.MYSQL_5_7);
        return new DefaultDSLContext(jooqConfiguration);
    }

}