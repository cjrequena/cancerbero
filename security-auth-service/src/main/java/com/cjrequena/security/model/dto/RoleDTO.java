package com.cjrequena.security.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class RoleDTO {

  private Long roleId;

  private String roleName;

  private String description;

  private Date creationDate;

  private Date updateDate;

  private Set<UserDTO> users;

  private Set<PermissionDTO> permissions;

  // getters and setters
}
