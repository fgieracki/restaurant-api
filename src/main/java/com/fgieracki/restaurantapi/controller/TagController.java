package com.fgieracki.restaurantapi.controller;


import com.fgieracki.restaurantapi.payload.TagDTO;
import com.fgieracki.restaurantapi.service.TagService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/tags")
    ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return new ResponseEntity<>(tagService.createTag(tagDTO), HttpStatus.CREATED);
    }

    @GetMapping("/tags")
    ResponseEntity<List<TagDTO>> getTags() {
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    ResponseEntity<TagDTO> getTagById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(tagService.getTagById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/tags/{id}")
    ResponseEntity<TagDTO> updateTag(@PathVariable(name = "id") Long id, @RequestBody TagDTO tagDTO) {
        return new ResponseEntity<>(tagService.updateTag(id, tagDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/tags/{id}")
    ResponseEntity<String> deleteTag(@PathVariable(name = "id") Long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>("Tag deleted successfully", HttpStatus.OK);
    }
}
