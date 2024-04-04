package com.cjrequena.security.repository;

import com.cjrequena.security.model.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    PermissionEntity findByPermissionName(String permissionName);
    // Add any custom query methods if needed
}
