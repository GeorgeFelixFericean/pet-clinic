package com.example.petclinic.exception;

import com.example.petclinic.rest.util.ErrorReturnCode;
import org.springframework.http.HttpStatus;

public class PetClinicException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorReturnCode errorReturnCode;

    public PetClinicException(HttpStatus httpStatus, ErrorReturnCode errorReturnCode) {
        this.httpStatus = httpStatus;
        this.errorReturnCode = errorReturnCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorReturnCode getErrorReturnCode() {
        return errorReturnCode;
    }
}
