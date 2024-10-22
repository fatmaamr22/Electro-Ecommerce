package com.example.electro.repository;

import com.example.electro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

    Category findCategoryByName(String name);
}
