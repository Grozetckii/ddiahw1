package ru.grozetsky.ddiahw1.mapper;

import org.mapstruct.Mapper;
import ru.grozetsky.ddiahw1.dto.ItemDto;
import ru.grozetsky.ddiahw1.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto entityToDto(Item entity);
}
