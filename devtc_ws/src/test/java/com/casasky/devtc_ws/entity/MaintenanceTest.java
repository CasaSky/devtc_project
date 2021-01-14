package com.casasky.devtc_ws.entity;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import com.casasky.core.util.TimeUtil;
import org.junit.jupiter.api.Test;


class MaintenanceTest {

    @Test
    void construct() {

        var id = 2L;
        var originId = 1L;
        var toolId = 10L;
        var maintainerName = "test";
        var releaseVersion = "0.14.3";
        var releaseVersionFormat = "//d.//d//d.//d";
        var supportedPlatformCodes = Set.of("linux_amd64");
        var packageBinaryPathTemplate = "terraform";
        var packageExtension = "zip";
        var sourceUrlTemplate= "https://releases.hashicorp.com/terraform/{release-version}/terraform_{release-version}_{platform-code}.{package-extension}";
        var docsUrl = "https://www.terraform.io/docs/index.html";
        var instructions = Set.of(Instruction.builder().description("unzip file").command("unzip terraform_{release-version}_{platform-code}.{package-extension}").build());
        var preOpenTime = TimeUtil.now();
        var maintenance = Maintenance.builder()
                .id(id)
                .originId(originId)
                .toolId(toolId)
                .maintainerName(maintainerName)
                .releaseVersion(releaseVersion)
                .releaseVersionFormat(releaseVersionFormat)
                .supportedPlatformCodes(supportedPlatformCodes)
                .packageBinaryPathTemplate(packageBinaryPathTemplate)
                .packageExtension(packageExtension)
                .downloadUrlTemplate(sourceUrlTemplate)
                .docsUrl(docsUrl)
                .instructions(instructions)
                .build();
        var postOpenTime = TimeUtil.now();

        assertThat(maintenance.getId()).isEqualTo(id);
        assertThat(maintenance.getOriginId()).isEqualTo(originId);
        assertThat(maintenance.getToolId()).isEqualTo(toolId);
        assertThat(maintenance.getMaintainerName()).isEqualTo(maintainerName);
        assertThat(maintenance.getReleaseVersion()).isEqualTo(releaseVersion);
        assertThat(maintenance.getReleaseVersionFormat()).isEqualTo(releaseVersionFormat);
        assertThat(maintenance.getSupportedPlatformCodes()).isEqualTo(supportedPlatformCodes);
        assertThat(maintenance.getPackageBinaryPathTemplate()).isEqualTo(packageBinaryPathTemplate);
        assertThat(maintenance.getPackageExtension()).isEqualTo(packageExtension);
        assertThat(maintenance.getDownloadUrlTemplate()).isEqualTo(sourceUrlTemplate);
        assertThat(maintenance.getDocsUrl()).isEqualTo(docsUrl);
        assertThat(maintenance.getOpenTime()).isBetween(preOpenTime, postOpenTime);
        assertThat(maintenance.getCloseTime()).isNull();
        assertThat(maintenance.getInstructions()).isEqualTo(instructions);

        var preCloseTime = TimeUtil.now();
        maintenance.close();
        var postCloseTime = TimeUtil.now();

        assertThat(maintenance.getCloseTime()).isBetween(preCloseTime, postCloseTime);

    }

}