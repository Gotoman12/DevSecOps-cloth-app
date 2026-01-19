package com.example.clothapp.service;

import com.example.clothapp.model.ClothingItem;
import com.example.clothapp.repository.ClothingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClothingServiceTest {

    @Mock
    private ClothingItemRepository repository;

    @InjectMocks
    private ClothingService service;

    private ClothingItem testItem;

    @BeforeEach
    void setUp() {
        testItem = new ClothingItem(1L, "T-Shirt", "M", "Blue", BigDecimal.valueOf(19.99), 50, "Tops");
    }

    @Test
    void listAll_withNoQuery_shouldReturnAllItems() {
        List<ClothingItem> items = Arrays.asList(testItem);
        when(repository.findAll()).thenReturn(items);

        List<ClothingItem> result = service.listAll(null);

        assertEquals(1, result.size());
        assertEquals("T-Shirt", result.get(0).getName());
        verify(repository).findAll();
    }

    @Test
    void listAll_withQuery_shouldSearchByNameOrCategory() {
        when(repository.searchByNameOrCategory("shirt")).thenReturn(Arrays.asList(testItem));

        List<ClothingItem> result = service.listAll("shirt");

        assertEquals(1, result.size());
        verify(repository).searchByNameOrCategory("shirt");
    }

    @Test
    void getById_whenItemExists_shouldReturnItem() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        ClothingItem result = service.getById(1L);

        assertNotNull(result);
        assertEquals("T-Shirt", result.getName());
        verify(repository).findById(1L);
    }

    @Test
    void getById_whenItemNotFound_shouldThrowException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getById(99L));
    }

    @Test
    void create_shouldSaveNewItemWithNullId() {
        ClothingItem newItem = new ClothingItem(5L, "Jeans", "32", "Blue", BigDecimal.valueOf(49.99), 30, "Bottoms");
        when(repository.save(any(ClothingItem.class))).thenReturn(newItem);

        ClothingItem result = service.create(newItem);

        assertNotNull(result);
        verify(repository).save(argThat(item -> item.getId() == null));
    }

    @Test
    void update_shouldUpdateExistingItem() {
        ClothingItem updated = new ClothingItem(null, "Updated", "L", "Red", BigDecimal.valueOf(29.99), 40, "Tops");
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(any(ClothingItem.class))).thenReturn(testItem);

        ClothingItem result = service.update(1L, updated);

        assertEquals("Updated", result.getName());
        assertEquals("L", result.getSize());
        verify(repository).save(testItem);
    }

    @Test
    void decreaseStock_withSufficientStock_shouldDecreaseSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(any(ClothingItem.class))).thenReturn(testItem);

        service.decreaseStock(1L, 10);

        assertEquals(40, testItem.getStock());
        verify(repository).save(testItem);
    }

    @Test
    void decreaseStock_withInsufficientStock_shouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThrows(IllegalArgumentException.class, () -> service.decreaseStock(1L, 100));
    }
}
