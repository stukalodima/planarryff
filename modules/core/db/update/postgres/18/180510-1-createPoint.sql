create table ERP_POINT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE integer,
    ADDRESS varchar(255) not null,
    COUNTRY varchar(255) not null,
    CITY varchar(255) not null,
    STREET varchar(255),
    BUILDING varchar(255),
    LATITUDE double precision not null,
    LONGITUDE double precision not null,
    --
    primary key (ID)
);
