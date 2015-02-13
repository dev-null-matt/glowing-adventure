--drop sequence character_id;
--drop table character;

create sequence CHARACTER_ID;

create table CHARACTER (
	id 			integer PRIMARY KEY DEFAULT nextval('character_id'),
	user_id 	integer NOT NULL REFERENCES user_account(id),
	name 		varchar(64) UNIQUE NOT NULL,
	logged_in	boolean NOT NULL default false
);

insert into character (user_id,name) values (1,'Charok');

select * from character;