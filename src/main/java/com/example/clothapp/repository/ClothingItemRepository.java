package com.example.clothapp.repository;

import com.example.clothapp.model.ClothingItem;

import java.util.List;
import java.util.Optional;

public interface ClothingItemRepository {

    List<ClothingItem> findAll();

    Optional<ClothingItem> findById(Long id);

    List<ClothingItem> searchByNameOrCategory(String query);

    ClothingItem save(ClothingItem item);

    void deleteById(Long id);
}
