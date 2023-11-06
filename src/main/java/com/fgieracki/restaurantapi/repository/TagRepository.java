package com.fgieracki.restaurantapi.repository;

import com.fgieracki.restaurantapi.model.Item;
import com.fgieracki.restaurantapi.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Item> findByTagId(Long tagId);
    Boolean existsByName(String name);
}
