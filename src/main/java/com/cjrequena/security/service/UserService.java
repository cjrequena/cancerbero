package com.cjrequena.security.service;

import com.cjrequena.security.controller.dto.UserDTO;
import com.cjrequena.security.domain.exception.DomainException;
import com.cjrequena.security.domain.exception.UserNotFoundException;
import com.cjrequena.security.domain.mapper.ApplicationMapper;
import com.cjrequena.security.persistence.entity.UserEntity;
import com.cjrequena.security.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DomainException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

  private final UserRepository userRepository;
  private final ApplicationMapper applicationMapper;

  public UserDTO create(UserDTO userDTO) {
    UserEntity userEntity = this.applicationMapper.toEntity(userDTO);
    userRepository.save(userEntity);
    return this.applicationMapper.toDTO(userEntity);
  }

  public UserDTO retrieveByUserId(Long userId) throws UserNotFoundException {
    Optional<UserEntity> optional = this.userRepository.findById(userId);
    if (optional.isEmpty()) {
      throw new UserNotFoundException("User Not Found");
    }
    return applicationMapper.toDTO(optional.get());
  }

  public UserDTO retrieveByUserName(String userName) throws UserNotFoundException {
    Optional<UserEntity> optional = this.userRepository.findByUserName(userName);
    if (!optional.isPresent()) {
      throw new UserNotFoundException("User Not Found");
    }
    return applicationMapper.toDTO(optional.get());
  }

  public UserDTO retrieveByEmail(String email) throws UserNotFoundException {
    Optional<UserEntity> optional = this.userRepository.findByEmail(email);
    if (!optional.isPresent()) {
      throw new UserNotFoundException("User Not Found");
    }
    return applicationMapper.toDTO(optional.get());
  }

  public List<UserDTO> retrieve() {
    List<UserEntity> entities = this.userRepository.findAll();
    return entities
      .stream()
      .map(this.applicationMapper::toDTO)
      .collect(Collectors.toList());
  }

  public UserDTO update(UserDTO userDTO) throws UserNotFoundException {
    Optional<UserEntity> optional = userRepository.findById(userDTO.getUserId());
    if (optional.isPresent()) {
      UserEntity entity = this.applicationMapper.toEntity(userDTO);
      this.userRepository.saveAndFlush(entity);
      log.debug("Updated user with id {}", entity.getUserId());
      return this.applicationMapper.toDTO(entity);
    } else {
      throw new UserNotFoundException("The user " + userDTO.getUserId() + " was not Found");
    }
  }

//  public UserDTO patch(Long id, JsonPatch patchDocument) {
//    return null;
//  }

//  public UserDTO patch(Long id, JsonMergePatch mergePatchDocument) {
//    return null;
//  }

  public void delete(Long userId) throws UserNotFoundException {
    Optional<UserEntity> optional = this.userRepository.findById(userId);
    optional.ifPresent(
      entity -> {
        this.userRepository.delete(entity);
        log.debug("Deleted User: {}", entity);
      }
    );
    optional.orElseThrow(() -> new UserNotFoundException("User Not Found"));
  }

  // Add other service methods as needed
}
