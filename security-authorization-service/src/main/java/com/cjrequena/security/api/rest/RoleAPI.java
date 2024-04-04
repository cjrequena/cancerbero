package com.cjrequena.security.api.rest;

import com.cjrequena.security.exception.service.RoleNotFoundServiceException;
import com.cjrequena.security.model.dto.RoleDTO;
import com.cjrequena.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleAPI {

    private final RoleService roleService;

    @Autowired
    public RoleAPI(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.create(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> retrieveById(@PathVariable Long roleId) {
        try {
            RoleDTO role = roleService.retrieveByRoleId(roleId);
            return ResponseEntity.ok(role);
        } catch (RoleNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-rolename/{roleName}")
    public ResponseEntity<RoleDTO> retrieveByRoleName(@PathVariable String roleName) {
        try {
            RoleDTO role = roleService.retrieveByRoleName(roleName);
            return ResponseEntity.ok(role);
        } catch (RoleNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
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
        } catch (RoleNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable Long roleId) {
        try {
            roleService.delete(roleId);
            return ResponseEntity.noContent().build();
        } catch (RoleNotFoundServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
}