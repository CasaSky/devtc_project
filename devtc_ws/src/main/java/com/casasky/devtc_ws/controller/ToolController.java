package com.casasky.devtc_ws.controller;


import java.util.List;

import com.casasky.devtc_ws.entity.Tool;
import com.casasky.devtc_ws.service.ToolService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("tools")
public class ToolController {

    private final ToolService toolService;

    private final Logger LOG = LogManager.getLogger(getClass());


    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody ToolDto tool) {

        toolService.persist(tool.entity());
        return ResponseEntity.noContent().build();

    }


    @GetMapping
    public ResponseEntity<List<Tool>> tools() {

        return ResponseEntity.ok(toolService.findAll(Tool.class));

    }

}
