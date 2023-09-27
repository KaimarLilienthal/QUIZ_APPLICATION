package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * Main class serves as the entry point for the application.
 */
public class Main {
    public static void main(String[] args) {
        // Configure the PostgreSQL data source
        PGSimpleDataSource dataSource = DatabaseConfig.configureDataSource();

        try {
            // Obtain a database connection
            Connection connection = dataSource.getConnection();
            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur
            e.printStackTrace();
        }
    }
}