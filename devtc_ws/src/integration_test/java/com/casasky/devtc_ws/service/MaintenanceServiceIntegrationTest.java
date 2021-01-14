package com.casasky.devtc_ws.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.Tool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class MaintenanceServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MaintenanceService maintenanceService;


    @Test
    void persistence() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.get(tool.getId());
        maintenanceService.persist(maintenance);

        List<Maintenance> all = maintenanceService.findAll(Maintenance.class);
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactly(maintenance);

    }

}
