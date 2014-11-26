--drop sequence quest_in_progress_id;
--drop table quest_in_progress;

create sequence quest_in_progress_id;

create table quest_in_progress (
	id				integer PRIMARY KEY default nextval('quest_in_progress_id'),
	character_id	integer NOT NULL REFERENCES character(id),
	quest_id		integer NOT NULL REFERENCES quest(id),
	current_step	integer NOT NULL default 1,
	tracked			boolean NOT NULL default false
);

insert into quest_in_progress (character_id,quest_id) values (1,1);

select * from quest_in_progress;