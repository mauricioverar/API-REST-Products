package cl.kibernunmacademy.rest.security.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalSecurityExceptionAdvice {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
    return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
      HttpServletRequest req) {
    return error(HttpStatus.BAD_REQUEST, "Validation error", req.getRequestURI());
  }

  private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, String path) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", message);
    body.put("code", status.value());
    body.put("timestamp", Instant.now().toString());
    body.put("path", path);
    return ResponseEntity.status(status).body(body);
  }
}
