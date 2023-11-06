package com.fgieracki.restaurantapi.service;

import com.fgieracki.restaurantapi.payload.ItemDTO;
import com.fgieracki.restaurantapi.payload.ReorderDTO;

import java.util.List;

public interface ItemService {
    ItemDTO createItem(Long categoryId, ItemDTO itemDTO); //OK
    ItemDTO getItemById(Long itemId); //OK
    List<ItemDTO> getItemsByCategoryId(Long categoryId);
    List<ItemDTO> getItemsByTagId(Long tagId);
    ItemDTO updateItem(Long categoryId, Long itemId, ItemDTO itemDTO);
    void reorderItems(Long categoryId, List<ReorderDTO> reorderDTOs);
    void deleteItem(Long itemId);
}
