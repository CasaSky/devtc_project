package com.casasky.devtc_ws.controller;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.casasky.devtc_ws.entity.Tool;
import com.casasky.devtc_ws.service.ToolDto;
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
    public ResponseEntity<?> create(@RequestBody @Valid ToolDto tool) {

        toolService.create(tool);
        return ResponseEntity.noContent().build();

    }


    @GetMapping
    public ResponseEntity<List<ToolDto>> tools() {

        var tools = toolService.findAll(Tool.class)
                .stream()
                .map(t -> new ToolDto(t.getName()))
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(tools);

    }

}
