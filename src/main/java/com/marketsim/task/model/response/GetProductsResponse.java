package com.marketsim.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsResponse {
    private int code;
    private String message;
    private boolean success;
}
