package com.blank.project.controller;

import com.blank.project.model.RandomName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class RandomNameController {

    private List<String> firstNames = Arrays.asList("Josh", "James", "Mark", "Olivier", "Jurgen", "Jon");

    private List<String> lastNames = Arrays.asList("Long", "Watters", "Fisher", "Gierke", "Hoeller", "Schneider");

    @GetMapping("/api/random")
    public RandomName randomName() {
        log.info("Received call on /api/random-name");

        final Random random = new Random();
        final String randomName = firstNames.get(random.nextInt(firstNames.size())).concat(" ").concat(lastNames.get(random.nextInt(lastNames.size())));

        return RandomName.builder().name(randomName).build();
    }
}
