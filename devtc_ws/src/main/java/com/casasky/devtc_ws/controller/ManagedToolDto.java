package com.casasky.devtc_ws.controller;

import java.util.Set;

import lombok.Builder;


@Builder
public class ManagedToolDto {

    public String name;
    public String lastReleaseVersion;
    public String downloadUrl;
    public String packageBinaryPath;
    public String packageExtension;

    public static Set<ManagedToolDto> demo() {

        var java = ManagedToolDto.builder()
                .name("java")
                .downloadUrl("https://corretto.aws/downloads/latest/amazon-corretto-15-x64-linux-jdk.tar.gz")
                .packageBinaryPath("amazon-corretto-15.0.1.9.1-linux-x64/bin/java")
                .packageExtension("tar.gz")
                .lastReleaseVersion("15")
                .build();

        var terraform = ManagedToolDto.builder()
                .name("terraform")
                .downloadUrl("https://releases.hashicorp.com/terraform/0.14.3/terraform_0.14.3_linux_amd64.zip")
                .packageBinaryPath("terraform")
                .packageExtension("zip")
                .lastReleaseVersion("0.14.3")
                .build();

        var vault = ManagedToolDto.builder()
                .name("vault")
                .downloadUrl("https://releases.hashicorp.com/vault/1.6.1/vault_1.6.1_linux_amd64.zip")
                .packageBinaryPath("vault")
                .packageExtension("zip")
                .lastReleaseVersion("1.6.1")
                .build();

        return Set.of(java, terraform, vault);

    }

}
