package cl.kibernunmacademy.rest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

public class DatabaseInitializer {

  @Value("${DB_URL}")
  private String dbUrl;

  @Value("${DB_USER}")
  private String user;

  @Value("${DB_PASS}")
  private String pass;

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbUrl, user, pass);
  }


}
