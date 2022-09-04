insert into os_role(role) values ('ADMIN');
insert into os_role(role) values ('USER');

insert into os_users (fio, password, telephone_number, verification)
VALUES ('admin', '$2a$12$tyQ6j.apGIh3f7FtmZRpFeAjkscr1hNHTmzGAa9StEpJJCdFjZPne', '+77777777777', true);

insert into os_roles_user_entities(user_id, role_id) values (1, 1);