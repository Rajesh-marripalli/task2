package com.marketsim.task.controller;
import com.marketsim.task.entity.Product;
import com.marketsim.task.model.request.ProductUpdateRequest;
import com.marketsim.task.model.request.SearchRequest;
import com.marketsim.task.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerTest {


    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    // MockMvc object to simulate HTTP requests to the controller
    private MockMvc mockMvc;

    // Method to set up the test environment before each test case runs
    @BeforeEach
    public void setUp() {
        // Initialize mocks and inject them into the ProductController
        MockitoAnnotations.initMocks(this);
        // Set up MockMvc to use the ProductController
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProducts() throws Exception {
        // Perform a GET request to the /get-products endpoint with CSRF protection
        mockMvc.perform(get("/get-products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk());
        // Verify that the fetchAndSaveProducts method of the ProductService is called once
        verify(productService, times(1)).fetchAndSaveProducts();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchProducts() throws Exception {
        // Create a SearchRequest object with a query
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery("Product");

        // Create a Product object representing a search result
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");

        // Mock the behavior of the ProductService to return a list containing the Product object
        when(productService.searchProducts("Product")).thenReturn(Collections.singletonList(product));

        // Perform a POST request to the /search-products endpoint with the search query
        mockMvc.perform(post("/search-products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"Product\"}"))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk())
                // Expect the title of the first product in the response data to be "Product 1"
                .andExpect(jsonPath("$.data[0].title").value("Product 1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSearchProductsByCategory() throws Exception {
        // Create a SearchRequest object with a query
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery("Category 1");

        // Create a Product object representing a search result
        Product product = new Product();
        product.setId(1);
        product.setTitle("Product 1");
        product.setCategory("Category 1");

        // Mock the behavior of the ProductService to return a list containing the Product object
        when(productService.searchProductsByCategory("Category 1")).thenReturn(Collections.singletonList(product));

        // Perform a POST request to the /search-by-category endpoint with the search query
        mockMvc.perform(post("/search-by-category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"Category 1\"}"))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk())
                // Expect the title of the first product in the response data to be "Product 1"
                .andExpect(jsonPath("$.data[0].title").value("Product 1"));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteProductsByCategory() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery("Category 1");

        // Mock the behavior of the ProductService to return true when checking if the category exists
        when(productService.doesCategoryExist("Category 1")).thenReturn(true);

        // Perform a POST request to the /delete-by-category endpoint with the delete query
        mockMvc.perform(post("/delete-by-category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"Category 1\"}"))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk());
        // Verify that the deleteProductsByCategory method of the ProductService is called once
        verify(productService, times(1)).deleteProductsByCategory("Category 1");
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProductById() throws Exception {
        // Create a ProductUpdateRequest object with an id and title
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setId(1);
        updateRequest.setTitle("Updated Title");

        // Perform a POST request to the /update-product endpoint with the update request
        mockMvc.perform(post("/update-product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"Updated Title\"}"))
                // Expect the status of the response to be OK (200)
                .andExpect(status().isOk());
        // Verify that the updateProductById method of the ProductService is called once
        verify(productService, times(1)).updateProductById(any(ProductUpdateRequest.class));
    }
}
