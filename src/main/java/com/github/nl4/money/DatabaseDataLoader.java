package com.github.nl4.money;

import com.github.nl4.money.config.Properties;
import com.google.inject.Inject;
import org.flywaydb.core.Flyway;

public class DatabaseDataLoader {

    @Inject
    Properties properties;

    public void createSchema() {
        var dbUrl = properties.getDbUrl();
        var dbUser = properties.getDbUser();
        var dbPassword = properties.getDbPassword();
        var flyway = Flyway.configure()
                .dataSource(dbUrl, dbUser, dbPassword)
                .load();
        flyway.migrate();
    }

}