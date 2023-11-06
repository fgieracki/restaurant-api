package com.fgieracki.restaurantapi.utils;

import com.fgieracki.restaurantapi.model.Category;
import com.fgieracki.restaurantapi.model.Item;
import com.fgieracki.restaurantapi.model.Tag;
import com.fgieracki.restaurantapi.payload.CategoryDTO;
import com.fgieracki.restaurantapi.payload.ItemDTO;
import com.fgieracki.restaurantapi.payload.TagDTO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {
    private Converter() {}

    public static Item convertToEntity(ItemDTO itemDTO) {
        if(itemDTO == null) return null;

        Item item = new Item();
        item.setItemId(itemDTO.getItemId());
        item.setCode(itemDTO.getCode());
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setPrice(itemDTO.getPrice());
        item.setCategory(convertToEntity(itemDTO.getCategory()));
        item.setTags(convertToTags(itemDTO.getTagIds()));

        return item;
    }

    public static ItemDTO convertToDTO(Item item) {
        if(item == null) return null;

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(item.getItemId());
        itemDTO.setCode(item.getCode());
        itemDTO.setName(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setCategory(convertToDTO(item.getCategory()));
        itemDTO.setTagIds(getTagIds(item.getTags()));

        return itemDTO;
    }

    public static Category convertToEntity(CategoryDTO categoryDTO) {
        if(categoryDTO == null) return null;

        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());

        return category;
    }

    public static CategoryDTO convertToDTO(Category category) {
        if(category == null) return null;

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    private static Set<Long> getTagIds(Set<Tag> tags) {
        if(tags == null) return new HashSet<>();

        return tags.stream()
                .map(Tag::getTagId)
                .collect(Collectors.toSet());
    }

    private static Set<Tag> convertToTags(Set<Long> tagIds) {
        if(tagIds == null) return new HashSet<>();

        return tagIds.stream()
                .map(tagId -> {
                    Tag tag = new Tag();
                    tag.setTagId(tagId);
                    return tag;
                })
                .collect(Collectors.toSet());
    }

    public static Tag convertToEntity(TagDTO tagDTO) {
        if(tagDTO == null) return null;

        Tag tag = new Tag();
        tag.setTagId(tagDTO.getTagId());
        tag.setName(tagDTO.getName());

        return tag;
    }

    public static TagDTO convertToDTO(Tag tag) {
        if(tag == null) return null;

        TagDTO tagDTO = new TagDTO();
        tagDTO.setTagId(tag.getTagId());
        tagDTO.setName(tag.getName());

        return tagDTO;
    }

    public static Set<Tag> convertToEntity(Set<TagDTO> dtoSet) {
        if(dtoSet == null) return new HashSet<>();

        return dtoSet.stream()
                .map(Converter::convertToEntity)
                .collect(Collectors.toSet());
    }

    public static Set<TagDTO> convertToDTO(Set<Tag> tagSet) {
        if(tagSet == null) return new HashSet<>();

        return tagSet.stream()
                .map(Converter::convertToDTO)
                .collect(Collectors.toSet());
    }
}
