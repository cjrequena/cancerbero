package com.cjrequena.security.controller.rest;

import com.cjrequena.security.controller.dto.UserDTO;
import com.cjrequena.security.controller.exception.NotFoundException;
import com.cjrequena.security.domain.exception.UserNotFoundException;
import com.cjrequena.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cjrequena.security.controller.rest.UserController.ACCEPT_VERSION;
import static com.cjrequena.security.shared.common.Constant.HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

@Slf4j
@RestController
@RequestMapping(value = UserController.ENDPOINT, headers = {ACCEPT_VERSION})
//@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class UserController {

  public static final String ENDPOINT = "/api/users";
  public static final String ACCEPT_VERSION = "Accept-Version=" + HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasAuthority('create_user')")
  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    UserDTO createdUser = userService.create(userDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PreAuthorize("hasAuthority('read_user')")
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> retrieveByUserId(@PathVariable Long userId) {
    try {
      UserDTO user = userService.retrieveByUserId(userId);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @PreAuthorize("hasAuthority('read_user')")
  @GetMapping("/by-username/{userName}")
  public ResponseEntity<UserDTO> retrieveByUserName(@PathVariable String userName) {
    try {
      UserDTO user = userService.retrieveByUserName(userName);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @PreAuthorize("hasAuthority('read_user')")
  @GetMapping("/by-email/{email}")
  public ResponseEntity<UserDTO> retrieveByEmail(@PathVariable String email) {
    try {
      UserDTO user = userService.retrieveByEmail(email);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @PreAuthorize("hasAuthority('read_user')")
  @GetMapping
  public ResponseEntity<List<UserDTO>> retrieve() {
    List<UserDTO> users = userService.retrieve();
    return ResponseEntity.ok(users);
  }

  @PreAuthorize("hasAuthority('update_user')")
  @PutMapping("/{userId}")
  public ResponseEntity<UserDTO> update(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
    userDTO.setUserId(userId);
    try {
      UserDTO updatedUser = userService.update(userDTO);
      return ResponseEntity.ok(updatedUser);
    } catch (UserNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(@PathVariable Long userId) {
    try {
      userService.delete(userId);
      return ResponseEntity.noContent().build();
    } catch (UserNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }
}
