package com.fgieracki.restaurantapi.service;

import com.fgieracki.restaurantapi.payload.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategoryById(Long categoryId);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);

}
