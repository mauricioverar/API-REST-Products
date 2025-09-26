package cl.kibernunmacademy.rest.security.controller;

import java.net.URI;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.kibernunmacademy.rest.security.domain.Role;
import cl.kibernunmacademy.rest.security.domain.UserAccount;
import cl.kibernunmacademy.rest.security.dto.AuthResponse;
import cl.kibernunmacademy.rest.security.dto.LoginRequest;
import cl.kibernunmacademy.rest.security.dto.RegisterRequest;
import cl.kibernunmacademy.rest.security.dto.UserRegistrationResponse;
import cl.kibernunmacademy.rest.security.jwt.JwtProperties;
import cl.kibernunmacademy.rest.security.jwt.JwtService;
import cl.kibernunmacademy.rest.security.repository.UserAccountRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final UserAccountRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtProperties props;

  public AuthController(AuthenticationManager authManager, JwtService jwtService,
      UserAccountRepository userRepo, PasswordEncoder passwordEncoder,
      JwtProperties props) {
    this.authManager = authManager;
    this.jwtService = jwtService;
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.props = props;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      authManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      UserAccount user = userRepo.findByEmail(request.getEmail()).orElseThrow();
      String access = jwtService.generateAccessToken(user);
      String refresh = props.getJwt().isRefreshEnabled() ? jwtService.generateRefreshToken(user) : null;
      long expiresIn = props.getJwt().getAccessTtl().toSeconds();
      return ResponseEntity.ok(new AuthResponse(access, expiresIn, refresh));
    } catch (BadCredentialsException ex) {
      throw ex;
    }
  }

  @PostMapping("/register")

  public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody RegisterRequest request) {

    if (!props.getAuth().isRegistrationEnabled()) {
      return ResponseEntity.notFound().build();
    }
    if (userRepo.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }

    UserAccount ua = new UserAccount();
    ua.setUsername(request.getUsername());
    ua.setUserlastname(request.getUserlastname());
    ua.setRut(request.getRut());
    ua.setEmail(request.getEmail());
    ua.setPassword(passwordEncoder.encode(request.getPassword()));
    ua.setEnabled(true);
    ua.setRoles(Set.of(Role.ROLE_USER));

    UserAccount savedUser = userRepo.save(ua);

    UserRegistrationResponse response = new UserRegistrationResponse(savedUser.getId(), savedUser.getEmail());

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedUser.getId())
        .toUri();

    return ResponseEntity.created(location).body(response);

  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
    if (!props.getJwt().isRefreshEnabled()) {
      return ResponseEntity.notFound().build();
    }
    var payload = jwtService.parseAndValidate(refreshToken);
    UserAccount user = userRepo.findByEmail(payload.getSubject()).orElseThrow();
    if (payload.getVersion() != user.getTokenVersion()) {
      throw new BadCredentialsException("Invalid refresh token version");
    }
    String access = jwtService.generateAccessToken(user);
    String refresh = jwtService.generateRefreshToken(user);
    long expiresIn = props.getJwt().getAccessTtl().toSeconds();
    return ResponseEntity.ok(new AuthResponse(access, expiresIn, refresh));
  }
}
