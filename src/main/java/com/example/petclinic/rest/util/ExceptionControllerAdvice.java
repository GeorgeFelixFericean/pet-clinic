package com.example.petclinic.rest.util;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@ControllerAdvice
@RequestMapping(produces = {"application/json", "text/csv"})
public class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(PetClinicException.class)
    public ResponseEntity<ApiError> processWatchlistException(final PetClinicException e) {
        return generalErrorHandler(e, e.getHttpStatus(), e.getErrorReturnCode());
    }

    private ResponseEntity<ApiError> generalErrorHandler(final Throwable t, final HttpStatus httpStatus, final ErrorReturnCode errorReturnCode) {
        final String message = Optional.ofNullable(t.getMessage()).orElse(t.getClass().getSimpleName());
        final ApiError error = new ApiError();
        error.setErrorCode(Integer.parseInt(errorReturnCode.getCode()));
        error.setErrorMessage(errorReturnCode.getMessage());
        LOG.error(message, t);

        return new ResponseEntity<>(error, httpStatus);
    }
}
