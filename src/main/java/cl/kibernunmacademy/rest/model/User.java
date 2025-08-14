package cl.kibernunmacademy.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is mandatory")
  @Size(max = 50)
  private String name;

  @NotBlank(message = "Apellido is mandatory")
  @Size(max = 50)
  private String apellido;

  @NotBlank(message = "Rut is mandatory")
  @Size(max = 12)
  private String rut;

  @NotBlank(message = "Email is mandatory")
  @Size(max = 100)
  @Column(unique = true)
  private String email;

  @NotBlank(message = "Password is mandatory")
  @Size(min = 8)
  @Size(max = 64)
  private String password;

  private boolean active = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}