package com.fgieracki.restaurantapi.service.impl;

import com.fgieracki.restaurantapi.exception.RequestConflictException;
import com.fgieracki.restaurantapi.exception.ResourceAlreadyExistsException;
import com.fgieracki.restaurantapi.exception.ResourceNotFoundException;
import com.fgieracki.restaurantapi.model.Category;
import com.fgieracki.restaurantapi.payload.CategoryDTO;
import com.fgieracki.restaurantapi.repository.CategoryRepository;
import com.fgieracki.restaurantapi.service.CategoryService;
import com.fgieracki.restaurantapi.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.CATEGORY_ID_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.CATEGORY_OBJECT_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.NAME_FIELD_NAME;
import static com.fgieracki.restaurantapi.utils.Converter.convertToDTO;
import static com.fgieracki.restaurantapi.utils.Converter.convertToEntity;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        throwExceptionIfCategoryExistsByName(category.getName());
        Category newCategory = categoryRepository.save(category);

        return convertToDTO(newCategory);
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = retrieveCategoryById(categoryId);

        return convertToDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(Converter::convertToDTO).toList();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category existingCategory = retrieveCategoryById(categoryId);

        if(categoryDTO.getCategoryId() != null && !categoryId.equals(category.getCategoryId())) {
            throw new RequestConflictException(CATEGORY_OBJECT_NAME, CATEGORY_ID_FIELD_NAME, categoryId.toString(), category.getCategoryId().toString());
        }

        throwExceptionIfCategoryExistsByName(category.getName());

        existingCategory.setName(category.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);

        return convertToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = retrieveCategoryById(categoryId);

        categoryRepository.delete(category);
    }

    private Category retrieveCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_OBJECT_NAME, CATEGORY_ID_FIELD_NAME, categoryId.toString()));
    }

    private void throwExceptionIfCategoryExistsByName(String name) {
        if(Boolean.TRUE.equals(categoryRepository.existsByName(name))) {
            throw new ResourceAlreadyExistsException(CATEGORY_OBJECT_NAME, NAME_FIELD_NAME, name);
        }
    }
}
