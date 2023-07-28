package com.anywayclear.controller;

import com.anywayclear.service.DibService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class DibController {
    private final DibService dibService;

    public DibController(DibService dibService) {
        this.dibService = dibService;
    }

}
