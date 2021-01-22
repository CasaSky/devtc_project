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

    @Autowired
    private UrlExpander urlExpander;


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
                .packageBinaryPath(urlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                        .releaseVersion(javaMaintenance.getReleaseVersion())
                        .binaryPathTemplate(javaMaintenance.getPackageBinaryPathTemplate())
                        .build()))
                .packageExtension(javaMaintenance.getPackageExtension())
                .downloadUrl(urlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                        .releaseVersion(javaMaintenance.getReleaseVersion())
                        .selectedPlatformCode(javaMaintenance.getSupportedPlatformCodes().stream()
                                .filter(p -> p.contains(selectedPlatform))
                                .findAny()
                                .orElseThrow())
                        .packageExtension(javaMaintenance.getPackageExtension().getValue())
                        .downloadUrlTemplate(javaMaintenance.getDownloadUrlTemplate())
                        .build()))
                .build();
        var expectedManagedTerraform = ManagedToolDto.builder()
                .name(terraform.getName())
                .lastReleaseVersion(terraformMaintenance.getReleaseVersion())
                .packageBinaryPath(urlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                        .releaseVersion(terraformMaintenance.getReleaseVersion())
                        .binaryPathTemplate(terraformMaintenance.getPackageBinaryPathTemplate())
                        .build()))
                .packageExtension(terraformMaintenance.getPackageExtension())
                .downloadUrl(urlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                        .releaseVersion(terraformMaintenance.getReleaseVersion())
                        .selectedPlatformCode(terraformMaintenance.getSupportedPlatformCodes().stream()
                                .filter(p -> p.contains(selectedPlatform))
                                .findAny()
                                .orElseThrow())
                        .packageExtension(terraformMaintenance.getPackageExtension().getValue())
                        .downloadUrlTemplate(terraformMaintenance.getDownloadUrlTemplate())
                        .build()))
                .build();
        var expectedManagedVault = ManagedToolDto.builder()
                .name(vault.getName())
                .lastReleaseVersion(vaultMaintenance.getReleaseVersion())
                .packageBinaryPath(urlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                        .releaseVersion(vaultMaintenance.getReleaseVersion())
                        .binaryPathTemplate(vaultMaintenance.getPackageBinaryPathTemplate())
                        .build()))
                .packageExtension(vaultMaintenance.getPackageExtension())
                .downloadUrl(urlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                        .releaseVersion(vaultMaintenance.getReleaseVersion())
                        .selectedPlatformCode(vaultMaintenance.getSupportedPlatformCodes().stream()
                                .filter(p -> p.contains(selectedPlatform))
                                .findAny()
                                .orElseThrow())
                        .packageExtension(vaultMaintenance.getPackageExtension().getValue())
                        .downloadUrlTemplate(vaultMaintenance.getDownloadUrlTemplate())
                        .build()))
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
                .packageBinaryPath(urlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                        .releaseVersion(javaMaintenance.getReleaseVersion())
                        .binaryPathTemplate(javaMaintenance.getPackageBinaryPathTemplate())
                        .build()))
                .packageExtension(javaMaintenance.getPackageExtension())
                .downloadUrl(urlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                        .releaseVersion(javaMaintenance.getReleaseVersion())
                        .selectedPlatformCode(javaMaintenance.getSupportedPlatformCodes().stream()
                                .filter(p -> p.contains(selectedPlatform))
                                .findAny()
                                .orElseThrow())
                        .packageExtension(javaMaintenance.getPackageExtension().getValue())
                        .downloadUrlTemplate(javaMaintenance.getDownloadUrlTemplate())
                        .build()))
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

}
