package com.cjrequena.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "T_USER")
@Setter
@Getter
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "user_name", unique = true)
  private String userName;

  @Column(name = "password")
  private String password;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "creation_date")
  private Date creationDate;

  @Column(name = "update_date")
  private Date updateDate;

  @Column(name = "active")
  private boolean active;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "T_USER_ROLE",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<RoleEntity> roles;

  // getters and setters
}
