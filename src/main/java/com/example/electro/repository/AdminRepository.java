package com.example.electro.repository;

import com.example.electro.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository  extends JpaRepository<Admin, Integer> {
    public Optional<Admin> findAdminByEmail(String email);
}
