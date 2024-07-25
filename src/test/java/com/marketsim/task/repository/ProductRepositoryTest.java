package com.marketsim.task.repository;

import com.marketsim.task.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Annotate the class to indicate that it is a JPA test
@DataJpaTest
// Scan for components in the specified package
@ComponentScan(basePackages = "com.marketsim.task")
// Configure the test database to replace any existing configuration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// Specify the active profile for the test
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setTitle("Test Product");
        product.setDescription("Test Description");
        product.setCategory("Test Category");
        productRepository.save(product);
    }

    @Test
    void searchProducts_success() {
        List<Product> products = productRepository.searchProducts("Test");
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getTitle());
    }

    @Test
    void searchProducts_noMatch() {
        List<Product> products = productRepository.searchProducts("Nonexistent");
        assertTrue(products.isEmpty());
    }

    @Test
    void findByCategorys_success() {
        List<Product> products = productRepository.findByCategorys("Test Category");
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Test Category", products.get(0).getCategory());
    }

    @Test
    void findByCategorys_noMatch() {
        List<Product> products = productRepository.findByCategorys("Nonexistent Category");
        assertTrue(products.isEmpty());
    }

    @Test
    void countByCategory_success() {
        long count = productRepository.countByCategory("Test Category");
        assertEquals(1, count);
    }

    @Test
    void countByCategory_noMatch() {
        long count = productRepository.countByCategory("Nonexistent Category");
        assertEquals(0, count);
    }

    @Test
    void findByCategoryIgnoreCase_success() {
        List<Product> products = productRepository.findByCategoryIgnoreCase("test category");
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Test Category", products.get(0).getCategory());
    }

    @Test
    void findByCategoryIgnoreCase_noMatch() {
        List<Product> products = productRepository.findByCategoryIgnoreCase("nonexistent category");
        assertTrue(products.isEmpty());
    }
}
/*
for testing we need to create the Database
we need to configure correctly in the entity  class, that,   auto generated the fields
* */