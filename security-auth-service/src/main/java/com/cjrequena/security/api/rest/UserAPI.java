package com.cjrequena.security.api.rest;

import com.cjrequena.security.exception.service.UserNotFoundServiceException;
import com.cjrequena.security.model.dto.UserDTO;
import com.cjrequena.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cjrequena.security.api.rest.UserAPI.ACCEPT_VERSION;
import static com.cjrequena.security.common.Constants.HEADER_VND_SECURITY_AUTH_SERVICE_V1;

@Slf4j
@RestController
@RequestMapping(value = UserAPI.ENDPOINT, headers = {ACCEPT_VERSION})
public class UserAPI {

    public static final String ENDPOINT = "/api/users";
    public static final String ACCEPT_VERSION = "Accept-Version=" + HEADER_VND_SECURITY_AUTH_SERVICE_V1;

    private final UserService userService;

    @Autowired
    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> retrieveByUserId(@PathVariable Long userId) {
        try {
            UserDTO user = userService.retrieveByUserId(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-username/{userName}")
    public ResponseEntity<UserDTO> retrieveByUserName(@PathVariable String userName) {
        try {
            UserDTO user = userService.retrieveByUserName(userName);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserDTO> retrieveByEmail(@PathVariable String email) {
        try {
            UserDTO user = userService.retrieveByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> retrieve() {
        List<UserDTO> users = userService.retrieve();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> update(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        userDTO.setUserId(userId);
        try {
            UserDTO updatedUser = userService.update(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
