insert into ERP_COMPANY (ID, VERSION, NAME, PREFIX, COMPANY_TYPE, PHONE_NUMBER, AUTO_CREATION, ACTIVITIES_TYPE_ID, TRANSPORT_SEARCH_NARROW_RADIUS, TRANSPORT_SEARCH_WIDE_RADIUS)
  values ('bceb2ae9-bc2f-49f2-9885-aa9a17980bd2', 1, 'reference_comp', 'MAA', 1, '(000)-(00)-000-00-00', true, '30c91df6-ce18-4e8b-a693-19a8a19821ba', 50, 100);

insert into ERP_EMPLOYEE (ID, VERSION, NAME, ROLE_ID, COMPANY_ID, USER_ID, IS_FREE)
  values ('47f47246-4d7a-4db0-9317-a55c9acdd0ba', 1, 'reference_employee', 'ea309298-a917-4b48-8686-85d1564436b9', 'bceb2ae9-bc2f-49f2-9885-aa9a17980bd2',
  (select ID from SEC_USER u LIMIT 1), true);

update ERP_CARGO set MANAGER_ID = '47f47246-4d7a-4db0-9317-a55c9acdd0ba' where MANAGER_ID is null ;
alter table ERP_CARGO alter column MANAGER_ID set not null ;