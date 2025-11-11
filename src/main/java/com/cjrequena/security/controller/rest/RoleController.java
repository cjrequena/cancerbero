package com.cjrequena.security.controller.rest;

import com.cjrequena.security.controller.dto.RoleDTO;
import com.cjrequena.security.controller.exception.NotFoundException;
import com.cjrequena.security.domain.exception.RoleNotFoundException;
import com.cjrequena.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cjrequena.security.controller.rest.RoleController.ACCEPT_VERSION;
import static com.cjrequena.security.controller.rest.RoleController.ENDPOINT;
import static com.cjrequena.security.shared.common.Constant.HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

@RestController
@RequestMapping(value = ENDPOINT, headers = {ACCEPT_VERSION})

public class RoleController {

  public static final String ENDPOINT = "/api/roles";
  public static final String ACCEPT_VERSION = "Accept-Version=" + HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

  private final RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping
  public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
    RoleDTO createdRole = roleService.create(roleDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
  }

  @GetMapping("/{roleId}")
  public ResponseEntity<RoleDTO> retrieveByRoleId(@PathVariable Long roleId) {
    try {
      RoleDTO role = roleService.retrieveByRoleId(roleId);
      return ResponseEntity.ok(role);
    } catch (RoleNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @GetMapping("/by-rolename/{roleName}")
  public ResponseEntity<RoleDTO> retrieveByRoleName(@PathVariable String roleName) {
    try {
      RoleDTO role = roleService.retrieveByRoleName(roleName);
      return ResponseEntity.ok(role);
    } catch (RoleNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @GetMapping
  public ResponseEntity<List<RoleDTO>> retrieve() {
    List<RoleDTO> roles = roleService.retrieve();
    return ResponseEntity.ok(roles);
  }

  @PutMapping("/{roleId}")
  public ResponseEntity<RoleDTO> update(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
    roleDTO.setRoleId(roleId);
    try {
      RoleDTO updatedRole = roleService.update(roleDTO);
      return ResponseEntity.ok(updatedRole);
    } catch (RoleNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }

  @DeleteMapping("/{roleId}")
  public ResponseEntity<Void> delete(@PathVariable Long roleId) {
    try {
      roleService.delete(roleId);
      return ResponseEntity.noContent().build();
    } catch (RoleNotFoundException ex) {
      throw new NotFoundException(ex.getMessage(), ex);
    }
  }
}
