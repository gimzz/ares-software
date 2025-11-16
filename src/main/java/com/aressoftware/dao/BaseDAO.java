package com.aressoftware.dao;

import com.aressoftware.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {

    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getInstance().getConnection();
    }
}
