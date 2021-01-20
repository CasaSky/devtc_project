package com.casasky.devtc_ws.service;


import java.util.Map;

import lombok.Builder;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;


@Component
class UrlExpander {

    String expandDownloadUrl(DownloadUrlInput downloadUrlInput) {
        return new UriTemplate(downloadUrlInput.downloadUrlTemplate)
                .expand(Map.of(
                "release-version", downloadUrlInput.releaseVersion,
                "platform-code", downloadUrlInput.selectPlatformCode,
                "package-extension", downloadUrlInput.packageExtension))
                .toString();
    }


    String expandBinaryPath(BinaryPathInput binaryPathInput) {
        return new UriTemplate(binaryPathInput.binaryPathTemplate)
                .expand(Map.of("release-version", binaryPathInput.releaseVersion))
                .toString();
    }


    @Builder
    static class DownloadUrlInput {
        private final String releaseVersion;
        private final String selectPlatformCode;
        private final String packageExtension;
        private final String downloadUrlTemplate;
    }


    @Builder
    static class BinaryPathInput {
        private final String releaseVersion;
        private final String binaryPathTemplate;
    }

}
