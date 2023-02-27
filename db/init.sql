create sequence brand_seq start 214 increment 1;
create sequence device_info_seq start 14398 increment 1;
create sequence device_seq start 2082 increment 1;
create sequence type_seq start 8 increment 1;
create table os_brand (id int8 not null, name varchar(255), type_id int8, primary key (id));
create table os_device (id int8 not null, data_of_create int8, is_name boolean, name varchar(255), path_file TEXT, price varchar(255) not null, brand_id int8, type_id int8, primary key (id));
create table os_device_info (id int8 not null, description text, title text, device_id int8, primary key (id));
create table os_order (id  bigserial not null, data_of_create int8, status varchar(255), total_sum_check int8 not null, user_id int8, primary key (id));
create table os_order_device (id  bigserial not null, amount_of_product int8 not null, device_id int8, order_id int8, primary key (id));
create table os_rating (id  bigserial not null, rate varchar(255), device_id int8, user_id int8, primary key (id));
create table os_refresh_token (id  bigserial not null, expiry_date timestamp not null, token varchar(255) not null, user_id int8, primary key (id));
create table os_role (id  bigserial not null, role varchar(255), primary key (id));
create table os_roles_user_entities (user_id int8 not null, role_id int8 not null);
create table os_type (id int8 not null, name varchar(255), primary key (id));
create table os_users (id  bigserial not null, fio varchar(255), code int4, is_man boolean, password varchar(255), telephone_number varchar(255) not null, time_of_creation int8, verification boolean, primary key (id));
alter table if exists os_device add constraint UKbj4t7p0tggpag9wcmybi3pruq unique (name);
alter table if exists os_refresh_token add constraint UK_hkuy0qblrwy6m2q69iphgw8nd unique (token);
alter table if exists os_type add constraint UKkxil4qdacayg2fdxyi31tax0g unique (name);
alter table if exists os_users add constraint UKhx7tuk6f8yxe5uxtnjb7lqd7e unique (telephone_number);
alter table if exists os_brand add constraint Ukbj4t7pOtggp8gnwcmybi3pruq unique (name, type_id);
alter table if exists os_brand add constraint FKm75en58ybxe798ayqbkjk4k7i foreign key (type_id) references os_type on delete cascade;
alter table if exists os_device add constraint FKmj22rk9rlg1p7rq5fuivj0829 foreign key (brand_id) references os_brand on delete cascade;
alter table if exists os_device add constraint FK59w5prox8vfk6ai0efh775o67 foreign key (type_id) references os_type on delete cascade;
alter table if exists os_device_info add constraint FKa1pnm5u6v8e714vnlt3jakqt9 foreign key (device_id) references os_device on delete cascade;
alter table if exists os_order add constraint FK3utlev34r2puojvcaoy4tdx1 foreign key (user_id) references os_users;
alter table if exists os_order_device add constraint FK3rb9fy0snywkyusln4ti4odwq foreign key (device_id) references os_device;
alter table if exists os_order_device add constraint FK37p1rswsbum9jpbsfcx5u5v8i foreign key (order_id) references os_order;
alter table if exists os_rating add constraint FKomedw8wlo4egsy1t0x8kvtgm2 foreign key (device_id) references os_device;
alter table if exists os_rating add constraint FK4kfx297r0link2dxqtppo6uaq foreign key (user_id) references os_users;
alter table if exists os_refresh_token add constraint FKc5eff31geb3nbm4wf3by4u5sw foreign key (user_id) references os_users;
alter table if exists os_roles_user_entities add constraint FKrlnrhrcd3ej31xm1vmrdebgg1 foreign key (role_id) references os_role;
alter table if exists os_roles_user_entities add constraint FKl32gfivab3akfy5nyfeyjlrrf foreign key (user_id) references os_users;
alter table if exists os_roles_user_entities add constraint ZKl3sdf432svbkfy5nyfeyjlrrf unique (user_id, role_id);


insert into os_role(role) values ('ADMIN');
insert into os_role(role) values ('USER');

insert into os_users (fio, password, telephone_number, verification)
VALUES ('admin', '$2a$12$tyQ6j.apGIh3f7FtmZRpFeAjkscr1hNHTmzGAa9StEpJJCdFjZPne', '+77777777777', true);

insert into os_roles_user_entities(user_id, role_id) values (1, 1);