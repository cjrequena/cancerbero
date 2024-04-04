package com.cjrequena.security.service;

import com.cjrequena.security.exception.service.ServiceException;
import com.cjrequena.security.model.entity.PermissionEntity;
import com.cjrequena.security.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionService {

  private final PermissionRepository permissionRepository;

  public PermissionEntity findByPermissionName(String permissionName) {
    return permissionRepository.findByPermissionName(permissionName);
  }

  public Optional<PermissionEntity> findById(Long permissionId) {
    return permissionRepository.findById(permissionId);
  }

  public PermissionEntity save(PermissionEntity permission) {
    return permissionRepository.save(permission);
  }

  public void delete(PermissionEntity permission) {
    permissionRepository.delete(permission);
  }

  // Add other service methods as needed
}
