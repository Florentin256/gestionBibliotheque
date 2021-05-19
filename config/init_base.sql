Drop table tag;
Drop table livre;
Drop table auteur;
Drop table utilisateur;

create table auteur (
		id SERIAL PRIMARY KEY,
		nom VARCHAR(50) NOT NULL, 
		prenom VARCHAR(50) NOT NULL);

create table livre (
		id SERIAL PRIMARY KEY, 
		titre VARCHAR(50) NOT NULL, 
		date_parution DATE NOT NULL, 
		auteur INT REFERENCES auteur(id) NOT NULL);

create table utilisateur(
		id SERIAL PRIMARY KEY,
		login VARCHAR(50) NOT NULL, 
		password VARCHAR(50) NOT NULL,
		nom VARCHAR(50) NOT NULL, 
		prenom VARCHAR(50) NOT NULL);

create table tag (
		id_livre SERIAL REFERENCES livre NOT NULL, 
		libelle VARCHAR(50) NOT NULL);

insert into auteur (id, nom, prenom) values (DEFAULT, 'Campagne', 'Florentin');
insert into livre (id, titre, date_parution, auteur) values (DEFAULT, 'titre', '2021-05-03', 1);
insert into utilisateur (id, nom, prenom, login, password) values(DEFAULT, 'Campagne', 'Florentin', 'id', 'mdp');
insert into tag (id_livre, libelle) values (1, 'film');

