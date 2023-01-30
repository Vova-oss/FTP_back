drop table os_refresh_token cascade;
drop table os_roles_user_entities cascade;
drop table os_role cascade;
drop table os_users cascade;
drop table os_device_info cascade;
drop table os_device cascade;
drop table os_order_device cascade;
drop table os_order cascade;
drop table os_brand cascade;
drop table os_type cascade;

drop sequence brand_seq;
drop sequence device_info_seq;
drop sequence device_seq;
drop sequence type_seq;



delete from os_type;
delete from os_brand;
delete from os_users;
delete from os_device;
delete from os_device_info;








select * from os_users;
select * from os_type;
select * from os_brand;

insert into os_brand(name, type_id) values ('Nokia', 8);