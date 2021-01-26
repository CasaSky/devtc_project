package com.casasky.devtc_ws.service;


import static java.lang.String.format;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.casasky.devtc_ws.entity.Instruction;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;
import com.casasky.devtc_ws.service.exception.GlobalRuntimeException;


public class MaintenanceDto {

    @NotEmpty
    public String maintainerName;
    @NotEmpty
    public String docsUrl;
    @NotEmpty
    public String downloadUrlTemplate;
    @NotEmpty
    public String packageBinaryPathTemplate;
    @NotNull
    public PackageExtension packageExtension;
    @NotEmpty
    public String releaseVersion;
    @NotEmpty
    public Set<String> supportedPlatformCodes;
    public Set<Instruction> instructions;


    Maintenance toEntity(Long toolId) {
        return Maintenance.builder()
                .toolId(toolId)
                .maintainerName(maintainerName)
                .docsUrl(docsUrl)
                .downloadUrlTemplate(downloadUrlTemplate)
                .packageBinaryPathTemplate(packageBinaryPathTemplate)
                .packageExtension(packageExtension)
                .releaseVersion(releaseVersion)
                .supportedPlatformCodes(supportedPlatformCodes)
                .instructions(instructions)
                .build();
    }

    void preProcess() {
        trim();
        customValidation();
    }


    private void customValidation() {
        if (!UrlExpander.isValidUrl(docsUrl)) {
            throw new GlobalRuntimeException(format("invalid docsUrl %s", docsUrl));
        }

        // ensure that the UrlExpander can deal with all delivered data, otherwise an exception will be raised, so the maintenance creation will fail
        supportedPlatformCodes.forEach(p -> UrlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                .releaseVersion(releaseVersion)
                .selectedPlatformCode(p)
                .packageExtension(packageExtension.getValue())
                .downloadUrlTemplate(downloadUrlTemplate)
                .build()));
        UrlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                .releaseVersion(releaseVersion)
                .binaryPathTemplate(packageBinaryPathTemplate)
                .build());
    }


    private void trim() {
        maintainerName = maintainerName.trim();
        docsUrl = docsUrl.trim();
        downloadUrlTemplate = downloadUrlTemplate.trim();
        packageBinaryPathTemplate = packageBinaryPathTemplate.trim();
        releaseVersion = releaseVersion.trim();
        supportedPlatformCodes = supportedPlatformCodes.stream()
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());
    }

}
