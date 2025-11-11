package com.cjrequena.security.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Data
@JsonPropertyOrder(value = {
  "user_id",
  "user_name",
  "password",
  "email",
  "active",
  "roles",
  "creation_date",
  "update_date"
})
@JsonTypeName("user")
//@Schema(name = "User", description = "User")
public class UserDTO {

  @JsonProperty(value = "user_id")
  @Getter(onMethod = @__({@JsonProperty("user_id")}))
  //@Schema(name = "user_id", accessMode = Schema.AccessMode.READ_ONLY)
  private Long userId;

  @NotNull(message = "user_name is a required field")
  @JsonProperty(value = "user_name", required = true)
  @Getter(onMethod = @__({@JsonProperty("user_name")}))
  //@Schema(name = "user_name", requiredMode = REQUIRED)
  private String userName;

  @NotNull(message = "password is a required field")
  @JsonProperty(value = "password", required = true)
  @Getter(onMethod = @__({@JsonProperty("password")}))
  //@Schema(name = "password", requiredMode = REQUIRED)
  private String password;

  @NotNull(message = "email is a required field")
  @JsonProperty(value = "email", required = true)
  @Getter(onMethod = @__({@JsonProperty("email")}))
  //@Schema(name = "email", requiredMode = REQUIRED)
  private String email;

  @NotNull(message = "active is a required field")
  @JsonProperty(value = "active", required = true)
  @Getter(onMethod = @__({@JsonProperty("active")}))
  private boolean active;

  @JsonProperty(value = "roles")
  @Getter(onMethod = @__({@JsonProperty("roles")}))
  private Set<RoleDTO> roles;

  @JsonProperty(value = "creation_date")
  @Getter(onMethod = @__({@JsonProperty("creation_date")}))
  private Date creationDate;

  @JsonProperty(value = "update_date")
  @Getter(onMethod = @__({@JsonProperty("update_date")}))
  private Date updateDate;

  // getters and setters
}
