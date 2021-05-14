Drop table tag;
Drop table livre;
Drop table auteur;
Drop table utilisateur;

create table auteur (id SERIAL PRIMARY KEY, nom VARCHAR(50), prenom VARCHAR(50));

create table livre (id SERIAL PRIMARY KEY, titre VARCHAR(50), date_parution DATE, auteur INT REFERENCES auteur(id));

create table utilisateur(nom VARCHAR(50), prenom VARCHAR(50), login VARCHAR(50) PRIMARY KEY, password VARCHAR(50));

create table tag (id_livre INT REFERENCES livre, libelle VARCHAR(50));

insert into auteur values (DEFAULT, 'Florentin', 'Campagne');
insert into livre values (DEFAULT, 'titre', '2021-05-03', 1);
insert into utilisateur values('Campagne', 'Florentin', 'id', 'mdp');
insert into tag values (1, 'film');

