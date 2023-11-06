package com.fgieracki.restaurantapi.controller;

import com.fgieracki.restaurantapi.payload.ItemDTO;
import com.fgieracki.restaurantapi.payload.ReorderDTO;
import com.fgieracki.restaurantapi.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories/{categoryId}/items")
    public ResponseEntity<ItemDTO> createItem(@PathVariable(name = "categoryId") Long categoryId,
                                              @Valid @RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.createItem(categoryId, itemDTO), HttpStatus.CREATED);
    }

    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<List<ItemDTO>> getItemsByCategoryId(@PathVariable(name = "categoryId") Long categoryId) {
        return new ResponseEntity<>(itemService.getItemsByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemDTO> getItemByItemId(@PathVariable(name = "itemId") Long itemId) {
        return new ResponseEntity<>(itemService.getItemById(itemId), HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemDTO>> getItemsByTagId(@RequestParam(value = "tagId") Long tagId) {
        return new ResponseEntity<>(itemService.getItemsByTagId(tagId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{categoryId}/items/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable(name="categoryId") Long categoryId,
                                              @PathVariable(name = "itemId") Long itemId,
                                              @Valid @RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.updateItem(categoryId, itemId, itemDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{categoryId}/reorder")
    public ResponseEntity<String> reorderItems(@PathVariable(name="categoryId") Long categoryId,
                                         @Valid @RequestBody List<ReorderDTO> reorderDTOS) {
        itemService.reorderItems(categoryId, reorderDTOS);
        return new ResponseEntity<>("Items reordered successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable(name = "itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    }
}
