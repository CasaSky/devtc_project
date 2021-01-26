package com.casasky.devtc_ws.controller;


import static java.lang.String.format;

import com.casasky.devtc_ws.service.ToolUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("updater/{maintenanceId}")
public class ToolUpdaterController {

    @Autowired
    private ToolUpdater toolUpdater;


    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long maintenanceId) {
        boolean updated = toolUpdater.update(maintenanceId).get();
        return ResponseEntity.ok(format("updated: %s", updated));
    }

    @GetMapping
    public ResponseEntity<?> check(@PathVariable Long maintenanceId) {
        String newReleaseVersion = toolUpdater.check(maintenanceId);
        return ResponseEntity.ok(newReleaseVersion);
    }
}
