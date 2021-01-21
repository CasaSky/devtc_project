package com.casasky.devtc_ws.entity;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


@Getter
public enum PackageExtension {
    TAR_GZ("tar.gz"),
    ZIP("zip");

    @JsonValue
    private final String value;

    PackageExtension(String value) {
        this.value = value;
    }

}
