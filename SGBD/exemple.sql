-- ce fichier contient des exemples pour les syntaxes

-- creation de base de donnee
create database test 

-- suppression de base de donnees
drop database test

-- utilisation de base de donnee
use database test

-- creation table 
    -- creation de table personne 
    create table personne with idPersonne:int nom:varchar prenom:varchar
    -- insert into personne2 values idPersonne:3 nom:Ranarison prenom:Diary
    -- insert into personne2 values idPersonne:4 nom:Razama prenom:Jean

    -- -- /////////
    -- create table personne3 with idPersonne:int nom:varchar prenom:varchar
    -- insert into personne3 values idPersonne:1 nom:Razama prenom:Koto
    -- insert into personne3 values idPersonne:1 nom:Ranarison prenom:Diary
    -- insert into personne3 values idPersonne:3 nom:Randria prenom:Jean

-- suppression de table
drop table personne

-- insertion de donnees
    -- insertion dans la table personne 
    insert into personne values idPersonne:1 nom:Razanamamy prenom:Jean
    insert into personne values idPersonne:2 nom:Razaka prenom:Johany
    -- insertion dans la table emp
    insert into emp values idEmp:1 nom:Andriamanana prenom:Jean salaire:2000.500

-- selection de donnees
select * from personne where prenom = Jean
select * from personne where prenom like o -- les prenoms qui contiennent la lettre 'o'

-- suppression de donnees
    -- tout effacer
    delete from personne
    -- selectionner les lignes a effacer
    delete from personne where nom = Razaka
    delete from personne where nom like a

-- projection
select nom prenom from personne

-- jointure
    -- Donnees pour tester la jointure
    create table emp with idEmp:int nom:varchar prenom:varchar salaire:double
    insert into emp values idEmp:1 nom:Andriamanana prenom:Jean salaire:2000.500
    -- sql de test
    select * from personne emp join on prenom -- supprimeny ilay colonne prenom doublon

-- intersection 
    -- Donnees pour tester la jointure
    insert into personne values idPersonne:2 nom:Razaka prenom:Solo
    insert into personne values idPersonne:3 nom:Andriamanana prenom:Jean
    create table personne2 with idPersonne:int nom:varchar prenom:varchar
    insert into personne2 values idPersonne:1 nom:Razanamamy prenom:Jean
    insert into personne2 values idPersonne:2 nom:Razaka prenom:Solo
    -- sql de test
    select * from personne personne2 intersect

-- union (supprimeny ny lignes doublon)
    -- Donnees pour tester l'union
    create table etudiant with idEtudiant:int nom:varchar prenom:varchar
    insert into etudiant values idEtudiant:1 nom:Rason prenom:Fafa
    insert into etudiant values idEtudiant:2 nom:Randria prenom:Rene
    insert into etudiant values idEtudiant:3 nom:Andriamanana prenom:Jean
    -- sql de test
    select * from personne etudiant union

-- produit cartesien
select * from etudiant emp prod

-- soustraction 
select * from personne personne2 soustraction

-- division
    -- test pour tester la division
    create table cours with idCours:varchar nomCours:varchar
    insert into cours values idCours:c1 nomCours:Algo
    insert into cours values idCours:c2 nomCours:prog
    create table inscription with idEtudiant:int idCours:varchar
    insert into inscription values idEtudiant:2 idCours:c2
    insert into inscription values idEtudiant:1 idCours:c2
    insert into inscription values idEtudiant:1 idCours:c1
    insert into inscription values idEtudiant:3 idCours:c1
    insert into inscription values idEtudiant:2 idCours:c1
    -- sql pour tester la division
    select * from inscription cours division idCours



    select * from personne where nom = (select nom from personne2 where prenom = (select prenom from personne3))
