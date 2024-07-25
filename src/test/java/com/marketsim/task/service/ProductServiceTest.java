package com.marketsim.task.service;

import com.marketsim.task.entity.Product;
import com.marketsim.task.entity.Review;
import com.marketsim.task.model.request.ProductUpdateRequest;
import com.marketsim.task.model.response.ProductResponse;
import com.marketsim.task.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Value("${product.api.url}")
    private String productApiUrl;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchAndSaveProducts() {
        ProductResponse productResponse = new ProductResponse();
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");
        product.setCategory("Category 1");
        Review review = new Review();
        review.setId(1L);
        review.setComment("Review 1");
        product.setReviews(Collections.singletonList(review));
        productResponse.setProducts(Collections.singletonList(product));//setting up the product Response

        when(restTemplate.getForEntity(productApiUrl, ProductResponse.class))
                .thenReturn(new ResponseEntity<>(productResponse, HttpStatus.OK));

        productService.fetchAndSaveProducts();

        verify(productRepository).saveAll(any(List.class));
    }

    @Test
    public void testSearchProducts() {
        String query = "Product";
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");

        when(productRepository.searchProducts(query)).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.searchProducts(query);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getTitle());
    }

    @Test
    public void testSearchProductsByCategory() {
        String category = "Category 1";
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");
        product.setCategory(category);

        when(productRepository.findByCategorys(category)).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.searchProductsByCategory(category);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getTitle());
    }

    @Test
    public void testDeleteProductsByCategory() {
        String category = "Category 1";
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");
        product.setCategory(category);

        when(productRepository.findByCategoryIgnoreCase(category)).thenReturn(Collections.singletonList(product));

        productService.deleteProductsByCategory(category);

        verify(productRepository).deleteAll(any(List.class));
    }

    @Test
    public void testDoesCategoryExist() {
        String category = "Category 1";

        when(productRepository.countByCategory(category)).thenReturn(1L);

        boolean exists = productService.doesCategoryExist(category);

        assertTrue(exists);
    }

    @Test
    public void testUpdateProductById() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(1);
        request.setTitle("Updated Title");

        Product product = new Product();
        product.setId(1);
        product.setTitle("Old Title");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.updateProductById(request);

        verify(productRepository).save(any(Product.class));
    }
}
