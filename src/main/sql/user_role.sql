--drop sequence user_role_id;
--drop table user_role;

create sequence USER_ROLE_ID;

create table USER_ROLE (
id integer PRIMARY KEY default nextval('user_role_id'),
user_id integer NOT NULL REFERENCES user_account(id),
role varchar(32) NOT NULL
);

insert into user_role (user_id,role) values (1,'USER');

select * from user_role;

select username, role from user_account u, user_role ur where u.id = ur.user_id and u.username = 'dev.null.matt';