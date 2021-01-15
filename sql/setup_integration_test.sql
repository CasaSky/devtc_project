create role tech_user with login password 'f,nV1D*jb8m~wSKa';

create database devtc_db_it owner tech_user;


create table core.test_dummy(
    id bigserial,
    jsonb_attribute jsonb,
    jsonb_attributes jsonb,
    primary key (id)
);