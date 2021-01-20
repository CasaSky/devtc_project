package com.casasky.devtc_ws.service;

import java.util.Set;
import java.util.stream.Collectors;

import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.PackageExtension;
import lombok.Builder;


@Builder
public class ManagedToolDto {

    public String name;
    public String lastReleaseVersion;
    public String downloadUrl;
    public String packageBinaryPath;
    public PackageExtension packageExtension;

    public static Set<ManagedToolDto> demo() {
       return MaintenanceDemo.demo().stream()
               .map(ManagedToolDto::transform)
               .collect(Collectors.toUnmodifiableSet());
    }

    private static ManagedToolDto transform(Maintenance maintenance) {
        return ManagedToolDto.builder()
                .name(maintenance.getToolId().toString()) //TODO replace with name
                .lastReleaseVersion(maintenance.getReleaseVersion()) //TODO replace with last version
                .downloadUrl(maintenance.getDownloadUrlTemplate()) // TODO replace with expandedURL
                .packageBinaryPath(maintenance.getPackageBinaryPathTemplate()) //TODO replace with expanded binaryPath
                .packageExtension(maintenance.getPackageExtension())
                .build();
    }

}
