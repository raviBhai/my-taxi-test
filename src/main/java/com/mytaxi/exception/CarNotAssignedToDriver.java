package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "This car is not assigned to this driver...")
public class CarNotAssignedToDriver extends Exception {

    static final long serialVersionUID = -3387512993224229967L;

    public CarNotAssignedToDriver(String message) {
        super(message);
    }
}
