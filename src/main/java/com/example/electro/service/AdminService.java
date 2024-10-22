package com.example.electro.service;

import com.laptop.dao.AdminDAO;
import com.laptop.entity.Admin;

public class AdminService {
    private final AdminDAO adminDAO;
    public AdminService() {
        adminDAO = new AdminDAO();
    }

    public Admin findById(Integer id) {
        return adminDAO.findById(id);
    }

    public Admin findByEmail(String email) {
        return adminDAO.findAdminByEmail(email);
    }

    public Admin update(Admin admin) {
        try{
            return adminDAO.update(admin);
        }
        catch(Exception e){
            return null;
        }

    }
}
