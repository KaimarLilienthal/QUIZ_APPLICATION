package org.example;

import org.postgresql.ds.PGSimpleDataSource;

public class DatabaseConfig {
    public static PGSimpleDataSource configureDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("student123");
        return dataSource;
    }
}
