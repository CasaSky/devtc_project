package com.casasky.devtc_ws.service;


import lombok.Builder;
import org.springframework.stereotype.Component;


@Component
class ToolUpdater {

    // Todo implement me
    String findNextReleaseVersion(UpdateInput updateInput) {
        return null;
    }


    @Builder
    static class UpdateInput {
        private final String lastReleaseVersion;
        private final String releaseVersionFormat;
        private final String downloadUrl;
    }

}
