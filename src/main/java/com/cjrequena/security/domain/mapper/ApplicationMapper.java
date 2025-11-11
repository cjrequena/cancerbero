package com.cjrequena.security.domain.mapper;

import com.cjrequena.security.controller.dto.PermissionDTO;
import com.cjrequena.security.controller.dto.RoleDTO;
import com.cjrequena.security.controller.dto.UserDTO;
import com.cjrequena.security.persistence.entity.PermissionEntity;
import com.cjrequena.security.persistence.entity.RoleEntity;
import com.cjrequena.security.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@Mapper(componentModel = "spring")
public interface ApplicationMapper {

  @Mapping(source = "roles", target = "roles")
  UserEntity toEntity(UserDTO dto);

  @Mapping(source = "roles", target = "roles")
  UserDTO toDTO(UserEntity entity);

  @Mapping(source = "users", target = "users")
  RoleEntity toEntity(RoleDTO dto);

  @Mapping(source = "users", target = "users", ignore = true)
  @Mapping(source = "permissions", target = "permissions")
  RoleDTO toDTO(RoleEntity entity);

  @Mapping(source = "roles", target = "roles", ignore = true)
  PermissionEntity toEntity(PermissionDTO dto);

  @Mapping(source = "roles", target = "roles", ignore = true)
  PermissionDTO toDTO(PermissionEntity entity);

}
