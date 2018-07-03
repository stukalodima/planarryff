create table ERP_CATEGORY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    WEIGHT double precision,
    VOLUME double precision,
    NUMBER_OF_PALLETS integer,
    COST_KILOMETER double precision,
    COST_HOUR double precision,
    COST_SUPPLY double precision,
    --
    primary key (ID)
);
