package com.aressoftware.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * DatabaseConfig
 *
 * Clase encargada de gestionar la conexión a la base de datos PostgreSQL.
 * Implementa el patrón Singleton para asegurar una única instancia.
 *
 * Configuración leída desde /resources/config/app.properties
 */
public class DatabaseConfig {

    private static DatabaseConfig instance;

    private String url;
    private String user;
    private String password;
    private int poolSize;

    private DatabaseConfig() {
        loadProperties();
        registerDriver();
    }

    /**
     * Obtiene la instancia Singleton de DatabaseConfig.
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Carga las variables del archivo app.properties ubicado en resources/config
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config/app.properties")) {

            if (input == null) {
                throw new RuntimeException("❌ No se encontró el archivo config/app.properties");
            }

            Properties props = new Properties();
            props.load(input);

            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            this.poolSize = Integer.parseInt(props.getProperty("db.pool.size", "5"));

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Parámetros de conexión faltantes en app.properties");
            }

            System.out.println("✔ Configuración de base de datos cargada correctamente");

        } catch (Exception e) {
            throw new RuntimeException(" Error cargando configuración de base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Registra el driver de PostgreSQL
     */
    private void registerDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✔ Driver PostgreSQL registrado correctamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver de PostgreSQL no encontrado: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos PostgreSQL.
     * Se recomienda usar en un bloque try-with-resources.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public int getPoolSize() {
        return poolSize;
    }
}
