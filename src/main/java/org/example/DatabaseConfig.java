package org.example;

import org.postgresql.ds.PGSimpleDataSource;
/**
 * The DatabaseConfig class provides methods for configuring a PostgreSQL data source
 * for establishing database connections.
 */
public class DatabaseConfig {
    /**
     * Configures a PostgreSQL data source with default connection parameters.
     * You may need to customize the parameters as per your database configuration.
     *
     * @return A configured PostgreSQL data source.
     */
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
