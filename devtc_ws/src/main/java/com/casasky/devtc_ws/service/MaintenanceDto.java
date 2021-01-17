package com.casasky.devtc_ws.service;


import java.util.Set;

import com.casasky.devtc_ws.entity.Instruction;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;
import org.hibernate.annotations.Type;

public class MaintenanceDto {

    public String maintainerName;
    public String docsUrl;
    public String downloadUrlTemplate;
    public String packageBinaryPathTemplate;
    @Type(type = "enum")
    public PackageExtension packageExtension;
    public String releaseVersion;
    public String releaseVersionFormat;
    @Type(type = "jsonb")
    public Set<String> supportedPlatformCodes;
    @Type(type = "jsonb")
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
                .releaseVersionFormat(releaseVersionFormat)
                .supportedPlatformCodes(supportedPlatformCodes)
                .instructions(instructions)
                .build();
    }

}
