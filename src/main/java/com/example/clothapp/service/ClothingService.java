package com.example.clothapp.service;

import com.example.clothapp.model.ClothingItem;
import com.example.clothapp.repository.ClothingItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothingService {

    private final ClothingItemRepository repository;

    public ClothingService(ClothingItemRepository repository) {
        this.repository = repository;
    }

    public List<ClothingItem> listAll(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAll();
        }
        return repository.searchByNameOrCategory(query.trim());
    }

    public ClothingItem getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
    }

    public ClothingItem create(ClothingItem item) {
        item.setId(null);
        return repository.save(item);
    }

    public ClothingItem update(Long id, ClothingItem updated) {
        ClothingItem existing = getById(id);
        existing.setName(updated.getName());
        existing.setSize(updated.getSize());
        existing.setColor(updated.getColor());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setCategory(updated.getCategory());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void decreaseStock(Long itemId, int quantity) {
        ClothingItem item = getById(itemId);
        int newStock = item.getStock() - quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for item " + itemId);
        }
        item.setStock(newStock);
        repository.save(item);
    }
}
