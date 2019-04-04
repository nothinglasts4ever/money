package com.github.nl4.money;

import org.flywaydb.core.Flyway;

public class DatabaseDataLoader {

    public void createSchema() {
        Flyway flyway  = Flyway.configure()
                .dataSource("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1", "sa", "")
                .load();
        flyway.migrate();
    }

}