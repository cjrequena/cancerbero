package com.cjrequena.security.service;

import com.cjrequena.security.exception.service.PermissionNotFoundServiceException;
import com.cjrequena.security.exception.service.ServiceException;
import com.cjrequena.security.model.dto.PermissionDTO;
import com.cjrequena.security.model.entity.PermissionEntity;
import com.cjrequena.security.model.mapper.ApplicationMapper;
import com.cjrequena.security.repository.PermissionRepository;
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
public class PermissionService {

  private final PermissionRepository permissionRepository;
  private final ApplicationMapper applicationMapper;

  public PermissionDTO create(PermissionDTO permissionDTO) {
    PermissionEntity permissionEntity = this.applicationMapper.toEntity(permissionDTO);
    permissionRepository.save(permissionEntity);
    return this.applicationMapper.toDTO(permissionEntity);
  }

  public PermissionDTO retrieveByPermissionId(Long permissionId) throws PermissionNotFoundServiceException {
    Optional<PermissionEntity> optional = this.permissionRepository.findById(permissionId);
    if (!optional.isPresent()) {
      throw new PermissionNotFoundServiceException("Permission Not Found");
    }
    return applicationMapper.toDTO(optional.get());
  }

  public PermissionDTO retrieveByPermissionName(String permissionName) throws PermissionNotFoundServiceException {
    Optional<PermissionEntity> optional = this.permissionRepository.findByPermissionName(permissionName);
    if (!optional.isPresent()) {
      throw new PermissionNotFoundServiceException("Permission Not Found");
    }
    return applicationMapper.toDTO(optional.get());
  }

  public List<PermissionDTO> retrieve() {
    List<PermissionEntity> entities = this.permissionRepository.findAll();
    return entities
      .stream()
      .map(this.applicationMapper::toDTO)
      .collect(Collectors.toList());
  }

  public PermissionDTO update(PermissionDTO permissionDTO) throws PermissionNotFoundServiceException {
    Optional<PermissionEntity> optional = permissionRepository.findById(permissionDTO.getPermissionId());
    if (optional.isPresent()) {
      PermissionEntity entity = this.applicationMapper.toEntity(permissionDTO);
      this.permissionRepository.saveAndFlush(entity);
      log.debug("Updated permission with id {}", entity.getPermissionId());
      return this.applicationMapper.toDTO(entity);
    } else {
      throw new PermissionNotFoundServiceException("The permission " + permissionDTO.getPermissionId() + " was not Found");
    }
  }

  //  public PermissionDTO patch(Long id, JsonPatch patchDocument) {
  //    return null;
  //  }

  //  public PermissionDTO patch(Long id, JsonMergePatch mergePatchDocument) {
  //    return null;
  //  }

  public void delete(Long permissionId) throws PermissionNotFoundServiceException {
    Optional<PermissionEntity> optional = this.permissionRepository.findById(permissionId);
    optional.ifPresent(
      entity -> {
        this.permissionRepository.delete(entity);
        log.debug("Deleted Permission: {}", entity);
      }
    );
    optional.orElseThrow(() -> new PermissionNotFoundServiceException("Permission Not Found"));
  }

  // Add other service methods as needed
}
