package com.marketsim.task.repository;

import com.marketsim.task.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String adminUsername);
}
