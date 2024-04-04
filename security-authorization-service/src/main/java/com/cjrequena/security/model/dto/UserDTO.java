package com.cjrequena.security.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserDTO {

  private Long userId;

  private String userName;

  private String password;

  private String email;

  private Date creationDate;

  private Date updateDate;

  private boolean active;

  private Set<RoleDTO> roles;

  // getters and setters
}
