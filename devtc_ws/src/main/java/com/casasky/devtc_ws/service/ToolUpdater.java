package com.casasky.devtc_ws.service;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
class ToolUpdater {

    private final WebClient webClient;

    @Autowired
    private MaintenanceService maintenanceService;

    private static final String ALL_VERSION_PARTS = "\\d+";


    public ToolUpdater() {
        webClient = WebClient.create();
    }


    // Used only for mocking
    ToolUpdater(WebClient webClient, MaintenanceService maintenanceService) {
        this.webClient = webClient;
        this.maintenanceService = maintenanceService;
    }


    String findHighestNewReleaseVersion(UpdateInput updateInput) {
        String lastReleaseVersion = updateInput.lastReleaseVersion;
        Predicate<String> isValidVersion = version -> webClient.get()
                .uri(expandDownloadUrl(version, updateInput))
                .exchangeToMono(clientResponse -> clientResponse.statusCode().is2xxSuccessful() ? Mono.just(true) : Mono.just(false))
                .block();
        return computeNewReleaseVersions(lastReleaseVersion).stream()
                .filter(isValidVersion)
                .max(String::compareTo)
                .orElse(lastReleaseVersion);
    }


    Set<String> computeNewReleaseVersions(String lastReleaseVersion) {
        var nextReleaseVersions = new HashSet<String>();
        Matcher match = Pattern.compile(ALL_VERSION_PARTS).matcher(lastReleaseVersion);
        while (match.find()) {
            var sb = new StringBuilder();
            String versionPart = match.group();
            Integer nextVersionPart = Integer.parseInt(versionPart) + 1;
            int repStart = match.start();
            int repEnd = match.end();
            sb.append(lastReleaseVersion, 0, repStart);
            sb.append(nextVersionPart);
            sb.append(lastReleaseVersion, repEnd, lastReleaseVersion.length());
            nextReleaseVersions.add(sb.toString());
        }
        return nextReleaseVersions;
    }

    private String expandDownloadUrl(String version, UpdateInput updateInput) {
        UrlExpander.DownloadUrlInput downloadUrlInput = UrlExpander.DownloadUrlInput.builder()
                .releaseVersion(version)
                .selectedPlatformCode(updateInput.selectedPlatformCode)
                .packageExtension(updateInput.packageExtension)
                .downloadUrlTemplate(updateInput.downloadUrlTemplate)
                .build();
        return UrlExpander.expandDownloadUrl(downloadUrlInput);
    }


    public void updateReleaseVersion(Long id, UpdateInput updateInput) {
        String newReleaseVersion = findHighestNewReleaseVersion(updateInput);
        maintenanceService.updateReleaseVersion(id, newReleaseVersion);
    }


    @Builder
    static class UpdateInput {
        private final String lastReleaseVersion;
        private final String selectedPlatformCode;
        private final String packageExtension;
        private final String downloadUrlTemplate;
    }


}
