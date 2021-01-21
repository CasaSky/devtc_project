package com.casasky.devtc_ws.service;


import java.util.Set;

import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;


class MaintenanceDemo {

    static Maintenance java(Long toolId) {
        return Maintenance.builder()
                .maintainerName("Tester")
                .toolId(toolId)
                .releaseVersion("15")
                .supportedPlatformCodes(Set.of("x64-linux", "x64-windows"))
                .packageBinaryPathTemplate("amazon-corretto-{release-version}")
                .packageExtension(PackageExtension.TAR_GZ)
                .downloadUrlTemplate("https://corretto.aws/downloads/latest/amazon-corretto-{release-version}-{platform-code}-jdk.{package-extension}")
                .docsUrl("https://docs.oracle.com/en/java/javase/15/docs/api/index.html")
                .build();
    }


    static Maintenance terraform(Long toolId) {
        return Maintenance.builder()
                .maintainerName("Tester")
                .toolId(toolId)
                .releaseVersion("0.14.3")
                .supportedPlatformCodes(Set.of("linux_amd64", "windows_amd64"))
                .packageBinaryPathTemplate("terraform")
                .packageExtension(PackageExtension.ZIP)
                .downloadUrlTemplate("https://releases.hashicorp.com/terraform/{release-version}/terraform_{release-version}_{platform-code}.{package-extension}")
                .docsUrl("https://www.terraform.io/docs/index.html")
                .build();
    }


    static Maintenance vault(Long toolId) {
        return Maintenance.builder()
                .maintainerName("Tester")
                .toolId(toolId)
                .releaseVersion("1.6.1")
                .supportedPlatformCodes(Set.of("linux_amd64", "windows_amd64"))
                .packageBinaryPathTemplate("vault")
                .packageExtension(PackageExtension.ZIP)
                .downloadUrlTemplate("https://releases.hashicorp.com/vault/{release-version}/vault_{release-version}_{platform-code}.{package-extension}")
                .docsUrl("https://www.vaultproject.io/docs")
                .build();
    }


    static Set<Maintenance> demo() {
       return Set.of(java(1L), terraform(2L), vault(3L));
    }




}