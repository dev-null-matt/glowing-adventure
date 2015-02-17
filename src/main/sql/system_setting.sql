--drop table system_setting;

create table SYSTEM_SETTING (
	name			varchar(64) PRIMARY KEY,
	
	string_value	varchar(256),
	int_value		integer,
	enabled			boolean
);

insert into system_setting (name,int_value) values ('ACCOUNT.MAX_CHARACTERS', 8);

select * from system_setting;
