package com.example.clothapp.repository;

import com.example.clothapp.model.ClothingItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClothingItemRepository implements ClothingItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ClothingItem> rowMapper = (rs, rowNum) -> {
        ClothingItem item = new ClothingItem();
        item.setId(rs.getLong("id"));
        item.setName(rs.getString("name"));
        item.setSize(rs.getString("size"));
        item.setColor(rs.getString("color"));
        item.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
        item.setStock(rs.getInt("stock"));
        item.setCategory(rs.getString("category"));
        return item;
    };

    public JdbcClothingItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ClothingItem> findAll() {
        String sql = "SELECT * FROM clothing_item ORDER BY name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<ClothingItem> findById(Long id) {
        String sql = "SELECT * FROM clothing_item WHERE id = ?";
        List<ClothingItem> result = jdbcTemplate.query(sql, rowMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public List<ClothingItem> searchByNameOrCategory(String query) {
        String like = "%" + query.toLowerCase() + "%";
        String sql = "SELECT * FROM clothing_item WHERE lower(name) LIKE ? OR lower(category) LIKE ? ORDER BY name";
        return jdbcTemplate.query(sql, rowMapper, like, like);
    }

    @Override
    public ClothingItem save(ClothingItem item) {
        if (item.getId() == null) {
            String sql = "INSERT INTO clothing_item (name, size, color, price, stock, category) VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, item.getName());
                ps.setString(2, item.getSize());
                ps.setString(3, item.getColor());
                ps.setDouble(4, item.getPrice().doubleValue());
                ps.setInt(5, item.getStock());
                ps.setString(6, item.getCategory());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                item.setId(key.longValue());
            }
        } else {
            String sql = "UPDATE clothing_item SET name = ?, size = ?, color = ?, price = ?, stock = ?, category = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    item.getName(),
                    item.getSize(),
                    item.getColor(),
                    item.getPrice().doubleValue(),
                    item.getStock(),
                    item.getCategory(),
                    item.getId());
        }
        return item;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM clothing_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
