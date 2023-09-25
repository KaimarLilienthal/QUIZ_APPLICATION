package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        PGSimpleDataSource dataSource = DatabaseConfig.configureDataSource();

        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}