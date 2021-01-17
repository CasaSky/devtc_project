create role tech_user with login password 'ENCRYPTED!';

create user tech_user with password 'ENCRYPTED!' role postgres;

create database devtc_db owner tech_user;

set role tech_user;

create schema core;

create schema tools_schema;

create table tools_schema.tool
(
    id   bigserial,
    name text collate "C" not null,
    primary key (id),
    constraint const_tool_name unique (name)
);

create type tools_schema.package_extension as enum ('TAR_GZ', 'ZIP');

-- tools_schema.package_extension not null,
create table tools_schema.maintenance
(
    id                           bigserial,
    origin_id                    bigint, --references tools_schema.maintenance (id),
    tool_id                      bigint not null references tools_schema.tool (id),
    maintainer_name              text collate "C" not null,
    release_version              text collate "C" not null,
    release_version_format       text collate "C",
    supported_platform_codes     jsonb not null,
    package_binary_path_template text collate "C" not null,
    package_extension            tools_schema.package_extension not null,
    download_url_template        text collate "C" not null,
    docs_url                     text collate "C" not null,
    instructions                 jsonb,
    open_time                    timestamptz not null,
    close_time                   timestamptz,
    primary key (id),
    constraint constr_tool_maintainer unique (tool_id, maintainer_name, origin_id)
);


create table tools_schema.release
(
    id                  bigserial,
    maintenance_id      bigint not null references tools_schema.maintenance (id),
    name                text collate "C" not null,
    download_url        text collate "C" not null,
    last_sync_time      timestamptz not null,
    last_roll_back_time timestamptz,
    primary key (id),
    constraint constr_release_maintenance unique (maintenance_id, name)
)

-- later add release history?