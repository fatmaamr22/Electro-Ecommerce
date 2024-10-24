package com.example.electro.service;

import com.example.electro.dto.CategoryDTO;
import com.example.electro.mapper.CategoryMapper;
import com.example.electro.model.Category;
import com.example.electro.repository.CategoryRepository;
import com.example.electro.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FilterService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public FilterService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    public List<CategoryDTO> getCategories(){
        return CategoryMapper.Instance.toDTOs(categoryRepository.findAll());
    }
    public List<String> getBrands() {
        return entityManager.createQuery("select distinct p.brandName from Product p", String.class)
                .getResultList();
    }

    public List<String> getProcessors() {
        return entityManager.createQuery("select distinct p.specs.processor from Product p", String.class)
                .getResultList();
    }

    public List<Integer> getMemoryList() {
        return entityManager.createQuery("select distinct p.specs.memory from Product p", Integer.class)
                .getResultList();
    }

    public List<String> getOsList() {
        return entityManager.createQuery("select distinct p.specs.os from Product p", String.class)
                .getResultList();
    }

    public List<Integer> getBatteryList() {
        return entityManager.createQuery("select distinct p.specs.batteryLife from Product p", Integer.class)
                .getResultList();
    }

    public Integer getMinPrice() {
        Integer minPrice = entityManager.createQuery("select min(p.price) from Product p", Integer.class)
                .getSingleResult();
        return minPrice != null ? minPrice - (minPrice % 1000) : null; // Adjusted for 1000s
    }

    public Integer getMaxPrice() {
        Integer maxPrice = entityManager.createQuery("select max(p.price) from Product p", Integer.class)
                .getSingleResult();
        return maxPrice != null ? maxPrice + (1000 - maxPrice % 1000) : null; // Adjusted for 1000s
    }
}
