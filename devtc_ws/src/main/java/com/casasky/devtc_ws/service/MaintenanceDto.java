package com.casasky.devtc_ws.service;


import java.util.Set;

import com.casasky.devtc_ws.entity.Instruction;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;

public class MaintenanceDto {

    public String maintainerName;
    public String docsUrl;
    public String downloadUrlTemplate;
    public String packageBinaryPathTemplate;
    public PackageExtension packageExtension;
    public String releaseVersion;
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

}
