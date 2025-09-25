package cl.kibernunmacademy.rest.service;

import java.util.List;
import java.util.Optional;

import cl.kibernunmacademy.rest.dto.ProductDTO;
import cl.kibernunmacademy.rest.model.Product;

public interface IProductService {

  public List<Product> findAll();

  public Optional<Product> findById(Long id);

  public Product create(ProductDTO productDTO);

  public Product update(Long id, ProductDTO book);

  public void delete(Long id);

}
