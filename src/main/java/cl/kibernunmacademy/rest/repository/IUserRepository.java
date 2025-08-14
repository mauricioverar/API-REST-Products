package cl.kibernunmacademy.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.kibernunmacademy.rest.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {

}
