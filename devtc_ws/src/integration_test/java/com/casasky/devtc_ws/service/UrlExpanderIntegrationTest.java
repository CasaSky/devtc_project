package com.casasky.devtc_ws.service;


import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Maintenance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class UrlExpanderIntegrationTest extends BaseIntegrationTest {


    @Autowired
    private UrlExpander urlExpander;


    @Test
    void expandDownloadUrl() {

        // source
        Maintenance maintenance = MaintenanceDemo.java(1L);

        // input
        var linux = maintenance.getSupportedPlatformCodes().stream().filter(s -> s.equals("x64-linux")).findAny().orElse(null);
        var downloadUrlInput = UrlExpander.DownloadUrlInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .selectPlatformCode(linux)
                .packageExtension(maintenance.getPackageExtension().getValue())
                .downloadUrlTemplate(maintenance.getDownloadUrlTemplate())
                .build();

        // expected output
        var formattedDownloadUrl = "https://corretto.aws/downloads/latest/amazon-corretto-%s-%s-jdk.%s";
        var expectedExpandedDownloadUrl = format(formattedDownloadUrl, maintenance.getReleaseVersion(), linux, maintenance.getPackageExtension().getValue());

        String expandedDownloadUrl = urlExpander.expandDownloadUrl(downloadUrlInput);
        assertThat(expandedDownloadUrl).isEqualTo(expectedExpandedDownloadUrl);

    }


    @Test
    void expandBinaryPath() {

        // source
        Maintenance maintenance = MaintenanceDemo.java(1L);

        // input
        var binaryPathInput = UrlExpander.BinaryPathInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .binaryPathTemplate(maintenance.getPackageBinaryPathTemplate())
                .build();

        // expected output
        var formattedBinaryPath = "amazon-corretto-%s";
        var expectedExpandedBinaryPath = format(formattedBinaryPath, maintenance.getReleaseVersion());

        String expandedBinaryPath = urlExpander.expandBinaryPath(binaryPathInput);
        assertThat(expandedBinaryPath).isEqualTo(expectedExpandedBinaryPath);

    }

}
