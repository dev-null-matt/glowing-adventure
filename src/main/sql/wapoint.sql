--drop sequence waypoint_id;-
--drop table waypoint;

create sequence WAYPOINT_ID;

create table WAYPOINT (
	ID 			integer PRIMARY KEY DEFAULT nextVal('WAYPOINT_ID'),
	LATITUDE	double precision NOT NULL default 0.0,
	LONGITUDE	double precision NOT NULL default 0.0,
	DESCRIPTION	varchar(64)
);

insert into WAYPOINT (latitude,longitude,description) values 
	(44.90606367588043,-93.20197284221649,'Matts firepit'),
	(44.913933,-93.211817,'JH Stevens House'),
	(44.919902,-93.211216,'Sea Salt Restaurant');

select * from WAYPOINT;