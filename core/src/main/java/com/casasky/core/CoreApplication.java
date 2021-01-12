package com.casasky.core;


import com.casasky.core.config.BasicConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import({ BasicConfig.class })
@SpringBootApplication
public class CoreApplication {}
