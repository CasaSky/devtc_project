package com.casasky.devtc_ws.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.casasky.core.service.TemplateBaseService;
import com.casasky.devtc_ws.entity.Maintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MaintenanceService extends TemplateBaseService<Maintenance> {

    @Autowired
    private ToolService toolService;

    @Autowired
    private UrlExpander urlExpander;


    public MaintenanceService(EntityManager em) {
        super(em);
    }


    public void create(Long toolId, MaintenanceDto maintenance) {
        if (!toolService.doesExist(toolId)) {
            throw new ToolNotFoundException(toolId);
        }

        if (isDuplicate(toolId, maintenance.maintainerName)) {
            throw new DuplicateMaintenanceException(toolId);
        }

        save(maintenance.toEntity(toolId));
    }


    private void save(Maintenance entity) {
        super.persist(entity);
    }


    List<Maintenance> findAll() {
        return super.findAll(Maintenance.class);
    }


    Maintenance find(Long id) {
        Maintenance maintenance = find(Maintenance.class, id);
        if (maintenance == null) {
            throw new MaintenanceNotFoundException(id);
        }
        return maintenance;
    }


    public void updateReleaseVersion(Long id, String newReleaseVersion) {
        Maintenance maintenance = find(id);
        maintenance.updateReleaseVersion(newReleaseVersion);
    }


    boolean doesExist(Long id) {
        return doesExist(Maintenance.class, id);
    }


    boolean isDuplicate(Long toolId, String maintainerName) {
        return em.createQuery("select m from Maintenance m where m.toolId = :toolId and m.maintainerName = :maintainerName")
                .setParameter("toolId", toolId)
                .setParameter("maintainerName", maintainerName).getResultList().size() > 0;
    }


    public Set<ManagedToolDto> deliverToolchain(String selectedPlatform) {
        return findAll().stream()
                .filter(m -> m.getSupportedPlatformCodes().stream()
                        .anyMatch(p -> p.contains(selectedPlatform)))
                .map(m -> ManagedToolDto.builder()
                        .name(toolService.find(m.getToolId()).getName())
                        .downloadUrl(urlExpander.expandDownloadUrl(UrlExpander.DownloadUrlInput.builder()
                                .releaseVersion(m.getReleaseVersion())
                                .selectedPlatformCode(m.getSupportedPlatformCodes().stream()
                                        .filter(p -> p.contains(selectedPlatform))
                                        .findAny()
                                        .orElseThrow(() -> new RuntimeException("Could not match selected platform")))
                                .packageExtension(m.getPackageExtension().getValue())
                                .downloadUrlTemplate(m.getDownloadUrlTemplate())
                                .build()))
                        .lastReleaseVersion(m.getReleaseVersion())
                        .packageBinaryPath(urlExpander.expandBinaryPath(UrlExpander.BinaryPathInput.builder()
                                .releaseVersion(m.getReleaseVersion())
                                .binaryPathTemplate(m.getPackageBinaryPathTemplate())
                                .build()))
                        .packageExtension(m.getPackageExtension())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

}
