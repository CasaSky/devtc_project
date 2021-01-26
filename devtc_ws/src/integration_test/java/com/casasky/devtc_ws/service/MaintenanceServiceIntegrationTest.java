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

        var selectedPlatform = "linux";
        Set<ManagedToolDto> all = maintenanceService.deliverToolchain(selectedPlatform);
        var expectedManagedJava = ManagedToolDto.builder()
                .name(java.getName())
                .lastReleaseVersion(javaMaintenance.getReleaseVersion())
                .packageBinaryPath(expectedExpandedBinaryPath(javaMaintenance))
                .packageExtension(javaMaintenance.getPackageExtension())
                .downloadUrl(expectedExpandedDownloadUrl(javaMaintenance, selectedPlatform))
                .build();
        var expectedManagedTerraform = ManagedToolDto.builder()
                .name(terraform.getName())
                .lastReleaseVersion(terraformMaintenance.getReleaseVersion())
                .packageBinaryPath(expectedExpandedBinaryPath(terraformMaintenance))
                .packageExtension(terraformMaintenance.getPackageExtension())
                .downloadUrl(expectedExpandedDownloadUrl(terraformMaintenance, selectedPlatform))
                .build();
        var expectedManagedVault = ManagedToolDto.builder()
                .name(vault.getName())
                .lastReleaseVersion(vaultMaintenance.getReleaseVersion())
                .packageBinaryPath(expectedExpandedBinaryPath(vaultMaintenance))
                .packageExtension(vaultMaintenance.getPackageExtension())
                .downloadUrl(expectedExpandedDownloadUrl(vaultMaintenance, selectedPlatform))
                .build();
        assertThat(all).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedManagedJava, expectedManagedTerraform, expectedManagedVault);

    }


    @Test
    void deliverTool() {
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

        var selectedPlatform = "linux";
        ManagedToolDto managedTool = maintenanceService.deliverTool("java", selectedPlatform);
        var expectedManagedTool = ManagedToolDto.builder()
                .name(java.getName())
                .lastReleaseVersion(javaMaintenance.getReleaseVersion())
                .packageBinaryPath(expectedExpandedBinaryPath(javaMaintenance))
                .packageExtension(javaMaintenance.getPackageExtension())
                .downloadUrl(expectedExpandedDownloadUrl(javaMaintenance, selectedPlatform))
                .build();

        assertThat(managedTool).usingRecursiveComparison()
                .isEqualTo(expectedManagedTool);
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


    @Test
    void findAllByToolId() {
        var java = new Tool("java");
        persist(java);

        var javaMaintenance1 = MaintenanceDemo.java(java.getId());
        maintenanceService.persist(javaMaintenance1);

        var javaMaintenance2 = MaintenanceDemo.java(java.getId());
        maintenanceService.persist(javaMaintenance2);


        List<Maintenance> all = maintenanceService.findAllByToolId(java.getId());
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(javaMaintenance1, javaMaintenance2);
    }


    /**
     * Helper Method that provides expected expanded download url based on a given maintenance and selected platform
     */
    private String expectedExpandedDownloadUrl(Maintenance maintenance, String selectedPlatform) {
        return UrlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .selectedPlatformCode(maintenance.getSupportedPlatformCodes().stream()
                        .filter(p -> p.contains(selectedPlatform))
                        .findAny()
                        .orElseThrow())
                .packageExtension(maintenance.getPackageExtension().getValue())
                .downloadUrlTemplate(maintenance.getDownloadUrlTemplate())
                .build());
    }

    /**
     * Helper Method that provides expected expanded binary path based on a given maintenance
     */
    private String expectedExpandedBinaryPath(Maintenance maintenance) {
        return UrlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .binaryPathTemplate(maintenance.getPackageBinaryPathTemplate())
                .build());
    }

}
