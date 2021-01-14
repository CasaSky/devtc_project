package com.casasky.devtc_ws.service;


import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Maintenance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


class ToolUpdaterIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private ToolUpdater toolUpdater;


    @Test
    void findNextReleaseVersion() {

        // source
        Maintenance maintenance = MaintenanceDemo.get();

        // input
        // this part will be generated from the urlExpander via the maintenanceService
        var formattedDownloadUrl = "https://corretto.aws/downloads/latest/amazon-corretto-%s-%s-jdk.%s";
        var expectedExpandedDownloadUrl = format(formattedDownloadUrl, maintenance.getReleaseVersion(), maintenance.getSupportedPlatformCodes(), maintenance.getPackageExtension());
        // internal part
        var updateInput = ToolUpdater.UpdateInput.builder()
                .lastReleaseVersion(maintenance.getReleaseVersion())
                .releaseVersionFormat(maintenance.getReleaseVersionFormat())
                .downloadUrl(expectedExpandedDownloadUrl)
                .build();

        // expected output
        var expectedNextReleaseVersion = "16";
        when(toolUpdater.findNextReleaseVersion(updateInput)).thenReturn(expectedNextReleaseVersion);

        assertThat(toolUpdater.findNextReleaseVersion(updateInput)).isEqualTo(expectedNextReleaseVersion);

    }

}
