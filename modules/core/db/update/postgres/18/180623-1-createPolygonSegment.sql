create table ERP_POLYGON_SEGMENT (
    ID uuid,
    --
    POLYGON_ID uuid not null,
    START_ID uuid not null,
    END_ID uuid not null,
    MIN_LAT double precision,
    MIN_LON double precision,
    MAX_LAT double precision,
    MAX_LON double precision,
    --
    primary key (ID)
);
