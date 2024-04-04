package com.cjrequena.security.service;

import com.cjrequena.security.exception.service.ServiceException;
import com.cjrequena.security.exception.service.UserNotFoundServiceException;
import com.cjrequena.security.model.dto.UserDTO;
import com.cjrequena.security.model.entity.UserEntity;
import com.cjrequena.security.model.mapper.Mapper;
import com.cjrequena.security.repository.UserRepository;
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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

  private final UserRepository userRepository;
  private final Mapper mapper;

  public UserDTO create(UserDTO userDTO) {
    UserEntity userEntity = this.mapper.toEntity(userDTO);
    userRepository.save(userEntity);
    return this.mapper.toDTO(userEntity);
  }

  public UserDTO retrieveById(Long userId) throws UserNotFoundServiceException {
    Optional<UserEntity> optional = this.userRepository.findById(userId);
    if (!optional.isPresent()) {
      throw new UserNotFoundServiceException("User Not Found");
    }
    return mapper.toDTO(optional.get());
  }

  public UserDTO retrieveByUserName(String userName) throws UserNotFoundServiceException {
    Optional<UserEntity> optional = this.userRepository.findByUserName(userName);
    if (!optional.isPresent()) {
      throw new UserNotFoundServiceException("User Not Found");
    }
    return mapper.toDTO(optional.get());
  }

  public UserDTO retrieveByEmail(String email) throws UserNotFoundServiceException {
    Optional<UserEntity> optional = this.userRepository.findByEmail(email);
    if (!optional.isPresent()) {
      throw new UserNotFoundServiceException("User Not Found");
    }
    return mapper.toDTO(optional.get());
  }

  public List<UserDTO> retrieve() {
    List<UserEntity> entities = this.userRepository.findAll();
    return entities
      .stream()
      .map(this.mapper::toDTO)
      .collect(Collectors.toList());
  }

  public UserDTO update(UserDTO userDTO) throws UserNotFoundServiceException {
    Optional<UserEntity> optional = userRepository.findById(userDTO.getUserId());
    if (optional.isPresent()) {
      UserEntity entity = this.mapper.toEntity(userDTO);
      this.userRepository.saveAndFlush(entity);
      log.debug("Updated user with id {}", entity.getUserId());
      return this.mapper.toDTO(entity);
    } else {
      throw new UserNotFoundServiceException("The user " + userDTO.getUserId() + " was not Found");
    }
  }

//  public UserDTO patch(Long id, JsonPatch patchDocument) {
//    return null;
//  }

//  public UserDTO patch(Long id, JsonMergePatch mergePatchDocument) {
//    return null;
//  }

  public void delete(Long userId) throws UserNotFoundServiceException {
    Optional<UserEntity> optional = this.userRepository.findById(userId);
    optional.ifPresent(
      entity -> {
        this.userRepository.delete(entity);
        log.debug("Deleted User: {}", entity);
      }
    );
    optional.orElseThrow(() -> new UserNotFoundServiceException("User Not Found"));
  }

  // Add other service methods as needed
}
