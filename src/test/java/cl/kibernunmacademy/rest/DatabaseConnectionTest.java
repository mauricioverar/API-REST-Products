package cl.kibernunmacademy.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cdimascio.dotenv.Dotenv;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

  private static final Dotenv dotenv = Dotenv.load();
  private static final String DB_URL = dotenv.get("DB_URL");
  private static final String USER = dotenv.get("DB_USER");
  private static final String PASS = dotenv.get("DB_PASS");

  @Test
  void testConnection() {
    assertNotNull(DB_URL, "❌ Variable DB_URL no definida.");
    assertNotNull(USER, "❌ Variable DB_USER no definida.");
    assertNotNull(PASS, "❌ Variable DB_PASS no definida.");

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
      assertNotNull(conn, "✅ Conexión establecida correctamente.");
    } catch (SQLException e) {
      fail("❌ Falló la conexión a la base de datos: " + e.getMessage());
    }
  }

  @Test
  void dummyTest() {
    assertTrue(true);
  }
}
