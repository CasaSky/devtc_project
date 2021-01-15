package com.casasky.devtc_ws.service;


import com.casasky.devtc_ws.entity.PackageExtension;
import lombok.Builder;
import org.springframework.stereotype.Component;


@Component
class UrlExpander {

    // Todo implement me
    String expandDownloadUrl(DownloadUrlInput downloadUrlInput) {
        return null;
    }


    // Todo implement me
    String expandBinaryPath(BinaryPathInput binaryPathInput) {
        return null;
    }


    @Builder
    static class DownloadUrlInput {
        private final String releaseVersion;
        private final String selectPlatformCode;
        private final PackageExtension packageExtension;
        private final String downloadUrlTemplate;
    }


    @Builder
    static class BinaryPathInput {
        private final String releaseVersion;
        private final String binaryPathTemplate;
    }

}
