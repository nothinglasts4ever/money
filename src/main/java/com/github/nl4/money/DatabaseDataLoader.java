package com.github.nl4.money;

import com.github.nl4.money.config.Properties;
import com.google.inject.Inject;
import org.flywaydb.core.Flyway;

public class DatabaseDataLoader {

    @Inject
    Properties properties;

    public void createSchema() {
        String dbUrl = properties.getDbUrl();
        String dbUser = properties.getDbUser();
        String dbPassword = properties.getDbPassword();
        Flyway flyway = Flyway.configure()
                .dataSource(dbUrl, dbUser, dbPassword)
                .load();
        flyway.migrate();
    }

}