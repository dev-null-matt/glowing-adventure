--drop sequence quest_id;
--drop table quest;

create sequence quest_id;

create table quest (
	id			integer PRIMARY KEY default nextval('quest_id'),
	name		varchar(64),
	expiration	date
);

insert into quest (name) values ('Rail Station');

select * from quest;