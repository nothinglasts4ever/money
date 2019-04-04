package com.github.nl4.money.config;

import com.github.nl4.money.controller.PersonController;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PersonController.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public DataSourceConnectionProvider connectionProvider() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        return new DataSourceConnectionProvider(dataSource);
    }

    @Provides
    @Singleton
    public DSLContext dsl() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(SQLDialect.H2);
        return new DefaultDSLContext(jooqConfiguration);
    }

}