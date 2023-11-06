package com.fgieracki.restaurantapi.service.impl;

import com.fgieracki.restaurantapi.exception.ResourceAlreadyExistsException;
import com.fgieracki.restaurantapi.exception.ResourceNotFoundException;
import com.fgieracki.restaurantapi.model.Category;
import com.fgieracki.restaurantapi.model.Item;
import com.fgieracki.restaurantapi.model.Tag;
import com.fgieracki.restaurantapi.payload.ItemDTO;
import com.fgieracki.restaurantapi.payload.ReorderDTO;
import com.fgieracki.restaurantapi.repository.CategoryRepository;
import com.fgieracki.restaurantapi.repository.ItemRepository;
import com.fgieracki.restaurantapi.repository.TagRepository;
import com.fgieracki.restaurantapi.service.ItemService;
import com.fgieracki.restaurantapi.utils.Converter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.CATEGORY_ID_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.CATEGORY_OBJECT_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.CODE_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.ITEM_ID_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.ITEM_OBJECT_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.NAME_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.NULL;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.TAG_ID_FIELD_NAME;
import static com.fgieracki.restaurantapi.service.impl.ServiceConstants.TAG_OBJECT_NAME;
import static com.fgieracki.restaurantapi.utils.Converter.convertToDTO;
import static com.fgieracki.restaurantapi.utils.Converter.convertToEntity;

@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional
    @Override
    public ItemDTO createItem(Long categoryId, ItemDTO itemDTO) {
        Category category = retrieveCategoryById(categoryId);
        Item item = convertToEntity(itemDTO);
        item.setCategory(category);
        item.setTags(retrieveTagsForItem(itemDTO.getTagIds()));

        throwExceptionIfItemAlreadyExists(item);

        Item newItem = itemRepository.save(item);
        newItem.setPosition((double) newItem.getItemId());
        itemRepository.save(newItem);

        return convertToDTO(newItem);
    }

    @Override
    public ItemDTO getItemById(Long itemId) {
        Item item = retrieveItemById(itemId);

        return convertToDTO(item);
    }

    @Override
    public List<ItemDTO> getItemsByCategoryId(Long categoryId) {
        return retrieveItemsByCategoryId(categoryId).stream()
                .map(Converter::convertToDTO)
                .toList();

    }

    @Override
    public List<ItemDTO> getItemsByTagId(Long tagId) {
        throwExceptionIfTagDoesNotExist(tagId);

        List<Item> items = tagRepository.findByTagId(tagId);

        return items.stream()
                .map(Converter::convertToDTO)
                .toList();
    }

    @Override
    public ItemDTO updateItem(Long categoryId, Long itemId, ItemDTO itemDTO) {
        Category category = categoryRepository.getReferenceById(categoryId);
        Item item = retrieveItemById(itemId);
        item.setCategory(category);
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setPrice(itemDTO.getPrice());
        item.setTags(retrieveTagsForItem(itemDTO.getTagIds()));

        throwExceptionIfItemIsDuplicate(item);

        Item updatedItem = itemRepository.save(item);

        return convertToDTO(updatedItem);
    }

    @Transactional
    @Override
    public void reorderItems(Long categoryId, List<ReorderDTO> reorderDTOs) {
        reorderDTOs.forEach(
            reorderDTO -> {
                Item item = retrieveItemById(reorderDTO.getItemId());
                List<Item> items = retrieveItemsByCategoryId(categoryId);
                Double positionBefore = getPositionBefore(reorderDTO.getBeforeItemId());
                Double positionAfter = getLowestItemPositionHigherThan(items, positionBefore);

                if(item.getPosition().equals(positionAfter) || item.getPosition().equals(positionBefore)) return;
                double positionDiff = ((positionAfter - positionBefore) / 2);

                if(positionDiff < 1e-10) {
                    reorderAllElements(items);
                    positionBefore = getPositionBefore(reorderDTO.getBeforeItemId());
                    positionAfter = getLowestItemPositionHigherThan(items, positionBefore);
                    positionDiff = ((positionAfter - positionBefore) / 2);
                }

                item.setPosition(positionBefore + (positionDiff));
                itemRepository.save(item);
            }
        );
    }

    private Double getPositionBefore(Long beforeItemId) {
        Item beforeItem = itemRepository.findById(beforeItemId).orElse(null);
        return beforeItem != null ? beforeItem.getPosition() : 0;
    }

    private Double getLowestItemPositionHigherThan(List<Item> items, Double position) {
        Item tmp = items.stream()
                .filter(item -> item.getPosition() > position)
                .min(Comparator.comparing(Item::getPosition))
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_OBJECT_NAME, "position", position.toString()));

        return tmp.getPosition();
    }

    @Transactional
    private void reorderAllElements(List <Item> items) {
        for(int i=1; i <= items.size(); i++) {
            items.get(i-1).setPosition((double) i);
            itemRepository.save(items.get(i-1));
        }
    }

    @Override
    public void deleteItem(Long itemId) {
        Item item = retrieveItemById(itemId);
        itemRepository.delete(item);
    }

    private void throwExceptionIfItemAlreadyExists(Item item) {
        if (Boolean.TRUE.equals(itemRepository.existsByCodeAndName(item.getCode(), item.getName()))) {
            throw new ResourceAlreadyExistsException(ITEM_OBJECT_NAME, CODE_FIELD_NAME, item.getCode().toString(),
                    NAME_FIELD_NAME, item.getName());
        }
    }

    private void throwExceptionIfItemIsDuplicate(Item item) {
        if (Boolean.TRUE.equals(itemRepository.existsByCodeAndNameAndItemIdIsNot(item.getCode(), item.getName(), item.getItemId()))) {
            throw new ResourceAlreadyExistsException(ITEM_OBJECT_NAME, CODE_FIELD_NAME, item.getCode().toString(),
                    NAME_FIELD_NAME, item.getName());
        }
    }

    private Item retrieveItemById(Long itemId) {
        if(itemId == null) throw new ResourceNotFoundException(ITEM_OBJECT_NAME, ITEM_ID_FIELD_NAME, NULL);

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_OBJECT_NAME, ITEM_ID_FIELD_NAME, itemId.toString()));
    }

    private List<Item> retrieveItemsByCategoryId(Long categoryId) {
        List<Item> items = itemRepository.findItemsByCategoryCategoryId(categoryId);
        return items.stream()
                .sorted(Comparator.comparing(Item::getPosition))
                .toList();
    }

    private Category retrieveCategoryById(Long categoryId) {
        if(categoryId == null) throw new ResourceNotFoundException(CATEGORY_OBJECT_NAME, CATEGORY_ID_FIELD_NAME, NULL);

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_OBJECT_NAME, CATEGORY_ID_FIELD_NAME, categoryId.toString()));
    }

    private void throwExceptionIfTagDoesNotExist(Long tagId) {
        if(tagId == null) throw new ResourceNotFoundException(TAG_OBJECT_NAME, TAG_ID_FIELD_NAME, NULL);

        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException(TAG_OBJECT_NAME, TAG_ID_FIELD_NAME, tagId.toString());
        }
    }

    private Set<Tag>  retrieveTagsForItem(Set <Long> tagIds) {
        if(tagIds == null) return new HashSet<>();

        return tagIds.stream()
                .map(tag -> tagRepository.findById(tag)
                        .orElseThrow(() -> new ResourceNotFoundException(TAG_OBJECT_NAME, TAG_ID_FIELD_NAME, tag.toString())))
                .collect(Collectors.toSet());
    }

}
