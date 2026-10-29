package ru.grozetsky.ddiahw1.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@RestController
public class HandlersController {

    private final RequestMappingHandlerMapping mapping;

    public HandlersController(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    @GetMapping("/handlers")
    public List<String> getHandlers() {
        return mapping.getHandlerMethods().entrySet().stream()
                .filter(entry -> {
                    var patterns = entry.getKey().getPatternValues();
                    return patterns.stream().noneMatch(p -> p.equals("/health")
                            || p.equals("/metrics")
                            || p.equals("/handlers")
                            || p.equals("/error")
                    );
                })
                .flatMap(entry -> {
                    RequestMappingInfo info = entry.getKey();
                    var methods = info.getMethodsCondition().getMethods();
                    String method = methods.isEmpty() ? "ANY" : methods.iterator().next().name();
                    return info.getPatternValues().stream()
                            .map(pattern -> method + " " + pattern);
                })
                .distinct()
                .sorted()
                .toList();
    }
}
