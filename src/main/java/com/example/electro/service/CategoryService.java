package com.example.electro.service;

import com.example.electro.model.Category;
import com.example.electro.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    public CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll () {
        return categoryRepository.findAll();
    }
}
