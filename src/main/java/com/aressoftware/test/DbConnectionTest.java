package com.aressoftware.test;

import com.aressoftware.config.DatabaseConfig;
import java.sql.Connection;

public class DbConnectionTest {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
            System.out.println("✔ Conexión a la base de datos exitosa");
        } catch (Exception e) {
            System.out.println("✘ Error conectando a la base de datos");
            e.printStackTrace();
        }
    }
}
