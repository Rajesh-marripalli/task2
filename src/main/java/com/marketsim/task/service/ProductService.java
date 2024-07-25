package com.marketsim.task.service;

import com.marketsim.task.common.AppConstants;
import com.marketsim.task.entity.Product;
import com.marketsim.task.entity.Review;
import com.marketsim.task.exceptions.ProductServiceException;
import com.marketsim.task.model.request.ProductUpdateRequest;
import com.marketsim.task.model.response.ProductResponse;
import com.marketsim.task.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    public ProductService(RestTemplate restTemplate, ProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }

    @Value("${product.api.url}")
    private String productApiUrl;

    public void fetchAndSaveProducts() {
        try {
            log.info("Fetching and saving products from API");

            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(productApiUrl, ProductResponse.class);
            ProductResponse productResponse = response.getBody();

            List<Product> products = productResponse.getProducts();
            for (Product product : products) {
                if (product.getReviews() != null) {
                    for (Review review : product.getReviews()) {
                        review.setProduct(product);
                    }
                }
            }
            productRepository.saveAll(products);
            log.info("Products saved successfully");
        } catch (Exception e) {
            log.error("Error fetching and saving products", e);
            throw new ProductServiceException(AppConstants.UNEXPECTED_ERROR + e.getMessage(), e);
        }
    }

    @Cacheable("products")
    public List<Product> searchProducts(String query) {
        try {
            log.info("Searching products with query: {}", query);
            return productRepository.searchProducts(query);
        } catch (Exception e) {
            log.error("Error searching products", e);
            throw new ProductServiceException(AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), e);
        }
    }

    @Cacheable("categories")
    public List<Product> searchProductsByCategory(String category) {
        try {
            log.info("Searching products by category: {}", category);
            return productRepository.findByCategorys(category);
        } catch (Exception e) {
            log.error("Error searching products by category", e);
            throw new ProductServiceException(AppConstants.PRODUCTS_SEARCH_FAILURE + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "categories", allEntries = true)
    @Transactional
    public void deleteProductsByCategory(String category) {
        try {
            log.info("Deleting products by category: {}", category);
            List<Product> products = productRepository.findByCategoryIgnoreCase(category);
            productRepository.deleteAll(products);
            log.info("Products deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting products by category", e);
            throw new ProductServiceException(AppConstants.PRODUCTS_CATEGORY_DELETE_FAILURE + e.getMessage(), e);
        }
    }

    public boolean doesCategoryExist(String query) {
        log.info("Checking if category exists: {}", query);
        return productRepository.countByCategory(query) > 0;
    }

    public void updateProductById(ProductUpdateRequest productUpdateRequest) {
        try {
            log.info("Updating product with ID: {}", productUpdateRequest.getId());
            Product product = productRepository.findById(productUpdateRequest.getId())
                    .orElseThrow(() -> new ProductServiceException(AppConstants.PRODUCT_NOT_FOUND_WITH_ID + productUpdateRequest.getId()));
            if (product.getTitle().equals(productUpdateRequest.getTitle())) {
                throw new ProductServiceException(AppConstants.SAME_TITLE_AVAILABLE);
            }
            product.setTitle(productUpdateRequest.getTitle());
            productRepository.save(product);
            log.info("Product updated successfully with ID: {}", productUpdateRequest.getId());
        } catch (Exception e) {
            log.error("Error updating product with ID: {}", productUpdateRequest.getId(), e);
            throw new ProductServiceException(e.getMessage(), e);
        }
    }

}
