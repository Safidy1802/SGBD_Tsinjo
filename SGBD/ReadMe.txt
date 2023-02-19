ce fichier explique les syntaxes du SGBD 
NB:	le fichier "exemple.sql" contient des exemples pour chaque syntaxe
	le fichier a executer pour le serveur: 'server.bat'
	le fichier a executer pour le serveur: 'client.bat'
   

liste des syntaxes
_Creation base de donnees:
	syntaxe:"create database nom_base"

_suppression de base de donnees:
	syntaxe:"drop database nom_base"

_utilisation base  de donnees:
	syntaxe:"use database nom_base"

_creation table:
	_syntaxe:"create table nom_table with nom_colonne1:type_colonne1 nom_colonne2:type_colonne2 ..."
	_les types de colonne possibles:_varchar (caractere)
									_int (entier)
									_double (decimal)

_suppression de table:
	syntaxe:"drop table nom_table"

_insertion de donnees:
	_syntaxe:"insert into nom_table values nom_colonne1:value1 nom_colonne2:value2 ..."

selection de donnees:
	_syntaxe:"select * from nom_table where nom_colonne = filtre"
	_NB:les espaces avant et apres '=' sont obligatoires
	_syntaxe:"select * from nom_table where nom_colonne like filtre"

_suppression de donnees:
	_syntaxe:"delete from nom_table" (tout effacer)
	_syntaxe:"delete from nom_table where nom_colonne = value" (selectionner les lignes a effacer)
	_syntaxe:"delete from nom_table where nom_colonne like value" (selectionner les lignes a effacer)

_projection:
	_syntaxe:"select nom_colonne1 nom_colonne2 ... from nom_table"

_jointure:
	_syntaxe:"select * from nom_table1 nom_table2 join on nom_colonne"
	_NB:le nom_colonne pour faire la jointure doit etre pareil dans les deux tables
 
_intersection:
	_syntaxe:"select * from nom_table1 nom_table2 intersect"
	_NB:pour faire l'intersection de deux tables le nombre et les types des colonnes doivent etre pareil

_union:
	_syntaxe:"select * from nom_table1 nom_table2 union"
	_NB:pour faire l'union de deux tables le nombre et les types des colonnes doivent etre pareil
	_l'union supprime les lignes doublons

_produit cartesien:
	syntaxe:"select * from nom_table1 nom_table2 prod"

_soustraction:
	_syntaxe:"select * from nom_table1 nom_table2 soustraction"
	_NB:pour faire la soustraction de deux tables le nombre et les types des colonnes doivent etre pareil

_Division:
	_syntaxe"select * from nom_table1 nom_table2 division nom_colonne"

_pour quitter:
	_syntaxe:"quit"

 




	
