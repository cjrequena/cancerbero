package com.cjrequena.security.service;

import com.cjrequena.security.controller.dto.RoleDTO;
import com.cjrequena.security.domain.exception.DomainException;
import com.cjrequena.security.domain.exception.RoleNotFoundException;
import com.cjrequena.security.domain.mapper.ApplicationMapper;
import com.cjrequena.security.persistence.entity.RoleEntity;
import com.cjrequena.security.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DomainException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;
    private final ApplicationMapper applicationMapper;

    public RoleDTO create(RoleDTO roleDTO) {
        RoleEntity roleEntity = this.applicationMapper.toEntity(roleDTO);
        roleRepository.save(roleEntity);
        return this.applicationMapper.toDTO(roleEntity);
    }

    public RoleDTO retrieveByRoleId(Long roleId) throws RoleNotFoundException {
        Optional<RoleEntity> optional = this.roleRepository.findById(roleId);
        if (!optional.isPresent()) {
            throw new RoleNotFoundException("Role Not Found");
        }
        return applicationMapper.toDTO(optional.get());
    }

    public RoleDTO retrieveByRoleName(String roleName) throws RoleNotFoundException {
        Optional<RoleEntity> optional = this.roleRepository.findByRoleName(roleName);
        if (!optional.isPresent()) {
            throw new RoleNotFoundException("Role Not Found");
        }
        return applicationMapper.toDTO(optional.get());
    }

    public List<RoleDTO> retrieve() {
        List<RoleEntity> entities = this.roleRepository.findAll();
        return entities
          .stream()
          .map(this.applicationMapper::toDTO)
          .collect(Collectors.toList());
    }

    public RoleDTO update(RoleDTO roleDTO) throws RoleNotFoundException {
        Optional<RoleEntity> optional = roleRepository.findById(roleDTO.getRoleId());
        if (optional.isPresent()) {
            RoleEntity entity = this.applicationMapper.toEntity(roleDTO);
            this.roleRepository.saveAndFlush(entity);
            log.debug("Updated role with id {}", entity.getRoleId());
            return this.applicationMapper.toDTO(entity);
        } else {
            throw new RoleNotFoundException("The role " + roleDTO.getRoleId() + " was not Found");
        }
    }

    //  public RoleDTO patch(Long id, JsonPatch patchDocument) {
    //    return null;
    //  }

    //  public RoleDTO patch(Long id, JsonMergePatch mergePatchDocument) {
    //    return null;
    //  }

    public void delete(Long roleId) throws RoleNotFoundException {
        Optional<RoleEntity> optional = this.roleRepository.findById(roleId);
        optional.ifPresent(
          entity -> {
              this.roleRepository.delete(entity);
              log.debug("Deleted Role: {}", entity);
          }
        );
        optional.orElseThrow(() -> new RoleNotFoundException("Role Not Found"));
    }

    // Add other service methods as needed
}
