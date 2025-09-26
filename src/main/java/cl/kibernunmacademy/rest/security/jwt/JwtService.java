package cl.kibernunmacademy.rest.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import cl.kibernunmacademy.rest.security.domain.Role;
import cl.kibernunmacademy.rest.security.domain.UserAccount;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtService.class);
  private final JwtProperties props;
  private final Clock clock;

  @Autowired
  public JwtService(JwtProperties props) {
    this(props, Clock.systemUTC());
  }

  public JwtService(JwtProperties props, Clock clock) {
    this.props = props;
    this.clock = clock;
  }

  public String generateAccessToken(UserAccount user) {
    return generateToken(user, props.getJwt().getAccessTtl().toSeconds());
  }

  public String generateRefreshToken(UserAccount user) {
    if (!props.getJwt().isRefreshEnabled()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh tokens are disabled");
    }
    return generateToken(user, props.getJwt().getRefreshTtl().toSeconds());
  }

  private List<String> mapRoles(Set<Role> roles) {
    return roles.stream().map(Role::name).collect(Collectors.toList());
  }

  private String generateToken(UserAccount user, long ttlSeconds) {
    try {
      Instant now = clock.instant();
      List<String> roles = mapRoles(user.getRoles());
      JWTClaimsSet claims = new JWTClaimsSet.Builder()
          .subject(user.getEmail())
          .issuer(props.getJwt().getIssuer())
          .issueTime(Date.from(now))
          .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
          .jwtID(UUID.randomUUID().toString())
          .claim("roles", roles)
          .claim("ver", user.getTokenVersion())
          .build();

      JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
      SignedJWT jwt = new SignedJWT(header, claims);
      JWSSigner signer = new MACSigner(getSecret());
      jwt.sign(signer);
      String token = jwt.serialize();
      log.debug("Issued token for {}: {}...", user.getEmail(), token.substring(0, Math.min(10, token.length())));
      return token;
    } catch (JOSEException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate token");
    }
  }

  public JwtPayload parseAndValidate(String token) {
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(getSecret());
      if (!jwt.verify(verifier)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token signature");
      }

      JWTClaimsSet claims = jwt.getJWTClaimsSet();
      validateStandardClaims(claims);

      String subject = claims.getSubject();
      @SuppressWarnings("unchecked")
      List<String> roles = (List<String>) claims.getClaim("roles");
      Integer ver = claims.getIntegerClaim("ver");
      String jti = claims.getJWTID();

      return new JwtPayload(subject, roles, ver == null ? 0 : ver, jti, claims.getExpirationTime());
    } catch (ParseException | JOSEException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
  }

  private void validateStandardClaims(JWTClaimsSet claims) {
    Instant now = clock.instant();
    String issuer = claims.getIssuer();
    Date exp = claims.getExpirationTime();
    if (issuer == null || !issuer.equals(props.getJwt().getIssuer())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid issuer");
    }
    if (exp == null || exp.toInstant().isBefore(now.minusSeconds(30))) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
    }
  }

  private String getSecret() {
    String secret = props.getJwt().getSecret();
    if (secret == null || secret.length() < 32) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT secret not configured properly");
    }
    return secret;
  }

  public static class JwtPayload {
    private final String subject;
    private final List<String> roles;
    private final int version;
    private final String jti;
    private final Date expiresAt;

    public JwtPayload(String subject, List<String> roles, int version, String jti, Date expiresAt) {
      this.subject = subject;
      this.roles = roles;
      this.version = version;
      this.jti = jti;
      this.expiresAt = expiresAt;
    }

    public String getSubject() {
      return subject;
    }

    public List<String> getRoles() {
      return roles;
    }

    public int getVersion() {
      return version;
    }

    public String getJti() {
      return jti;
    }

    public Date getExpiresAt() {
      return expiresAt;
    }
  }
}
