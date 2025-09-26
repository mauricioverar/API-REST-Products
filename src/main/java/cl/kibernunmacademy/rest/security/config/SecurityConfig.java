package cl.kibernunmacademy.rest.security.config;

import java.time.Duration;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import cl.kibernunmacademy.rest.security.exception.SecurityExceptionHandler;
import cl.kibernunmacademy.rest.security.filter.JwtAuthenticationFilter;
import cl.kibernunmacademy.rest.security.jwt.JwtProperties;
import cl.kibernunmacademy.rest.security.repository.UserAccountRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;
  private final JwtProperties props;
  private final SecurityExceptionHandler securityExceptionHandler;

  public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtProperties props,
      SecurityExceptionHandler securityExceptionHandler) {
    this.jwtFilter = jwtFilter;
    this.props = props;
    this.securityExceptionHandler = securityExceptionHandler;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    List<String> origins = props.getCors().getAllowedOrigins();
    if (origins != null && !origins.isEmpty()) {
      config.setAllowedOrigins(origins);
    } else {
      config.addAllowedOriginPattern("*");
    }
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.setAllowCredentials(false);
    config.setMaxAge(Duration.ofHours(1));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(securityExceptionHandler)
            .accessDeniedHandler(securityExceptionHandler))
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/auth/**", "/h2-console/**",
                "/api/v1/products/**")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountRepository repo) {
    return email -> repo.findByEmail(email)
        .map(ua -> User.withUsername(ua.getEmail())
            .password(ua.getPassword())
            .authorities(ua.getRoles().stream().map(Enum::name).toArray(String[]::new))
            .disabled(!ua.isEnabled())
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  
}
