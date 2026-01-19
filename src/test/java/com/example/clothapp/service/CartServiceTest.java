package com.example.clothapp.service;

import com.example.clothapp.model.CartItem;
import com.example.clothapp.model.ClothingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ClothingService clothingService;

    @InjectMocks
    private CartService cartService;

    private ClothingItem testItem;

    @BeforeEach
    void setUp() {
        testItem = new ClothingItem(1L, "T-Shirt", "M", "Blue", BigDecimal.valueOf(19.99), 50, "Tops");
        cartService.clear(); // Clear cart before each test
    }

    @Test
    void addItem_shouldAddItemToCart() {
        cartService.addItem(1L);

        assertEquals(1, cartService.getQuantities().size());
        assertEquals(1, cartService.getQuantities().get(1L));
    }

    @Test
    void addItem_multipleTimes_shouldIncrementQuantity() {
        cartService.addItem(1L);
        cartService.addItem(1L);
        cartService.addItem(1L);

        assertEquals(3, cartService.getQuantities().get(1L));
    }

    @Test
    void removeItem_shouldRemoveItemFromCart() {
        cartService.addItem(1L);
        cartService.addItem(2L);

        cartService.removeItem(1L);

        assertFalse(cartService.getQuantities().containsKey(1L));
        assertTrue(cartService.getQuantities().containsKey(2L));
    }

    @Test
    void clear_shouldRemoveAllItems() {
        cartService.addItem(1L);
        cartService.addItem(2L);
        cartService.addItem(3L);

        cartService.clear();

        assertTrue(cartService.getQuantities().isEmpty());
    }

    @Test
    void getTotal_shouldCalculateCorrectTotal() {
        when(clothingService.getById(1L)).thenReturn(testItem);
        when(clothingService.getById(2L)).thenReturn(
                new ClothingItem(2L, "Jeans", "32", "Blue", BigDecimal.valueOf(49.99), 30, "Bottoms")
        );

        cartService.addItem(1L);
        cartService.addItem(1L); // 2x T-Shirt @ 19.99
        cartService.addItem(2L); // 1x Jeans @ 49.99

        BigDecimal total = cartService.getTotal();

        // Expected: (2 * 19.99) + (1 * 49.99) = 89.97
        assertEquals(0, total.compareTo(BigDecimal.valueOf(89.97)));
    }

    @Test
    void getCartItems_shouldReturnListOfCartItems() {
        when(clothingService.getById(1L)).thenReturn(testItem);

        cartService.addItem(1L);
        cartService.addItem(1L);

        List<CartItem> items = cartService.getCartItems();

        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals("T-Shirt", items.get(0).getItem().getName());
    }
}
