package com.casasky.devtc_ws.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Maintenance;

import com.casasky.devtc_ws.entity.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class ToolUpdaterIntegrationTest extends BaseIntegrationTest {

    private ToolUpdater toolUpdater;

    @Autowired
    private MaintenanceService maintenanceService;

    @MockBean
    private ExchangeFunction exchangeFunction;


    @BeforeEach
    void setUp() {
        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).build()));

        var mockedWebClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
        toolUpdater = new ToolUpdater(mockedWebClient, maintenanceService);
    }


    @Test
    void getNextReleaseVersions() {

        assertThat(toolUpdater.computeNewReleaseVersions("15")).containsExactly("16");
        assertThat(toolUpdater.computeNewReleaseVersions("0.14.4")).containsExactlyInAnyOrder("0.14.5", "0.15.4", "1.14.4");

    }



    @Test
    void findHighestNextReleaseVersion() {

        // source
        Maintenance maintenance = MaintenanceDemo.java(1L);

        // input
        String linux = maintenance.getSupportedPlatformCodes().iterator().next();
        var updateInput = ToolUpdater.UpdateInput.builder()
                .lastReleaseVersion(maintenance.getReleaseVersion())
                .selectedPlatformCode(linux)
                .packageExtension(maintenance.getPackageExtension().getValue())
                .downloadUrlTemplate(maintenance.getDownloadUrlTemplate())
                .build();

        // expected output
        var expectedNextReleaseVersion = "16";

        assertThat(toolUpdater.findHighestNewReleaseVersion(updateInput)).isEqualTo(expectedNextReleaseVersion);

    }

    @Test
    void update() {

        // source
        var tool = new Tool("java");
        persist(tool);
        Maintenance maintenance = MaintenanceDemo.java(tool.getId());
        persist(maintenance);

        toolUpdater.update(maintenance.getId());

        var maintenanceFromDB = find(Maintenance.class, maintenance.getId());
        assertThat(maintenanceFromDB.getReleaseVersion()).isGreaterThan(maintenance.getReleaseVersion());
    }


    @Test
    void check() {

        // source
        var tool = new Tool("java");
        persist(tool);
        Maintenance maintenance = MaintenanceDemo.java(tool.getId());
        persist(maintenance);

        String newReleaseVersion = toolUpdater.check(maintenance.getId());

        assertThat(newReleaseVersion).isGreaterThan(maintenance.getReleaseVersion());

    }

}
