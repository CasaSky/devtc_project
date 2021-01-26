package com.casasky.devtc_ws.service;


import static com.casasky.devtc_ws.service.UrlExpander.*;
import static java.lang.String.format;

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
                .packageBinaryPathTemplate(format("amazon-corretto-{%s}", VAR_NAME_RELEASE_VERSION))
                .packageExtension(PackageExtension.TAR_GZ)
                .downloadUrlTemplate(format("https://corretto.aws/downloads/latest/amazon-corretto-{%s}-{%s}-jdk.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION))
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
                .downloadUrlTemplate(format("https://releases.hashicorp.com/terraform/{%s}/terraform_{%s}_{%s}.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION))
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
                .downloadUrlTemplate(format("https://releases.hashicorp.com/vault/{%s}/vault_{%s}_{%s}.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION))
                .docsUrl("https://www.vaultproject.io/docs")
                .build();
    }


    static Set<Maintenance> demo() {
       return Set.of(java(1L), terraform(2L), vault(3L));
    }




}