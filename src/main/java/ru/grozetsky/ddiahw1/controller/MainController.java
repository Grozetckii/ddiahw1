package ru.grozetsky.ddiahw1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
