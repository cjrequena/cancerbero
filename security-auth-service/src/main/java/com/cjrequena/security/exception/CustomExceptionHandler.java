package com.cjrequena.security.exception;


import com.cjrequena.security.exception.api.ApiException;
import com.cjrequena.security.exception.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.cjrequena.security.common.Constants.DATE_TIME_FORMAT;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  private static final String EXCEPTION_LOG = "Exception {}";

  @ExceptionHandler({Exception.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> unhandledExceptions(Exception ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public ResponseEntity<Object> unhandledAccessDeniedException(AccessDeniedException ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.FORBIDDEN.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
  }

  @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ResponseBody
  public ResponseEntity<Object> handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
    log.debug(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.CONFLICT.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage("Optimistic concurrency control error");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
  }

  @ExceptionHandler({ServiceException.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseEntity<Object> handleServiceException(ServiceException ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
  }

  @ExceptionHandler({ApiException.class})
  @ResponseBody
  public ResponseEntity<Object> handleApiException(ApiException ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(ex.getHttpStatus().value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus()).body(errorDTO);
  }

}
