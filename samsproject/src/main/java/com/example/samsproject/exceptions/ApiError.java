package com.example.samsproject.exceptions;

import lombok.Data;

@Data
public class ApiError {
    private Integer errorcode;
    private String errorDescription;

    public ApiError(Integer errorcode, String errorDescription) {
        this.errorcode = errorcode;
        this.errorDescription = errorDescription;
    }
}
