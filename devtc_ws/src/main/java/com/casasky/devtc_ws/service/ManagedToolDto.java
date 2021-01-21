package com.casasky.devtc_ws.service;

import com.casasky.devtc_ws.entity.PackageExtension;
import lombok.Builder;


@Builder
public class ManagedToolDto {

    public String name;
    public String lastReleaseVersion;
    public String downloadUrl;
    public String packageBinaryPath;
    public PackageExtension packageExtension;

}
