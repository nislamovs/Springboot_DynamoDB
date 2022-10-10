package com.example.dynamoDemo.controllers;

import com.example.dynamoDemo.controllers.apidocs.iTableDescriptionController;
import com.example.dynamoDemo.service.TableDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TableDescriptionController implements iTableDescriptionController {

    private final TableDescriptionService service;

    @Override
    @GetMapping("/table/info")
    public Flux<?> getTableInfo() {
        log.info("Getting all tables with descriptions");
        return Flux.just(service.getTableInfo());
    }
}
