package ru.grozetsky.ddiahw1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.grozetsky.ddiahw1.dto.ItemDto;
import ru.grozetsky.ddiahw1.entity.Item;
import ru.grozetsky.ddiahw1.mapper.ItemMapper;
import ru.grozetsky.ddiahw1.repository.ItemRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    public ItemDto getItemDtoById(Long id) {
        return itemMapper.entityToDto(getItemById(id));
    }

    public void createItem(String content) {
        Item newItem = new Item();
        newItem.setContent(content);

        itemRepository.save(newItem);
    }

    @Transactional
    public ItemDto updateContentById(Long id, String newContent) {
        Item item = getItemById(id);
        item.setContent(newContent);

        return itemMapper.entityToDto(item);
    }

    private Item getItemById(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);

        if(itemOpt.isEmpty()) {
            throw new NoSuchElementException("item with id %s not found".formatted(id));
        }

        return itemOpt.get();
    }
}
