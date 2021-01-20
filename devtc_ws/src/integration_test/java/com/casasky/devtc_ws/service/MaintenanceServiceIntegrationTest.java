package com.casasky.devtc_ws.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

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

        var maintenance = MaintenanceDemo.java(tool.getId());
        maintenanceService.persist(maintenance);

        Maintenance maintenanceFromDB = maintenanceService.find(maintenance.getId());
        assertThat(maintenanceFromDB).usingRecursiveComparison().isEqualTo(maintenance);

    }


    @Test
    void findAll() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.java(tool.getId());
        maintenanceService.persist(maintenance);

        List<Maintenance> maintenanceFromDB = maintenanceService.findAll();
        assertThat(maintenanceFromDB).usingRecursiveFieldByFieldElementComparator().containsExactly(maintenance);

    }


    @Test
    void isDuplicate() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.java(tool.getId());

        assertThat(maintenanceService.isDuplicate(tool.getId(), maintenance.getMaintainerName())).isFalse();

        maintenanceService.persist(maintenance);

        assertThat(maintenanceService.isDuplicate(tool.getId(), maintenance.getMaintainerName())).isTrue();

    }

    @Test
    void doesExist() {

        assertThat(maintenanceService.doesExist(1L)).isFalse();

        var tool = new Tool("java");
        persist(tool);

        var maintenance = MaintenanceDemo.java(tool.getId());

        maintenanceService.persist(maintenance);

        assertThat(maintenanceService.doesExist(maintenance.getId())).isTrue();

    }

    @Test
    void deliverToolchain() {

        var java = new Tool("java");
        persist(java);

        var javaMaintenance = MaintenanceDemo.java(java.getId());
        maintenanceService.persist(javaMaintenance);

        var terraform = new Tool("terraform");
        persist(terraform);

        var terraformMaintenance = MaintenanceDemo.terraform(terraform.getId());
        maintenanceService.persist(terraformMaintenance);

        var vault = new Tool("vault");
        persist(vault);

        var vaultMaintenance = MaintenanceDemo.vault(vault.getId());
        maintenanceService.persist(vaultMaintenance);

        Set<ManagedToolDto> all = maintenanceService.deliverToolchain();
        var managedJava = ManagedToolDto.builder()
                .name(javaMaintenance.getToolId().toString()) //TODO replace with name
                .lastReleaseVersion(javaMaintenance.getReleaseVersion())
                .packageBinaryPath(javaMaintenance.getPackageBinaryPathTemplate())
                .packageExtension(javaMaintenance.getPackageExtension())
                .downloadUrl(javaMaintenance.getDownloadUrlTemplate())
                .build();
        var managedTerraform = ManagedToolDto.builder()
                .name(terraformMaintenance.getToolId().toString()) //TODO replace with name
                .lastReleaseVersion(terraformMaintenance.getReleaseVersion())
                .packageBinaryPath(terraformMaintenance.getPackageBinaryPathTemplate())
                .packageExtension(terraformMaintenance.getPackageExtension())
                .downloadUrl(terraformMaintenance.getDownloadUrlTemplate())
                .build();
        var managedVault = ManagedToolDto.builder()
                .name(vaultMaintenance.getToolId().toString()) //TODO replace with name
                .lastReleaseVersion(vaultMaintenance.getReleaseVersion())
                .packageBinaryPath(vaultMaintenance.getPackageBinaryPathTemplate())
                .packageExtension(vaultMaintenance.getPackageExtension())
                .downloadUrl(vaultMaintenance.getDownloadUrlTemplate())
                .build();
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(managedJava, managedTerraform, managedVault);

    }


    @Test
    void updateReleaseVersion() {
        var java = new Tool("java");
        persist(java);

        var javaMaintenance = MaintenanceDemo.java(java.getId());
        maintenanceService.persist(javaMaintenance);

        var newReleaseVersion = "16";
        maintenanceService.updateReleaseVersion(javaMaintenance.getId(), newReleaseVersion);

        var javaMaintenanceFromDB = maintenanceService.find(javaMaintenance.getId());
        assertThat(javaMaintenanceFromDB.getReleaseVersion()).isEqualTo(newReleaseVersion);
    }

}
