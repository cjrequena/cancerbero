package com.cjrequena.security.domain.exception;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class UserNotFoundException extends DomainRuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
