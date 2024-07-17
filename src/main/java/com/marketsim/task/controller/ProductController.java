package com.marketsim.task.controller;

import com.marketsim.task.common.ApiResponse;
import com.marketsim.task.common.AppConstants;
import com.marketsim.task.entity.Product;
import com.marketsim.task.exceptions.ProductServiceException;
import com.marketsim.task.model.request.ProductUpdateRequest;
import com.marketsim.task.model.request.SearchRequest;
import com.marketsim.task.model.response.GetProductsResponse;
import com.marketsim.task.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/get-products")
    public ResponseEntity<GetProductsResponse> getProducts() {
        try {
            productService.fetchAndSaveProducts();
            GetProductsResponse response = new GetProductsResponse(HttpStatus.OK.value(), AppConstants.PRODUCTS_FETCH_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            GetProductsResponse response = new GetProductsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCTS_FETCH_FAILURE + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search-products")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@Valid @RequestBody SearchRequest searchRequest) {
        try {
            if (searchRequest.getQuery() == null || searchRequest.getQuery().isEmpty()) {
                ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_SEARCH_QUERY, false, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            List<Product> products = productService.searchProducts(searchRequest.getQuery());
            if (products.isEmpty()) {
                ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No products available", false, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.OK.value(), "Products found", true, products);
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), false, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search-by-category")
    public ResponseEntity<ApiResponse<List<Product>>> searchProductsByCategory(@Valid @RequestBody SearchRequest searchRequest) {
        try {
            if (searchRequest.getQuery() == null || searchRequest.getQuery().isEmpty()) {
                ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_SEARCH_QUERY, false, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            List<Product> products = productService.searchProductsByCategory(searchRequest.getQuery());
            if (products.isEmpty()) {
                ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No products available", false, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.OK.value(), "Products found", true, products);
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            ApiResponse<List<Product>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), false, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete-by-category")
    public ResponseEntity<GetProductsResponse> deleteProductsByCategory(@Valid @RequestBody SearchRequest searchRequest) {
        try {
            if (searchRequest.getQuery() == null || searchRequest.getQuery().isEmpty()) {
                GetProductsResponse response = new GetProductsResponse(HttpStatus.BAD_REQUEST.value(), AppConstants.INVALID_SEARCH_QUERY, false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            boolean categoryExists = productService.doesCategoryExist(searchRequest.getQuery());
            if (!categoryExists) {
                GetProductsResponse response = new GetProductsResponse(HttpStatus.NOT_FOUND.value(), AppConstants.PRODUCTS_CATEGORY_NOT_FOUND, false);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            productService.deleteProductsByCategory(searchRequest.getQuery());
            GetProductsResponse response = new GetProductsResponse(HttpStatus.OK.value(), AppConstants.PRODUCTS_CATEGORY_DELETE_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            GetProductsResponse response = new GetProductsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCTS_CATEGORY_DELETE_FAILURE + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/update-product")
    public ResponseEntity<GetProductsResponse> updateProductById(@Valid @RequestBody ProductUpdateRequest updateRequest) {
        try {
            productService.updateProductById(updateRequest);
            GetProductsResponse response = new GetProductsResponse(HttpStatus.OK.value(), AppConstants.PRODUCT_UPDATE_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (ProductServiceException e) {
            GetProductsResponse response = new GetProductsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PRODUCT_UPDATE_FAILURE + e.getMessage(), false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
