package com.cjrequena.security.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "T_PERMISSION")
@Setter
@Getter
public class PermissionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "permission_id")
  private Long permissionId;

  @Column(name = "permission_name", unique = true)
  private String permissionName;

  @Column(name = "description")
  private String description;

  @Column(name = "creation_date")
  private Date creationDate;

  @Column(name = "update_date")
  private Date updateDate;

  @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
  private Set<RoleEntity> roles;

  // getters and setters
}
