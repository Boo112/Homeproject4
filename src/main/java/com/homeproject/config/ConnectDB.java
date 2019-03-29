package com.homeproject.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDB extends СonfigurationDB {
    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        Connection dbConnection;
        Properties properties = new Properties();
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPassword);
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "cp1251");

        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, properties);

        return dbConnection;
    }
}
