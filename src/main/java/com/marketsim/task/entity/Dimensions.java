package com.marketsim.task.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
    @Embeddable
    public class Dimensions {
        private double width;
        private double height;
        private double depth;
    }

