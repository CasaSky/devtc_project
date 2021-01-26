package com.casasky.devtc_ws.service;


import static com.casasky.devtc_ws.service.UrlExpander.VAR_NAME_PACKAGE_EXTENSION;
import static com.casasky.devtc_ws.service.UrlExpander.VAR_NAME_PLATFORM_CODE;
import static com.casasky.devtc_ws.service.UrlExpander.VAR_NAME_RELEASE_VERSION;
import static java.lang.String.format;

import com.casasky.devtc_ws.entity.PackageExtension;


/**
 * Used for facilitating test variants of UrlExpanderTest
 * Notice: A and B variants are used for tests to demonstrate important differences. To consider all other correct variants see the implementation of the UrlExpander
 */
class UrlExpanderDemo {
    static final String PLATFORM_CODE = "linux";
    static final String RELEASE_VERSION = "1";
    static final String PACKAGE_EXTENSION = PackageExtension.TAR_GZ.getValue();

    static class DownloadUrlTemplate {
        private static final String BASE_URL = "https://example.com/tool";

        // https://example.com/tool/{release-version}-{platform-code}.{package-extension}
        static final String DUT_CORRECT_A = format(BASE_URL + "/{%s}-{%s}.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION);

        // https://example.com/tool/{release-version}/{release-version}-{platform-code}.{package-extension}
        static final String DUT_CORRECT_B = format(BASE_URL + "/{%s}/{%s}-{%s}.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, VAR_NAME_PACKAGE_EXTENSION);

        // https://example.com/tool/{release-version}/{platform-code}.{malformed-variable}
        static final String DUT_MALFORMED_A = format(BASE_URL + "/{%s}-{%s}.{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE, "malformed-variable");

        // https://example.com/tool/{release-version}/{platform-code}.
        static final String DUT_MALFORMED_B = format(BASE_URL + "/{%s}-{%s}", VAR_NAME_RELEASE_VERSION, VAR_NAME_PLATFORM_CODE);

        static String expectedExpandedDownloadUrlA() {
            return BASE_URL + "/" + RELEASE_VERSION + "-" + PLATFORM_CODE + "." + PACKAGE_EXTENSION;
        }

        static String expectedExpandedDownloadUrlB() {
            return BASE_URL + "/" + RELEASE_VERSION + "/" + RELEASE_VERSION + "-" + PLATFORM_CODE + "." + PACKAGE_EXTENSION;
        }

    }

    static class BinaryPathTemplate {
        private static final String BASE_BINARY_PATH = "tool";

        // tool
        static final String BPT_CORRECT_WITHOUT_TEMPLATE = BASE_BINARY_PATH;

        // tool/{release-version}
        static final String BPT_CORRECT = format(BASE_BINARY_PATH + "/{%s}", VAR_NAME_RELEASE_VERSION);

        // tool/{malformed-variable}
        static final String BPT_MALFORMED_A = format(BASE_BINARY_PATH + "/{%s}", "malformed-variable");

        // tool/{release-version}/{malformed-variable}
        static final String BPT_MALFORMED_B = format(BASE_BINARY_PATH + "/{%s}/{%s}", VAR_NAME_RELEASE_VERSION, "malformed-variable");

        static String expectedExpandedBinaryPath(boolean isTemplate) {
            return isTemplate ? BASE_BINARY_PATH + "/" + RELEASE_VERSION : BASE_BINARY_PATH;
        }
    }
}
