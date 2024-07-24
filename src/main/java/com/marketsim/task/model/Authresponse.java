package com.marketsim.task.model;

import lombok.Data;

@Data
public class Authresponse {
    private String token;
    public Authresponse(String jwt) {
        this.token = jwt;
    }

}
