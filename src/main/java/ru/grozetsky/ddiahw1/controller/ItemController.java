package ru.grozetsky.ddiahw1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.grozetsky.ddiahw1.dto.ItemDto;
import ru.grozetsky.ddiahw1.service.ItemService;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemDtoById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createItem(@RequestParam String content) {
        itemService.createItem(content);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id,
                                              @RequestParam String newContent) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.updateContentById(id, newContent));
    }
}
