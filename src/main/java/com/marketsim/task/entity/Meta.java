package com.marketsim.task.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
@Embeddable
    public class Meta {
        @Temporal(TemporalType.TIMESTAMP)
        private Date createdAt;

        @Temporal(TemporalType.TIMESTAMP)
        private Date updatedAt;

        private String barcode;
        private String qrCode;

        // Getters and Setters
    }

