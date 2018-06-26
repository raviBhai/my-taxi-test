package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The car is already assigned to another driver...")
public class CarAlreadyInUseException extends Exception {

    static final long serialVersionUID = -3387512993224229948L;

    public CarAlreadyInUseException(String message) {
        super(message);
    }

}
