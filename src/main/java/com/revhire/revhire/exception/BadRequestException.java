//This layer centralizes exception handling and ensures structured error responses

package com.revhire.revhire.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
