package com.marketsim.task.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchRequest {
    @NotNull(message = "ID must not be null")
    private String query;
}
