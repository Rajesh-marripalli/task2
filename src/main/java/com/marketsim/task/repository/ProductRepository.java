package com.marketsim.task.repository;

import com.marketsim.task.entity.Product;
import jakarta.transaction.Transactional;
import org.hibernate.sql.Delete;
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
    List<Product> findByCategorys(@Param("category") String category);
/*
* UPDATE Product p SET p.category = NULL WHERE LOWER(p.category) = LOWER(:category)*/
    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    void deleteByCategory(@Param("category") String category);
    List<Product> findByCategory(String category);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    long countByCategory(@Param("category") String category);

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.product.id = :productId")
    void deleteReviewsByProductId(@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    List<Product> findByCategoryIgnoreCase(@Param("category") String category);


}

