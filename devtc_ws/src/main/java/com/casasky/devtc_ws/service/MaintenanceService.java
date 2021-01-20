package com.casasky.devtc_ws.service;


import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.casasky.core.service.TemplateBaseService;
import com.casasky.devtc_ws.entity.Maintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MaintenanceService extends TemplateBaseService<Maintenance> {

    @Autowired
    private ToolService toolService;


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


    public Set<ManagedToolDto> deliverToolchain() {
        return ManagedToolDto.demo(); //TODO implement
    }

}
