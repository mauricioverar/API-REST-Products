package cl.kibernunmacademy.rest.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

  @NotBlank
  private String username;

  @NotBlank
  private String userlastname;

  @NotBlank
  private String rut;

  @NotBlank
  private String email;

  @NotBlank
  private String password;

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

}
