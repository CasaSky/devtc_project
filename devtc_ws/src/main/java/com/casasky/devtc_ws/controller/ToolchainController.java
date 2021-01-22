package com.casasky.devtc_ws.controller;


import java.util.Set;

import com.casasky.devtc_ws.service.MaintenanceService;
import com.casasky.devtc_ws.service.ManagedToolDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("toolchain")
public class ToolchainController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<Set<ManagedToolDto>> managedTools(@RequestParam String platform) {

        return ResponseEntity.ok(maintenanceService.deliverToolchain(platform));

    }

    @GetMapping(path = "{name}")
    public ResponseEntity<ManagedToolDto> managedTool(@PathVariable String name, @RequestParam String platform) {

        return ResponseEntity.ok(maintenanceService.deliverTool(name, platform));

    }

}
