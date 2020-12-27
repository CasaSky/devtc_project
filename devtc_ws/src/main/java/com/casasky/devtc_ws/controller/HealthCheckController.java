package com.casasky.devtc_ws.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("health_check")
@RestController
public class HealthCheckController {

    @GetMapping
    public void healthCheck() {}

}
