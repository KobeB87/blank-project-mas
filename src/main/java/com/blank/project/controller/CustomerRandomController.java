package com.blank.project.controller;

import com.blank.project.model.RandomName;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/random",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
        name = "random",
        description = "Call Random API using RestTemplate"
)
public class CustomerRandomController {

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<RandomName> sayHello() {
        log.info("Received call on /api/hello. And will call /api/random-name on the random-name-service!");

        final RandomName randomName = this.restTemplate.getForObject("http://localhost:8080/api/random-name", RandomName.class);
        log.info("Received response from  random-name-service: {}", randomName);

        return ResponseEntity.ok(randomName);
    }
}