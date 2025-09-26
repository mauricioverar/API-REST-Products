package cl.kibernunmacademy.rest.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.kibernunmacademy.rest.security.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

  Optional<UserAccount> findByEmail(String email);

  boolean existsByEmail(String email);

  void deleteByEmail(String email);

}
