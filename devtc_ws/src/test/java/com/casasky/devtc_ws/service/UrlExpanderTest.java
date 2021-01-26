package com.casasky.devtc_ws.service;


import static com.casasky.devtc_ws.service.UrlExpander.*;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.*;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.BinaryPathTemplate.BPT_CORRECT;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.BinaryPathTemplate.BPT_CORRECT_WITHOUT_TEMPLATE;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.BinaryPathTemplate.BPT_MALFORMED_A;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.BinaryPathTemplate.BPT_MALFORMED_B;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.BinaryPathTemplate.expectedExpandedBinaryPath;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.DUT_CORRECT_A;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.DUT_CORRECT_B;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.DUT_MALFORMED_A;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.DUT_MALFORMED_B;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.expectedExpandedDownloadUrlA;
import static com.casasky.devtc_ws.service.UrlExpanderDemo.DownloadUrlTemplate.expectedExpandedDownloadUrlB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.casasky.devtc_ws.service.exception.MalformedBinaryPathTemplateException;
import com.casasky.devtc_ws.service.exception.MalformedDownloadUrlTemplateException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class UrlExpanderTest {


    @Nested
    class DownloadUrlTemplate {
        @Test
        void correct() {
            var inputA = DownloadUrlInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .selectedPlatformCode(PLATFORM_CODE)
                    .packageExtension(PACKAGE_EXTENSION)
                    .downloadUrlTemplate(DUT_CORRECT_A)
                    .build();

            assertThat(UrlExpander.expandDownloadUrl(inputA)).isEqualTo(expectedExpandedDownloadUrlA());

            var inputB = DownloadUrlInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .selectedPlatformCode(PLATFORM_CODE)
                    .packageExtension(PACKAGE_EXTENSION)
                    .downloadUrlTemplate(DUT_CORRECT_B)
                    .build();

            assertThat(UrlExpander.expandDownloadUrl(inputB)).isEqualTo(expectedExpandedDownloadUrlB());
        }

        @Test
        void malformed() {
            var inputA = DownloadUrlInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .selectedPlatformCode(PLATFORM_CODE)
                    .packageExtension(PACKAGE_EXTENSION)
                    .downloadUrlTemplate(DUT_MALFORMED_A)
                    .build();
            assertThatThrownBy(() -> UrlExpander.expandDownloadUrl(inputA)).isInstanceOf(MalformedDownloadUrlTemplateException.class);

            var inputB = DownloadUrlInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .selectedPlatformCode(PLATFORM_CODE)
                    .packageExtension(PACKAGE_EXTENSION)
                    .downloadUrlTemplate(DUT_MALFORMED_B)
                    .build();
            assertThatThrownBy(() -> UrlExpander.expandDownloadUrl(inputB)).isInstanceOf(MalformedDownloadUrlTemplateException.class);
        }
    }

    @Nested
    class BinaryPathTemplate {
        @Test
        void correctWithoutTemplate() {
            BinaryPathInput binaryPathInput = BinaryPathInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .binaryPathTemplate(BPT_CORRECT_WITHOUT_TEMPLATE)
                    .build();

            assertThat(UrlExpander.expandBinaryPath(binaryPathInput)).isEqualTo(expectedExpandedBinaryPath(false));
        }

        @Test
        void correctWithTemplate() {
            BinaryPathInput binaryPathInput = BinaryPathInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .binaryPathTemplate(BPT_CORRECT)
                    .build();

            assertThat(UrlExpander.expandBinaryPath(binaryPathInput)).isEqualTo(expectedExpandedBinaryPath(true));
        }

        @Test
        void malformed() {
            BinaryPathInput binaryPathInputA = BinaryPathInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .binaryPathTemplate(BPT_MALFORMED_A)
                    .build();
            assertThatThrownBy(() -> UrlExpander.expandBinaryPath(binaryPathInputA)).isInstanceOf(MalformedBinaryPathTemplateException.class);

            DownloadUrlInput downloadUrlInputB = DownloadUrlInput.builder()
                    .releaseVersion(RELEASE_VERSION)
                    .selectedPlatformCode(PLATFORM_CODE)
                    .packageExtension(PACKAGE_EXTENSION)
                    .downloadUrlTemplate(BPT_MALFORMED_B)
                    .build();
            assertThatThrownBy(() -> UrlExpander.expandDownloadUrl(downloadUrlInputB)).isInstanceOf(MalformedDownloadUrlTemplateException.class);
        }
    }

    @Test
    void isValidUrl() {
        var validUrl = "https://example.com";
        var invalidUrl = "example.com";
        assertThat(UrlExpander.isValidUrl(validUrl)).isTrue();
        assertThat(UrlExpander.isValidUrl(invalidUrl)).isFalse();
    }
}
