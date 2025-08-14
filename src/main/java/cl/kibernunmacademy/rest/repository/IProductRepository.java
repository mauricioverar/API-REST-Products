package cl.kibernunmacademy.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.kibernunmacademy.rest.model.Product;

public interface IProductRepository extends JpaRepository<Product, Long> {

}
