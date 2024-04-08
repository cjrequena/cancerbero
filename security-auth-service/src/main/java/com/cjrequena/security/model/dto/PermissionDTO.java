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
  "permission_id",
  "permission_name",
  "description",
  "roles",
  "creation_date",
  "update_date"
})
public class PermissionDTO {

  @JsonProperty(value = "permission_id")
  @Getter(onMethod = @__({@JsonProperty("permission_id")}))
  //@Schema(name = "permission_id", accessMode = Schema.AccessMode.READ_ONLY)
  private Long permissionId;

  @NotNull(message = "permission_name is a required field")
  @JsonProperty(value = "permission_name", required = true)
  @Getter(onMethod = @__({@JsonProperty("permission_name")}))
  //@Schema(name = "permission_name", requiredMode = REQUIRED)
  private String permissionName;

  @NotNull(message = "description is a required field")
  @JsonProperty(value = "description", required = true)
  @Getter(onMethod = @__({@JsonProperty("description")}))
  //@Schema(name = "description", requiredMode = REQUIRED)
  private String description;

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
