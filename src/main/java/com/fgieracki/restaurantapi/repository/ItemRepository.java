package com.fgieracki.restaurantapi.repository;

import com.fgieracki.restaurantapi.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Boolean existsByCodeAndNameAndItemIdIsNot(Long code, String name, Long itemId);
    Boolean existsByCodeAndName(Long code, String name);
    List<Item> findItemsByCategoryCategoryId(Long categoryId);
}
