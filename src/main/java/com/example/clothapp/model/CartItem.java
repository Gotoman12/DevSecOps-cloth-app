package com.example.clothapp.model;

import java.math.BigDecimal;

public class CartItem {

    private ClothingItem item;
    private int quantity;

    public CartItem(ClothingItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ClothingItem getItem() {
        return item;
    }

    public void setItem(ClothingItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return item.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
