DROP DATABASE IF EXISTS DB_Decisions;
CREATE DATABASE IF NOT EXISTS DB_Decisions DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE DB_Decisions;
/*
DROP TABLE IF EXISTS Statistics;
#*/
CREATE TABLE IF NOT EXISTS Statistics
(	Id INT(11) NOT NULL AUTO_INCREMENT
,	Title VARCHAR(500) NOT NULL
,	Results VARCHAR(5000) NOT NULL
,	PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

INSERT INTO Statistics (Title, Results) VALUES ('Test', 'Test');