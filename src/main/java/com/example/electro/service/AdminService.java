package com.example.electro.service;
import com.example.electro.customDetails.CustomAdminDetails;
import com.example.electro.model.Admin;
import com.example.electro.repository.AdminRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {
    private AdminRepository adminRepository;


    // To compare encrypted passwords
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

    @Cacheable("adminDetailsCache")
    @Override
    public UserDetails loadUserByUsername(String email) {
        // Find admin by email
        System.out.println("Inside ADMIN SERVICE loadUserByUsername");
        return adminRepository.findAdminByEmail(email)
                .map(CustomAdminDetails::new) // Return CustomAdminDetails if found
                .orElse(null); // Return null if not found
    }



}
