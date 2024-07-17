package com.marketsim.task.exceptions;

import com.marketsim.task.common.ApiResponse;
import com.marketsim.task.common.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {


        @ExceptionHandler(ProductServiceException.class)
        public ResponseEntity<ApiResponse> handleProductServiceException(ProductServiceException ex) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCTS_FETCH_FAILURE + ex.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.UNEXPECTED_ERROR + ex.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


