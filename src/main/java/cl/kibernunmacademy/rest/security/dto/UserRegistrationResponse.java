package cl.kibernunmacademy.rest.security.dto;

public class UserRegistrationResponse {
  private Long id;
  private String email;

  public UserRegistrationResponse(Long id, String email) {
    this.id = id;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
}
