package com.cjrequena.security.model.mapper;

import com.cjrequena.security.model.dto.PermissionDTO;
import com.cjrequena.security.model.dto.RoleDTO;
import com.cjrequena.security.model.dto.UserDTO;
import com.cjrequena.security.model.entity.PermissionEntity;
import com.cjrequena.security.model.entity.RoleEntity;
import com.cjrequena.security.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@Mapper(
  componentModel = "spring",
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ApplicationMapper {

  UserEntity toEntity(UserDTO dto);

  UserDTO toDTO(UserEntity entity);

  RoleEntity toEntity(RoleDTO dto);

  RoleDTO toDTO(RoleEntity entity);

  PermissionEntity toEntity(PermissionDTO dto);

  PermissionDTO toDTO(PermissionEntity entity);

}
