package com.cjrequena.security.repository;

import com.cjrequena.security.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUserName(String userName);

  Optional<UserEntity> findByEmail(String email);
  // Add any custom query methods if needed
}
