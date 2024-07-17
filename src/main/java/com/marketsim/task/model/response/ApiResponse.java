package com.marketsim.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    private int code;
    private String message;
    private boolean success;
    private T data;

    public ApiResponse(int value, String s, boolean b) {
        this.code = value;
        this.message = s;
        this .success= b;
    }
}


