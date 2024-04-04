package com.cjrequena.security.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class PermissionDTO {

  private Long permissionId;

  private String permissionName;

  private String description;

  private Date creationDate;

  private Date updateDate;

  private Set<RoleDTO> roles;

  // getters and setters
}
