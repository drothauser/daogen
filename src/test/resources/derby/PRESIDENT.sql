--  -------------------------------------------------- 
--  Generated by Enterprise Architect Version 11.1.1112
--  Created On : Monday, 02 February, 2015 
--  DBMS       : DB2 
--  -------------------------------------------------- 

DROP TABLE PRESIDENT
;
CREATE TABLE PRESIDENT ( 
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	firstname VARCHAR(255) NOT NULL,
	lastname VARCHAR(255) NOT NULL,
	state_id INTEGER NOT NULL,
	party_id INTEGER,
	inaugurated_year INTEGER NOT NULL
	CHECK (inaugurated_year BETWEEN 1789 AND 9999),
	years DECIMAL(3,1) NOT NULL
)
;

COMMENT ON COLUMN PRESIDENT.years
    IS 'Years in office'
;

CREATE INDEX IXFK_PRESIDENT_PARTY ON PRESIDENT
	(party_id)
;
CREATE INDEX IXFK_PRESIDENT_STATE ON PRESIDENT
	(state_id)
;
ALTER TABLE PRESIDENT ADD CONSTRAINT PK_President 
	PRIMARY KEY (id, state_id)
;


ALTER TABLE PRESIDENT ADD CONSTRAINT FK_PRESIDENT_PARTY 
	FOREIGN KEY (party_id) REFERENCES PARTY (id)
;

ALTER TABLE PRESIDENT ADD CONSTRAINT FK_PRESIDENT_STATE 
	FOREIGN KEY (state_id) REFERENCES STATE (id)
;
