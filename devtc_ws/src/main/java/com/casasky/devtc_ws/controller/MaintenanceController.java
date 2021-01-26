package com.casasky.devtc_ws.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import com.casasky.devtc_ws.service.MaintenanceDto;
import com.casasky.devtc_ws.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("tools/{name}/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;


    @PostMapping
    public ResponseEntity<?> create(@PathVariable String name, @RequestBody @Valid MaintenanceDto maintenance) {
        Long maintenanceId = maintenanceService.create(name, maintenance);
        return ResponseEntity.created(maintenanceURI(name, maintenanceId))
                .build();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> find(@PathVariable String name, @PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.find(id));
    }


    private URI maintenanceURI(String toolName, Long maintenanceId) {
        return linkTo(methodOn(MaintenanceController.class).find(toolName, maintenanceId)).toUri();
    }

}
