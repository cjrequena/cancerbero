package com.cjrequena.security.service;

import com.cjrequena.security.exception.service.RoleNotFoundServiceException;
import com.cjrequena.security.exception.service.ServiceException;
import com.cjrequena.security.model.dto.RoleDTO;
import com.cjrequena.security.model.entity.RoleEntity;
import com.cjrequena.security.model.mapper.Mapper;
import com.cjrequena.security.repository.RoleRepository;
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
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;
    private final Mapper mapper;

    public RoleDTO create(RoleDTO roleDTO) {
        RoleEntity roleEntity = this.mapper.toEntity(roleDTO);
        roleRepository.save(roleEntity);
        return this.mapper.toDTO(roleEntity);
    }

    public RoleDTO retrieveByRoleId(Long roleId) throws RoleNotFoundServiceException {
        Optional<RoleEntity> optional = this.roleRepository.findById(roleId);
        if (!optional.isPresent()) {
            throw new RoleNotFoundServiceException("Role Not Found");
        }
        return mapper.toDTO(optional.get());
    }

    public RoleDTO retrieveByRoleName(String roleName) throws RoleNotFoundServiceException {
        Optional<RoleEntity> optional = this.roleRepository.findByRoleName(roleName);
        if (!optional.isPresent()) {
            throw new RoleNotFoundServiceException("Role Not Found");
        }
        return mapper.toDTO(optional.get());
    }

    public List<RoleDTO> retrieve() {
        List<RoleEntity> entities = this.roleRepository.findAll();
        return entities
          .stream()
          .map(this.mapper::toDTO)
          .collect(Collectors.toList());
    }

    public RoleDTO update(RoleDTO roleDTO) throws RoleNotFoundServiceException {
        Optional<RoleEntity> optional = roleRepository.findById(roleDTO.getRoleId());
        if (optional.isPresent()) {
            RoleEntity entity = this.mapper.toEntity(roleDTO);
            this.roleRepository.saveAndFlush(entity);
            log.debug("Updated role with id {}", entity.getRoleId());
            return this.mapper.toDTO(entity);
        } else {
            throw new RoleNotFoundServiceException("The role " + roleDTO.getRoleId() + " was not Found");
        }
    }

    //  public RoleDTO patch(Long id, JsonPatch patchDocument) {
    //    return null;
    //  }

    //  public RoleDTO patch(Long id, JsonMergePatch mergePatchDocument) {
    //    return null;
    //  }

    public void delete(Long roleId) throws RoleNotFoundServiceException {
        Optional<RoleEntity> optional = this.roleRepository.findById(roleId);
        optional.ifPresent(
          entity -> {
              this.roleRepository.delete(entity);
              log.debug("Deleted Role: {}", entity);
          }
        );
        optional.orElseThrow(() -> new RoleNotFoundServiceException("Role Not Found"));
    }

    // Add other service methods as needed
}
