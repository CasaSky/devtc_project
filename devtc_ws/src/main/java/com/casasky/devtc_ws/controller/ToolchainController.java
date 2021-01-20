package com.casasky.devtc_ws.controller;


import java.util.Set;

import com.casasky.devtc_ws.service.MaintenanceService;
import com.casasky.devtc_ws.service.ManagedToolDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("toolchain")
public class ToolchainController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<Set<ManagedToolDto>> managedTools() {

        return ResponseEntity.ok(maintenanceService.deliverToolchain());

    }

}
