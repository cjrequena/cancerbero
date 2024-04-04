package com.cjrequena.security.api.rest;

import com.cjrequena.security.exception.service.PermissionNotFoundServiceException;
import com.cjrequena.security.model.dto.PermissionDTO;
import com.cjrequena.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionAPI {

    private final PermissionService permissionService;

    @Autowired
    public PermissionAPI(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        PermissionDTO createdPermission = permissionService.create(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionDTO> retrieveById(@PathVariable Long permissionId) {
        try {
            PermissionDTO permission = permissionService.retrieveByPermissionId(permissionId);
            return ResponseEntity.ok(permission);
        } catch (PermissionNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-permissionname/{permissionName}")
    public ResponseEntity<PermissionDTO> retrieveByPermissionName(@PathVariable String permissionName) {
        try {
            PermissionDTO permission = permissionService.retrieveByPermissionName(permissionName);
            return ResponseEntity.ok(permission);
        } catch (PermissionNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
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
        } catch (PermissionNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> delete(@PathVariable Long permissionId) {
        try {
            permissionService.delete(permissionId);
            return ResponseEntity.noContent().build();
        } catch (PermissionNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
