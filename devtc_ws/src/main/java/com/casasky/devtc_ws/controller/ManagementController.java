package com.casasky.devtc_ws.controller;


import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("management")
public class ManagementController {

    @GetMapping
    public ResponseEntity<Set<ManagedToolDto>> managedTools() {

        return ResponseEntity.ok(ManagedToolDto.demo());

    }

}
