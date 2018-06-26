package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The driver already has a selected car...")
public class DriverAlreadyHasACarException extends Exception {
    static final long serialVersionUID = -3327512993224229948L;

    public DriverAlreadyHasACarException(String message) {
        super(message);
    }

}
