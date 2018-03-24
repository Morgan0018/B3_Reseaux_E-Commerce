DROP TABLE intervenants CASCADE CONSTRAINTS;
DROP TABLE activites CASCADE CONSTRAINTS;

CREATE TABLE intervenants (
	IdIntervenant int,
	Nom varchar(50),
	Prenom varchar(50),
	Statut varchar(50),
	CONSTRAINT pk_intervenant PRIMARY KEY (IdIntervenant)
);

INSERT INTO intervenants VALUES (1,'Dreze','Morgan','Etudiant');

CREATE TABLE activites (
	IdActivite int,
	Nom varchar(30),
	DateActivite date,
	Description varchar(500),
	Intervenant int,
	CONSTRAINT pk_activite PRIMARY KEY (IdActivite),
	CONSTRAINT fk_intervenant FOREIGN KEY (Intervenant) REFERENCES intervenants(IdIntervenant)
);

INSERT INTO activites VALUES (1,'Quelque chose',null,null,1);
