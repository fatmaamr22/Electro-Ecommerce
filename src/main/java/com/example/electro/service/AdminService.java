package com.example.electro.service;

import com.example.electro.model.Admin;
import com.example.electro.repository.AdminRepository;

import java.util.Optional;


public class AdminService {
    private AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> findById(Integer id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    public Admin updateAdmin(Integer id, Admin adminDetails) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            Admin adminToUpdate = optionalAdmin.get();
            adminToUpdate.setUsername(adminDetails.getUsername());
            adminToUpdate.setPassword(adminDetails.getPassword()); ////Handle Password security /hash
            adminToUpdate.setEmail(adminDetails.getEmail());
            return adminRepository.save(adminToUpdate); //Use save for update
        } else {
            throw new RuntimeException("Admin not found with id " + id);
        }
    }
}
