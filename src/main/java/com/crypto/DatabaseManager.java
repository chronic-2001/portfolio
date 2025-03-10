package com.crypto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
  private static final String JDBC_URL = "jdbc:h2:mem:portfolio;INIT=RUNSCRIPT FROM 'classpath:schema.sql'\\;RUNSCRIPT FROM 'classpath:data.sql'";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(JDBC_URL);
  }
}