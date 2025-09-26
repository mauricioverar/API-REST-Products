package cl.kibernunmacademy.rest.security.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(unique = true, nullable = false, length = 100)
  private String userlastname;

  @Column(unique = true, nullable = false, length = 12)
  private String rut;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false)
  private int tokenVersion = 0;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 30)
  private Set<Role> roles = new HashSet<>();

  //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserlastname() {
    return userlastname;
  }

  public void setUserlastname(String userlastname) {
    this.userlastname = userlastname;
  }

  public String getRut() {
    return rut;
  }

  public void setRut(String rut) {
    this.rut = rut;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public int getTokenVersion() {
    return tokenVersion;
  }

  public void setTokenVersion(int tokenVersion) {
    this.tokenVersion = tokenVersion;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public void incrementTokenVersion() {
    this.tokenVersion++;
  }

}
