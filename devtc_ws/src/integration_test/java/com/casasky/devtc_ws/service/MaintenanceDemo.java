package com.casasky.devtc_ws.service;


import java.util.Set;

import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;


class MaintenanceDemo {


    static Maintenance get() {
        return defaultBuilder().build();
    }


    static Maintenance get(Long toolId) {
        return defaultBuilder()
                .toolId(toolId)
                .build();
    }


    private static Maintenance.MaintenanceBuilder<?, ?> defaultBuilder() {
        return Maintenance.builder()
                .maintainerName("Tester")
                .releaseVersion("15")
                .releaseVersionFormat("//d")
                .supportedPlatformCodes(Set.of("x64-linux", "x64-windows"))
                .packageBinaryPathTemplate("amazon-corretto-{release-version}")
                .packageExtension(PackageExtension.ZIP)
                .downloadUrlTemplate("https://corretto.aws/downloads/latest/amazon-corretto-{release-version}-{platform-code}-jdk.{package-extension}")
                .docsUrl("https://docs.oracle.com/en/java/javase/15/docs/api/index.html");
    }

}