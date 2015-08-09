--drop sequence email_id;
--drop table email;

create sequence EMAIL_ID;

create table EMAIL (
	id 			integer PRIMARY KEY DEFAULT nextval('email_id'),
	recipient	integer NOT NULL REFERENCES user_account(id),
	subject		varchar(64) NOT NULL,
	body		text,
	queued_date	timestamp,
	sent_date	timestamp
);