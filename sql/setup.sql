create role tech_user with login password 'ENCRYPTED!';

create user tech_user with password 'ENCRYPTED!' role postgres;

create database devtc_db owner tech_user;

set role tech_user;

create schema tool_schema;

create table tool_schema.tool
(
    id bigserial,
    name text collate "C" not null,
    primary key (id),
    constraint const_tool_name unique (name)
);

create table tool_schema.maintenance
(
    id bigserial,
    origin_maintenance_id bigint references tool_schema.maintenance (id),
    tool_id bigint not null references tool_schema.tool (id),
    maintainer_name text collate "C" not null,
    documentation_url text collate "C" not null,
    registration_time timestamptz not null,
    deregistration_time timestamptz,
    packages_source_url text collate "C" not null,
    release_format      text collate "C" not null,
    installation_steps jsonb, -- description, command
    primary key (id),
    constraint constr_tool_maintainer unique (tool_id, maintainer_name, origin_maintenance_id)
);

create table tool_schema.release
(
    id bigserial,
    maintenance_id bigint not null references tool_schema.maintenance (id),
    name text collate "C" not null,
    download_url text collate "C" not null,
    last_sync_time timestamptz not null,
    last_roll_back_time timestamptz,
    primary key (id),
    constraint constr_release_maintenance unique (maintenance_id, name)
)

-- later add release history?