package cl.kibernunmacademy.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductDTO {

  @NotBlank(message = "Name is mandatory")
  @Size(max = 100)
  private String name;

  @NotBlank(message = "Description is mandatory")
  @Size(max = 500)
  private String description;

  @Size(max = 255)
  private String image;

  @Min(value = 0, message = "The stock cannot be negative")
  private int stock;

  @DecimalMin(value = "0.0", inclusive = false, message = "The price must be greater than 0")
  private double price;

  private boolean active = true;

  //

  public ProductDTO() {
  }

  public ProductDTO(String name, String description, String image, int stock, double price,
      boolean active) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.stock = stock;
    this.price = price;
    this.active = active;
  }

  //
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
