package com.example.clothapp.service;

import com.example.clothapp.model.CartItem;
import com.example.clothapp.model.ClothingItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final ClothingService clothingService;

    // Simple in-memory cart shared across sessions (sufficient for local demo)
    private final Map<Long, Integer> quantities = new LinkedHashMap<>();

    public CartService(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    public void addItem(Long itemId) {
        quantities.merge(itemId, 1, Integer::sum);
    }

    public void removeItem(Long itemId) {
        quantities.remove(itemId);
    }

    public void clear() {
        quantities.clear();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            ClothingItem item = clothingService.getById(entry.getKey());
            items.add(new CartItem(item, entry.getValue()));
        }
        return items;
    }

    public BigDecimal getTotal() {
        return getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Long, Integer> getQuantities() {
        return new LinkedHashMap<>(quantities);
    }
}
