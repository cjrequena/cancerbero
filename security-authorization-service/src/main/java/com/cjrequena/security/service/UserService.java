package com.cjrequena.security.service;

import com.cjrequena.security.model.entity.UserEntity;
import com.cjrequena.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
//@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

  private final UserRepository userRepository;

  public UserEntity findByUserName(String userName) {
    return userRepository.findByUserName(userName);
  }

  public Optional<UserEntity> findById(Long userId) {
    return userRepository.findById(userId);
  }

  public UserEntity save(UserEntity user) {
    return userRepository.save(user);
  }

  public void delete(UserEntity user) {
    userRepository.delete(user);
  }

  // Add other service methods as needed
}
