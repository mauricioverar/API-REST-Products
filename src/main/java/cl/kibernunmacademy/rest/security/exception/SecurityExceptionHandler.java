package cl.kibernunmacademy.rest.security.exception;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", request.getRequestURI());

  }

  
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    writeJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", request.getRequestURI());

  }


  private void writeJson(HttpServletResponse response, int status, String error, String requestURI) throws IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    Map<String, Object> body = new HashMap<>();
    body.put("error", error);
    body.put("code", status);
    body.put("path", requestURI);
    body.put("timestamp", Instant.now().toString());
    objectMapper.writeValue(response.getWriter(), body);
  }
  
}
