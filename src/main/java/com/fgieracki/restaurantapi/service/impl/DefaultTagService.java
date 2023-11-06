package com.fgieracki.restaurantapi.service.impl;

import com.fgieracki.restaurantapi.exception.PayloadException;
import com.fgieracki.restaurantapi.exception.ResourceAlreadyExistsException;
import com.fgieracki.restaurantapi.exception.ResourceNotFoundException;
import com.fgieracki.restaurantapi.model.Tag;
import com.fgieracki.restaurantapi.payload.TagDTO;
import com.fgieracki.restaurantapi.repository.TagRepository;
import com.fgieracki.restaurantapi.service.TagService;
import com.fgieracki.restaurantapi.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.ID_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.NAME_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.NULL;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.TAG_OBJECT_NAME;
import static com.fgieracki.restaurantapi.utils.Converter.convertToDTO;
import static com.fgieracki.restaurantapi.utils.Converter.convertToEntity;

@Service
@RequiredArgsConstructor
public class DefaultTagService implements TagService {
    private final TagRepository tagRepository;
    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        validateTagDTO(tagDTO);
        Tag tag = convertToEntity(tagDTO);
        throwExceptionIfTagExists(tag.getName());
        Tag newTag = tagRepository.save(tag);

        return convertToDTO(newTag);
    }

    @Override
    public TagDTO getTagById(Long tagId) {
        Tag tag = retrieveTagById(tagId);

        return convertToDTO(tag);
    }

    @Override
    public List<TagDTO> getTags() {
        return tagRepository.findAll().stream()
                .map(Converter::convertToDTO)
                .toList();
    }


    @Override
    public TagDTO updateTag(Long tagId, TagDTO tagDTO) {
        validateTagDTO(tagDTO);
        throwExceptionIfTagExists(tagDTO.getName());

        Tag tag = convertToEntity(tagDTO);
        Tag newTag = tagRepository.save(tag);

        return convertToDTO(newTag);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = retrieveTagById(tagId);
        tagRepository.delete(tag);
    }

    private void validateTagDTO(TagDTO tagDTO) {
        if(tagDTO == null) {
            throw new PayloadException(TAG_OBJECT_NAME, TAG_OBJECT_NAME + " cannot be null");
        } else
        if(tagDTO.getName() == null || tagDTO.getName().isBlank()) {
            throw new PayloadException(TAG_OBJECT_NAME, NAME_FIELD_NAME + " cannot be null or blank");
        }
    }

    private void throwExceptionIfTagExists(String tagName) {
        if(tagName == null || tagName.isEmpty()) return;

        if (Boolean.TRUE.equals(tagRepository.existsByName(tagName))) {
            throw new ResourceAlreadyExistsException(TAG_OBJECT_NAME, NAME_FIELD_NAME, tagName);
        }
    }

    private Tag retrieveTagById(Long tagId) {
        if(tagId == null) throw new ResourceNotFoundException(TAG_OBJECT_NAME, ID_FIELD_NAME, NULL);

        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException(TAG_OBJECT_NAME, ID_FIELD_NAME, tagId.toString()));
    }
}
