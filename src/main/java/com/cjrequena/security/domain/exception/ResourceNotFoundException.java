package com.cjrequena.security.domain.exception;

public class ResourceNotFoundException extends DomainRuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
