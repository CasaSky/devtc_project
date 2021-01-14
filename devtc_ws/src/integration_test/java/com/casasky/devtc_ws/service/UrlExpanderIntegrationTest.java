package com.casasky.devtc_ws.service;


import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Maintenance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


class UrlExpanderIntegrationTest extends BaseIntegrationTest {


    @MockBean
    private UrlExpander urlExpander;


    @Test
    void downloadUrl() {

        // source
        Maintenance maintenance = MaintenanceDemo.get();

        // input
        var linux = maintenance.getSupportedPlatformCodes().stream().filter(s -> s.equals("x64-linux")).findAny().orElse(null);
        var downloadUrlInput = UrlExpander.DownloadUrlInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .selectPlatformCode(linux)
                .packageExtension(maintenance.getPackageExtension())
                .downloadUrlTemplate(maintenance.getDownloadUrlTemplate())
                .build();

        // expected output
        var formattedDownloadUrl = "https://corretto.aws/downloads/latest/amazon-corretto-%s-%s-jdk.%s";
        var expectedExpandedDownloadUrl = format(formattedDownloadUrl, maintenance.getReleaseVersion(), maintenance.getSupportedPlatformCodes(), maintenance.getPackageExtension());

        when(urlExpander.expandDownloadUrl(downloadUrlInput)).thenReturn(expectedExpandedDownloadUrl);

        String expandedDownloadUrl = urlExpander.expandDownloadUrl(downloadUrlInput);
        assertThat(expandedDownloadUrl).isEqualTo(expectedExpandedDownloadUrl);

    }


    @Test
    void binaryPath() {

        // source
        Maintenance maintenance = MaintenanceDemo.get();

        // input
        var binaryPathInput = UrlExpander.BinaryPathInput.builder()
                .releaseVersion(maintenance.getReleaseVersion())
                .binaryPathTemplate(maintenance.getPackageBinaryPathTemplate())
                .build();

        // expected output
        var formattedBinaryPath = "amazon-corretto-%s";
        var expectedExpandedBinaryPath = format(formattedBinaryPath, maintenance.getReleaseVersion());
        when(urlExpander.expandBinaryPath(binaryPathInput)).thenReturn(expectedExpandedBinaryPath);

        String expandedBinaryPath = urlExpander.expandBinaryPath(binaryPathInput);
        assertThat(expandedBinaryPath).isEqualTo(expectedExpandedBinaryPath);

    }

}
