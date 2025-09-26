package cl.kibernunmacademy.rest.security.jwt;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class JwtProperties {

  private Auth auth = new Auth();
  private Jwt jwt = new Jwt();
  private Cors cors = new Cors();

  public Auth getAuth() {
    return auth;
  }

  public Jwt getJwt() {
    return jwt;
  }

  public Cors getCors() {
    return cors;
  }

  public static class Auth {

    private boolean registrationEnabled = false;

    public boolean isRegistrationEnabled() {
      return registrationEnabled;
    }

    public void setRegistrationEnabled(boolean registrationEnabled) {
      this.registrationEnabled = registrationEnabled;
    }
  }

  public static class Jwt {

    private String secret;
    private String issuer = "apirest";
    private Duration accessTtl = Duration.ofMinutes(15);
    private Duration refreshTtl = Duration.ofDays(7);
    private boolean refreshEnabled = true;
    private boolean denylistEnabled = false;

    public String getSecret() {
      return secret;
    }

    public void setSecret(String secret) {
      this.secret = secret;
    }

    public String getIssuer() {
      return issuer;
    }

    public void setIssuer(String issuer) {
      this.issuer = issuer;
    }

    public Duration getAccessTtl() {
      return accessTtl;
    }

    public void setAccessTtl(Duration accessTtl) {
      this.accessTtl = accessTtl;
    }

    public Duration getRefreshTtl() {
      return refreshTtl;
    }

    public void setRefreshTtl(Duration refreshTtl) {
      this.refreshTtl = refreshTtl;
    }

    public boolean isRefreshEnabled() {
      return refreshEnabled;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {
      this.refreshEnabled = refreshEnabled;
    }

    public boolean isDenylistEnabled() {
      return denylistEnabled;
    }

    public void setDenylistEnabled(boolean denylistEnabled) {
      this.denylistEnabled = denylistEnabled;
    }
  }

  public static class Cors {

    private List<String> allowedOrigins;

    public List<String> getAllowedOrigins() {
      return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
      this.allowedOrigins = allowedOrigins;
    }
  }
}
