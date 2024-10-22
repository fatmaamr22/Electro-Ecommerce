package com.example.electro.repository;

import com.example.electro.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListDAO extends JpaRepository<Wishlist, Integer> {}
