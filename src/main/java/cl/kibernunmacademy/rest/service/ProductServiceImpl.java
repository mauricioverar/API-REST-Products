package cl.kibernunmacademy.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.kibernunmacademy.rest.dto.ProductDTO;
import cl.kibernunmacademy.rest.exception.ResourceNotFoundException;
import cl.kibernunmacademy.rest.model.Product;
import cl.kibernunmacademy.rest.repository.IProductRepository;

@Service
public class ProductServiceImpl implements IProductService {

  private final IProductRepository productRepository;

  public ProductServiceImpl(IProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> findAll() {
    return this.productRepository.findAll();
  }

  @Override
  public Optional<Product> findById(Long id) {
    return this.productRepository.findById(id);
  }

  @Override
  public Product create(ProductDTO productDTO) {

    Product product = new Product();
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setImage(productDTO.getImage());
    product.setStock(productDTO.getStock());
    product.setPrice(productDTO.getPrice());
    product.setActive(productDTO.isActive());
    return this.productRepository.save(product);
  }

  @Override
  public Product update(Long id, ProductDTO product) {
    Product existing = this.productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

    existing.setName(product.getName());
    existing.setDescription(product.getDescription());
    existing.setImage(product.getImage());
    existing.setStock(product.getStock());
    existing.setPrice(product.getPrice());
    existing.setActive(product.isActive());
    return this.productRepository.save(existing);
  }

  @Override
  public void delete(Long id) {
    Product existing = this.productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    this.productRepository.delete(existing);
  }

}
