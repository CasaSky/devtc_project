package com.casasky.devtc_ws.entity;


public enum PackageExtension {
    TAR_GZ("tar.gz"),
    ZIP("zip");

    private final String name;

    PackageExtension(String name) {
        this.name = name;
    }

}
