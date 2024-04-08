package com.cjrequena.security.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Data
@JsonPropertyOrder(value = {
  "role_id",
  "role_name",
  "description",
  "permissions",
  "users",
  "creation_date",
  "update_date"
})
public class RoleDTO {

  @JsonProperty(value = "role_id")
  @Getter(onMethod = @__({@JsonProperty("role_id")}))
  //@Schema(name = "role_id", accessMode = Schema.AccessMode.READ_ONLY)
  private Long roleId;

  @NotNull(message = "role_name is a required field")
  @JsonProperty(value = "role_name", required = true)
  @Getter(onMethod = @__({@JsonProperty("role_name")}))
  //@Schema(name = "role_name", requiredMode = REQUIRED)
  private String roleName;

  @NotNull(message = "description is a required field")
  @JsonProperty(value = "description", required = true)
  @Getter(onMethod = @__({@JsonProperty("description")}))
  //@Schema(name = "description", requiredMode = REQUIRED)
  private String description;

  @JsonProperty(value = "permissions")
  @Getter(onMethod = @__({@JsonProperty("permissions")}))
  private Set<PermissionDTO> permissions;

  @JsonProperty(value = "users")
  @Getter(onMethod = @__({@JsonProperty("users")}))
  private Set<UserDTO> users;

  @JsonProperty(value = "creation_date")
  @Getter(onMethod = @__({@JsonProperty("creation_date")}))
  private Date creationDate;

  @JsonProperty(value = "update_date")
  @Getter(onMethod = @__({@JsonProperty("update_date")}))
  private Date updateDate;



  // getters and setters
}
