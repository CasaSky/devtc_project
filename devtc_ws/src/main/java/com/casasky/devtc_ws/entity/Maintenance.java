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
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;


@Entity
@Table(schema = "tool_schema")
@Getter
@SuperBuilder(toBuilder = true)
public class Maintenance extends TemplateBaseEntity {

    private Long originId;
    private Long toolId;
    private String maintainerName;
    private String docsUrl;
    private String downloadUrlTemplate;     // https://corretto.aws/downloads/latest/amazon-corretto-{last-release-version}-{platform-code}-jdk.tar.gz
    private String packageBinaryPath;
    private String packageExtension;
    private String releaseVersion;
    private String releaseVersionFormat;
    @Type(type = "jsonb")
    private Set<String> supportedPlatformCodes;
    @Type(type = "jsonb")
    private Set<Instruction> instructions;
    private final ZonedDateTime openTime = TimeUtil.now();
    private ZonedDateTime closeTime;

    protected Maintenance() {
    }

    public void close() {

        closeTime = TimeUtil.now();

    }

}
