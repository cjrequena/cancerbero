package com.cjrequena.security.configuration.security;

import com.cjrequena.security.exception.service.UserNotFoundServiceException;
import com.cjrequena.security.model.dto.PermissionDTO;
import com.cjrequena.security.model.dto.RoleDTO;
import com.cjrequena.security.model.dto.UserDTO;
import com.cjrequena.security.model.entity.UserEntity;
import com.cjrequena.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessTokenPrincipalUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public AccessTokenPrincipalUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Optional<UserEntity> userEntityOptional = null;
    try {
      UserDTO userDTO = userService.retrieveByUserName(userName);
      return AccessTokenPrincipalUserDetails.builder()
        .userId(userDTO.getUserId())
        .userName(userDTO.getUserName())
        .email(userDTO.getEmail())
        .password(userDTO.getPassword())
        .roles(getRoles(userDTO))
        .authorities(getAuthorities(userDTO))
        .build();
    } catch (UserNotFoundServiceException e) {
      throw new UsernameNotFoundException(userName);
    }
  }

  private Set<String> getRoles(UserDTO userDTO) {
    return userDTO
      .getRoles()
      .stream()
      .map(RoleDTO::getRoleName)
      .collect(Collectors.toSet());
  }

  private Set<SimpleGrantedAuthority> getAuthorities(UserDTO userDTO) {
    return userDTO
      .getRoles()
      .stream()
      .flatMap(role -> role.getPermissions().stream()).map(PermissionDTO::getPermissionName)
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());
  }
}
