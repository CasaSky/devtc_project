package com.casasky.devtc_ws.entity;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.casasky.core.entity.TemplateBaseEntity;
import com.casasky.core.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(schema = "tool_schema")
@Getter
@SuperBuilder(toBuilder = true)
public class Maintenance extends TemplateBaseEntity {

    private Long originId;
    private Long toolId;
    private String maintainerName;
    private String docsUrl;
    private String sourceUrlTemplate;
    private String releaseVersion;
    private String releaseVersionFormat;
    private Set<String> supportedPlatformCodes;
    private Set<Instruction> instructions;
    private final ZonedDateTime openTime = TimeUtil.now();
    private ZonedDateTime closeTime;

    protected Maintenance() {
    }

    public void close() {

        closeTime = TimeUtil.now();

    }

}
