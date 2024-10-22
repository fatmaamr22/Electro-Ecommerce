package com.example.electro.repository;

import com.example.electro.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDAO extends JpaRepository<Admin, Integer> {

    Admin findAdminByEmail(String email);
}
