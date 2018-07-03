create table ERP_DEPARTMENT_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DEPARTMENT_ID uuid not null,
    EMPLOYEE_ID uuid not null,
    --
    primary key (ID)
);
