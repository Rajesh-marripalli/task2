package com.marketsim.task.service;

import com.marketsim.task.common.AppConstants;
import com.marketsim.task.entity.Product;
import com.marketsim.task.exceptions.ProductNotFoundException;
import com.marketsim.task.exceptions.ProductServiceException;
import com.marketsim.task.model.request.ProductUpdateRequest;
import com.marketsim.task.model.response.ProductResponse;
import com.marketsim.task.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;
    @Value("${product.api.url}")
    private String productApiUrl;

    public void fetchAndSaveProducts() {
        try {

            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(productApiUrl, ProductResponse.class);
          //checking status code
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ProductServiceException(AppConstants.FETCH_PRODUCTS_ERROR + response.getStatusCode());
            }
            //handling response body
            ProductResponse productResponse = response.getBody();
            if (productResponse == null || productResponse.getProducts() == null) {
                throw new ProductServiceException(AppConstants.INVALID_RESPONSE_BODY);
            }
            //getting products from product response object
            List<Product> products = productResponse.getProducts();
            productRepository.saveAll(products);
        } catch (Exception e) {
            throw new ProductServiceException(AppConstants.UNEXPECTED_ERROR + e.getMessage(), e);
        }
    }


    public List<Product> searchProducts(String query) {
        try {
            return productRepository.searchProducts(query);
        } catch (Exception e) {
            throw new ProductServiceException(AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), e);
        }
    }

    public List<Product> searchProductsByCategory(String category) {
        try {
            return productRepository.findByCategory(category);
        } catch (Exception e) {
            throw new ProductServiceException(AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), e);
        }
    }

    public void deleteProductsByCategory(String category) {
        try {
            productRepository.deleteByCategory(category);
        } catch (Exception e) {
            throw new ProductServiceException(AppConstants.PRODUCTS_CATEGORY_DELETE_FAILURE + e.getMessage(), e);
        }
    }
    public boolean doesCategoryExist(String query) {
        return productRepository.countByCategory(query) > 0;
    }

    public void updateProductById(ProductUpdateRequest productUpdateRequest) {
        try {
            Product product = productRepository.findById(productUpdateRequest.getId())
                    .orElseThrow(() -> new ProductServiceException(AppConstants.Product_not_found_with_ID + productUpdateRequest.getId()));
            if (product.getTitle().equals(productUpdateRequest.getTitle())) {
                throw new ProductServiceException(AppConstants.Same_title_available);
            }
            product.setTitle(productUpdateRequest.getTitle());
            productRepository.save(product);
        } catch (Exception e) {
            throw new ProductServiceException(e.getMessage(), e);
        }
    }


}
