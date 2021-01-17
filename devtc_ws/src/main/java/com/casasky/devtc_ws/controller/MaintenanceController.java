package com.casasky.devtc_ws.controller;


import com.casasky.devtc_ws.service.MaintenanceDto;
import com.casasky.devtc_ws.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("tools/{toolId}/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;


    //TODO add jackarta validation
    @PostMapping
    public ResponseEntity<?> create(@PathVariable Long toolId, @RequestBody MaintenanceDto maintenance) {
        maintenanceService.create(toolId, maintenance);
        //TODO return created with uri ;)
        return ResponseEntity.ok().build();
    }

}
