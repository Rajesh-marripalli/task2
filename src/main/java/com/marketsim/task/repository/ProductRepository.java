package com.marketsim.task.repository;

import com.marketsim.task.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface ProductRepository extends JpaRepository<Product, Integer> {
        @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
        List<Product> searchProducts(@Param("query") String query);

    @Query("SELECT p FROM Product p WHERE LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<Product> findByCategory(@Param("category") String category);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    void deleteByCategory(@Param("category") String category);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    long countByCategory(@Param("category") String category);



    }

