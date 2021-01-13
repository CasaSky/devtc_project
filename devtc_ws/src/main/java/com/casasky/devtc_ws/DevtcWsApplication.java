package com.casasky.devtc_ws;


import com.casasky.core.CoreApplication;
import com.casasky.core.config.BasicConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
public class DevtcWsApplication extends CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtcWsApplication.class, args);
    }

}
