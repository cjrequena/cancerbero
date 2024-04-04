package com.cjrequena.security.service;

import com.cjrequena.security.model.entity.RoleEntity;
import com.cjrequena.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
//@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public Optional<RoleEntity> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public RoleEntity save(RoleEntity role) {
        return roleRepository.save(role);
    }

    public void delete(RoleEntity role) {
        roleRepository.delete(role);
    }

    // Add other service methods as needed
}
