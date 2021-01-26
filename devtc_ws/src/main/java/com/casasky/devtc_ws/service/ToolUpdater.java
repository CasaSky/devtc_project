package com.casasky.devtc_ws.service;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.service.exception.PlatformCodeNotFoundException;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class ToolUpdater {

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

    public AtomicBoolean update(Long maintenanceId) {
        Maintenance maintenance = maintenanceService.find(maintenanceId);
        AtomicBoolean updated = new AtomicBoolean(false);
        // any supported platform code is enough to process the update
        String experimentPlatformCode = maintenance.getSupportedPlatformCodes().iterator().next();
        var updateInput = updateInput(maintenance, experimentPlatformCode);
        String newReleaseVersion = findHighestNewReleaseVersion(updateInput);
        if (newReleaseVersion.compareTo(updateInput.lastReleaseVersion) > 0) {
            maintenanceService.updateReleaseVersion(maintenanceId, newReleaseVersion);
            updated.set(true);
        }
        return updated;
    }

    private UpdateInput updateInput(Maintenance maintenance, String selectedPlatformCode) {
        return UpdateInput.builder()
                .lastReleaseVersion(maintenance.getReleaseVersion())
                .selectedPlatformCode(selectedPlatformCode)
                .packageExtension(maintenance.getPackageExtension().getValue())
                .downloadUrlTemplate(maintenance.getDownloadUrlTemplate())
                .build();
    }


    public String check(Long maintenanceId) {
        Maintenance maintenance = maintenanceService.find(maintenanceId);
        // any supported platform code is enough to process the update
        String experimentPlatformCode = maintenance.getSupportedPlatformCodes().iterator().next();
        var updateInput = updateInput(maintenance, experimentPlatformCode);
        return findHighestNewReleaseVersion(updateInput);
    }


    @Builder
    static class UpdateInput {
        private final String lastReleaseVersion;
        private final String selectedPlatformCode;
        private final String packageExtension;
        private final String downloadUrlTemplate;
    }


}
