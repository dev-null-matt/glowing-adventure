--drop sequence waypoint_objective_type_id;
--drop table waypoint_objective_type;

create sequence WAYPOINT_OBJECTIVE_TYPE_ID;

create table WAYPOINT_OBJECTIVE_TYPE (
	id integer 		PRIMARY KEY default nextval('waypoint_objective_type_id'),
	waypoint_id 	integer NOT NULL REFERENCES waypoint(id),
	radius 			integer not null default 15,
	objective_type	varchar(32) NOT NULL default 'QUEST_STEP'
);

insert into waypoint_objective_type (waypoint_id,radius,objective_type) values 
	(1,15,'QUEST_START'),
	(2,30,'QUEST_WAYPOINT'),
	(3,30,'QUEST_COMPLETION');

select * from waypoint_objective_type;