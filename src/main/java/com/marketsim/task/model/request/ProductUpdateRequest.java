package com.marketsim.task.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotNull(message = "ID must not be null")
    private Integer id;

    @NotEmpty(message = "Title must not be empty")
    private String title;


}


