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

        Maintenance maintenanceFromDB = maintenanceService.find(maintenance.getId());
        assertThat(maintenanceFromDB).usingRecursiveComparison().isEqualTo(maintenance);

    }


    @Test
    void findAll() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.get(tool.getId());
        maintenanceService.persist(maintenance);

        List<Maintenance> maintenanceFromDB = maintenanceService.findAll();
        assertThat(maintenanceFromDB).usingRecursiveFieldByFieldElementComparator().containsExactly(maintenance);

    }


    @Test
    void isDuplicate() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.get(tool.getId());

        assertThat(maintenanceService.isDuplicate(tool.getId(), maintenance.getMaintainerName())).isFalse();

        maintenanceService.persist(maintenance);

        assertThat(maintenanceService.isDuplicate(tool.getId(), maintenance.getMaintainerName())).isTrue();

    }

    @Test
    void doesExist() {

        assertThat(maintenanceService.doesExist(1L)).isFalse();

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.get(tool.getId());

        maintenanceService.persist(maintenance);

        assertThat(maintenanceService.doesExist(maintenance.getId())).isTrue();

    }

}
