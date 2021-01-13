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
    void test() {

        var tool = new Tool("java");
        persist(tool);

        var maintenance = Maintenance.builder()
                .toolId(tool.getId())
                .maintainerName("Tester")
                .releaseVersion("15")
                .releaseVersionFormat("//d")
                .supportedPlatformCodes(Set.of("x64-linux", "x64-windows"))
                .packageBinaryPathTemplate("amazon-corretto-{release-version}")
                .packageExtension("tar.gz")
                .downloadUrlTemplate("https://corretto.aws/downloads/latest/amazon-corretto-{release-version}-{platform-code}-jdk.[package-extension]")
                .docsUrl("https://docs.oracle.com/en/java/javase/15/docs/api/index.html")
                .build();

        maintenanceService.persist(maintenance);

        List<Maintenance> all = maintenanceService.findAll(Maintenance.class);
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactly(maintenance);

    }

}
