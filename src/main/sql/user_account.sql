--drop sequence user_id;
--drop table USER_ACCOUNT;

create sequence USER_ID;

create table USER_ACCOUNT (
	id 			integer PRIMARY KEY default nextval('user_id'),
	username 	varchar(32) UNIQUE NOT NULL,
	password 	varchar(256) NOT NULL,
	email 		varchar(256) UNIQUE NOT NULL,
	enabled 	boolean default(false)
);

insert into USER_ACCOUNT (username,password,email,enabled) values ('dev.null.matt',crypt('password',gen_salt('bf')),'dev.null.matt@gmail.com',true);

select * from USER_ACCOUNT;