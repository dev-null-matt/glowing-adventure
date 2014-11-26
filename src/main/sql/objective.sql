--drop sequence objective_id;
--drop table objective;

create sequence objective_id;

create table objective (
	id				integer PRIMARY KEY default nextval('objective_id'),
	waypoint_id		integer NOT NULL REFERENCES waypoint(id),
	quest_id		integer NOT NULL REFERENCES quest(id),
	quest_step		integer NOT NULL default 1,
	type			varchar(32) NOT NULL default 'QUEST_WAYPOINT'
);

insert into objective (waypoint_id, quest_id, quest_step, type) values
	(1,1,1,'QUEST_START'),
	(2,1,2,'QUEST_WAYPOINT'),
	(3,1,3,'QUEST_COMPLETION');

select * from objective;