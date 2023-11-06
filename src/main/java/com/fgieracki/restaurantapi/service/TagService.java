package com.fgieracki.restaurantapi.service;

import com.fgieracki.restaurantapi.payload.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService{
    TagDTO createTag(TagDTO tagDTO);
    TagDTO getTagById(Long tagId);
    List<TagDTO> getTags();
    TagDTO updateTag(Long tagId, TagDTO tagDTO);
    void deleteTag(Long tagId);

}
