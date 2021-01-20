package com.casasky.devtc_ws.entity;


import lombok.Getter;


@Getter
public enum PackageExtension {
    TAR_GZ("tar.gz"),
    ZIP("zip");

    private final String value;

    PackageExtension(String value) {
        this.value = value;
    }

}
