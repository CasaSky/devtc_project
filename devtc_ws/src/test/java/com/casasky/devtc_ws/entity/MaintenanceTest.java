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
        var docsUrl = "https://www.terraform.io/docs/index.html";
        // https://releases.hashicorp.com/terraform/0.14.3/terraform_0.14.3_linux_amd64.zip
        var sourceUrlTemplate= "https://releases.hashicorp.com/terraform/{release_version}/terraform_{release_version}_{platform_code}.zip";
        var releaseVersion = "0.14.3";
        var releaseVersionFormat = "//d.//d//d.//d";
        var supportedPlatformCodes = Set.of("linux_amd64");
        // https://corretto.aws/downloads/latest/amazon-corretto-15-x64-linux-jdk.tar.gz
        var sourceUrlTemplate2 = "https://corretto.aws/downloads/latest/amazon-corretto-{release_version}-{platform_code}-jdk.tar.gz";
        var releaseVersion2 = "15";
        var releaseVersionFormat2 = "";
        var supportedPlatformCodes2 = Set.of("x64-linux");
        var instructions = Set.of(Instruction.builder().description("unzip file").command("unzip terraform_{release_version}_{platform_code}.zip").build());
        var preOpenTime = TimeUtil.now();
        var maintenance = Maintenance.builder()
                .id(id)
                .originId(originId)
                .toolId(toolId)
                .maintainerName(maintainerName)
                .docsUrl(docsUrl)
                .sourceUrlTemplate(sourceUrlTemplate)
                .releaseVersion(releaseVersion)
                .releaseVersionFormat(releaseVersionFormat)
                .supportedPlatformCodes(supportedPlatformCodes)
                .instructions(instructions)
                .build();
        var postOpenTime = TimeUtil.now();

        assertThat(maintenance.getId()).isEqualTo(id);
        assertThat(maintenance.getOriginId()).isEqualTo(originId);
        assertThat(maintenance.getToolId()).isEqualTo(toolId);
        assertThat(maintenance.getMaintainerName()).isEqualTo(maintainerName);
        assertThat(maintenance.getDocsUrl()).isEqualTo(docsUrl);
        assertThat(maintenance.getOpenTime()).isBetween(preOpenTime, postOpenTime);
        assertThat(maintenance.getCloseTime()).isNull();
        assertThat(maintenance.getSourceUrlTemplate()).isEqualTo(sourceUrlTemplate);
        assertThat(maintenance.getReleaseVersion()).isEqualTo(releaseVersion);
        assertThat(maintenance.getReleaseVersionFormat()).isEqualTo(releaseVersionFormat);
        assertThat(maintenance.getSupportedPlatformCodes()).isEqualTo(supportedPlatformCodes);
        assertThat(maintenance.getInstructions()).isEqualTo(instructions);

        var preCloseTime = TimeUtil.now();
        maintenance.close();
        var postCloseTime = TimeUtil.now();

        assertThat(maintenance.getCloseTime()).isBetween(preCloseTime, postCloseTime);

    }

}
