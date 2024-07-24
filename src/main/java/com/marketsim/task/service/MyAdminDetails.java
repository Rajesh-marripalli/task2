package com.marketsim.task.service;

import com.marketsim.task.entity.Admin;
import com.marketsim.task.exceptions.UserNotFoundException;
import com.marketsim.task.repository.AdminRepo;
import com.marketsim.task.util.CustomAdminDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class MyAdminDetails implements UserDetailsService {
        @Autowired
        private AdminRepo adminRepo;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Admin admin = adminRepo.findByUsername(username);
            if (admin == null) {
                throw new UserNotFoundException("User not found");
            }
            return new CustomAdminDetails(admin);
        }
    }

