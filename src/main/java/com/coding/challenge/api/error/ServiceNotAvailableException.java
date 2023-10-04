package com.coding.challenge.api.error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = -4233183364769155695L;

    public ServiceNotAvailableException(String reason) {
        super(reason);
    }
}
