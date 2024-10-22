package com.example.electro.repository;

import com.example.electro.model.ProductSpecs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecsDAO extends JpaRepository<ProductSpecs, Integer> {}
