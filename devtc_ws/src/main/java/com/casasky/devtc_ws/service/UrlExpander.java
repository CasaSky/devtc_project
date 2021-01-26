package com.casasky.devtc_ws.service;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.casasky.devtc_ws.service.exception.MalformedBinaryPathTemplateException;
import com.casasky.devtc_ws.service.exception.MalformedDownloadUrlTemplateException;
import lombok.Builder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriTemplate;


class UrlExpander {

    static final String VAR_NAME_RELEASE_VERSION = "release-version";
    static final String VAR_NAME_PLATFORM_CODE = "platform-code";
    static final String VAR_NAME_PACKAGE_EXTENSION = "package-extension";

    private static final List<String> ALL_VARIABLES = List.of(VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION);


    static String expandDownloadUrl(DownloadUrlInput downloadUrlInput) {
        UriTemplate validUriTemplate = getValidDownloadTemplate(downloadUrlInput.downloadUrlTemplate);
        return validUriTemplate.expand(
                Map.of(
                        VAR_NAME_RELEASE_VERSION, downloadUrlInput.releaseVersion,
                        VAR_NAME_PLATFORM_CODE, downloadUrlInput.selectedPlatformCode,
                        VAR_NAME_PACKAGE_EXTENSION, downloadUrlInput.packageExtension))
                .toString();
    }


    static String expandBinaryPath(BinaryPathInput binaryPathInput) {
        UriTemplate validUriTemplate = getValidBinaryPathTemplate(binaryPathInput.binaryPathTemplate);
        if (validUriTemplate.getVariableNames().isEmpty()) {
            return binaryPathInput.binaryPathTemplate;
        }
        return validUriTemplate.expand(Map.of(VAR_NAME_RELEASE_VERSION, binaryPathInput.releaseVersion))
                .toString();
    }

    private static UriTemplate getValidDownloadTemplate(String downloadTemplate) {
        if (!isValidUrl(downloadTemplate)) {
            throw new MalformedDownloadUrlTemplateException(downloadTemplate);
        }
        UriTemplate uriTemplate = new UriTemplate(downloadTemplate);
        List<String> variablesFromTemplate = uriTemplate.getVariableNames();
        if (variablesFromTemplate.stream().distinct().count() != ALL_VARIABLES.size() || !ALL_VARIABLES.containsAll(variablesFromTemplate)) {
            throw new MalformedDownloadUrlTemplateException(downloadTemplate);
        }
        return uriTemplate;
    }

    private static UriTemplate getValidBinaryPathTemplate(String binaryPathTemplate) {
        if (StringUtils.isBlank(binaryPathTemplate) || StringUtils.containsWhitespace(binaryPathTemplate)) {
            throw new MalformedBinaryPathTemplateException(binaryPathTemplate);
        }
        UriTemplate uriTemplate = new UriTemplate(binaryPathTemplate);
        List<String> variablesFromTemplate = uriTemplate.getVariableNames();
        long variablesCount = variablesFromTemplate.stream()
                .distinct()
                .count();
        if (variablesCount > 1 || (variablesCount == 1 && !variablesFromTemplate.contains(VAR_NAME_RELEASE_VERSION))) {
            throw new MalformedBinaryPathTemplateException(binaryPathTemplate);
        }
        return uriTemplate;
    }


    static boolean isValidUrl(String urlValue) {
        if (StringUtils.isBlank(urlValue) || StringUtils.containsWhitespace(urlValue)) {
            return false;
        }
        try {
            new URL(urlValue);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }


    @Builder
    static class DownloadUrlInput {
        private final String releaseVersion;
        private final String selectedPlatformCode;
        private final String packageExtension;
        private final String downloadUrlTemplate;
    }


    @Builder
    static class BinaryPathInput {
        private final String releaseVersion;
        private final String binaryPathTemplate;
    }

}
