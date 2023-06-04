package com.crewwork.structure.security.jwtauth;

public class JwtRuntimeException extends RuntimeException {

    public JwtRuntimeException() {
        super();
    }

    public JwtRuntimeException(String message) {
        super(message);
    }
}
