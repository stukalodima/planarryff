insert into ERP_COMPANY (ID, VERSION, NAME, COMPANY_TYPE, PHONE_NUMBER, AUTO_CREATION, ACTIVITIES_TYPE_ID) VALUES ('08e2a738-6d98-4b13-bd0e-05e6688b6a4b', 1, 'reference company', 1, '', true, '30c91df6-ce18-4e8b-a693-19a8a19821ba')^
update ERP_CARGO set COMPANY_ID = '08e2a738-6d98-4b13-bd0e-05e6688b6a4b' where COMPANY_ID is null ;
alter table ERP_CARGO alter column COMPANY_ID set not null ;
