package com.cjrequena.security.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "T_ROLE")
@Setter
@Getter
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "role_name", unique = true)
  private String roleName;

  @Column(name = "description")
  private String description;

  @Column(name = "creation_date")
  private Date creationDate;

  @Column(name = "update_date")
  private Date updateDate;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<UserEntity> users;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "T_ROLE_PERMISSION",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<PermissionEntity> permissions;

  // getters and setters
}
