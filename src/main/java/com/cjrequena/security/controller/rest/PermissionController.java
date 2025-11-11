package com.cjrequena.security.controller.rest;

import com.cjrequena.security.controller.dto.PermissionDTO;
import com.cjrequena.security.controller.exception.NotFoundException;
import com.cjrequena.security.domain.exception.PermissionNotFoundException;
import com.cjrequena.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cjrequena.security.controller.rest.PermissionController.ACCEPT_VERSION;
import static com.cjrequena.security.shared.common.Constant.HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

@RestController
@RequestMapping(value = PermissionController.ENDPOINT, headers = {ACCEPT_VERSION})
public class PermissionController {

  public static final String ENDPOINT = "/api/permissions";
  public static final String ACCEPT_VERSION = "Accept-Version=" + HEADER_VND_SECURITY_AUTHORIZATION_SERVICE_V1;

  private final PermissionService permissionService;

  @Autowired
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @PostMapping
  public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
    PermissionDTO createdPermission = permissionService.create(permissionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
  }

  @GetMapping("/{permissionId}")
  public ResponseEntity<PermissionDTO> retrieveByPermissionId(@PathVariable Long permissionId) {
    try {
      PermissionDTO permission = permissionService.retrieveByPermissionId(permissionId);
      return ResponseEntity.ok(permission);
    } catch (PermissionNotFoundException ex) {
      throw  new NotFoundException(ex.getMessage(), ex);
    }
  }

  @GetMapping("/by-permissionname/{permissionName}")
  public ResponseEntity<PermissionDTO> retrieveByPermissionName(@PathVariable String permissionName) {
    try {
      PermissionDTO permission = permissionService.retrieveByPermissionName(permissionName);
      return ResponseEntity.ok(permission);
    } catch (PermissionNotFoundException ex) {
      throw  new NotFoundException(ex.getMessage(), ex);
    }
  }

  @PreAuthorize("hasAuthority('read_permission')")
  @GetMapping
  public ResponseEntity<List<PermissionDTO>> retrieve() {
    List<PermissionDTO> permissions = permissionService.retrieve();
    return ResponseEntity.ok(permissions);
  }

  @PutMapping("/{permissionId}")
  public ResponseEntity<PermissionDTO> update(@PathVariable Long permissionId, @RequestBody PermissionDTO permissionDTO) {
    permissionDTO.setPermissionId(permissionId);
    try {
      PermissionDTO updatedPermission = permissionService.update(permissionDTO);
      return ResponseEntity.ok(updatedPermission);
    } catch (PermissionNotFoundException ex) {
      throw  new NotFoundException(ex.getMessage(), ex);
    }
  }

  @DeleteMapping("/{permissionId}")
  public ResponseEntity<Void> delete(@PathVariable Long permissionId) {
    try {
      permissionService.delete(permissionId);
      return ResponseEntity.noContent().build();
    } catch (PermissionNotFoundException ex) {
      throw  new NotFoundException(ex.getMessage(), ex);
    }
  }
}
