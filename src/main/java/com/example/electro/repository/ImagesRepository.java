package com.example.electro.repository;

import com.example.electro.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Image,Integer> {}
