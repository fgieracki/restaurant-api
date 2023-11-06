package com.fgieracki.restaurantapi.repository;

import com.fgieracki.restaurantapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryId(Long categoryId);
    public Boolean existsByName(String name);
}
