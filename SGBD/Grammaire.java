package grammaire;

import java.io.File;
import java.io.Serializable;
import java.rmi.server.ExportException;
import java.util.Vector;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import grammaire.Mot;
import grammaire.Requete;
import dataBase.Bdd;
import dataBase.Racine;
import java.util.Scanner;
import relation.Relation;

public class Grammaire implements Serializable {
    Racine noyau;
    Bdd dataBase;
    String[] vocabulaire = new String[24];
    Requete requete;
    Relation[] tableReq;
    String[] types = new String[3];
    public Grammaire(Racine noyau){
        intitVocabulaire();
        this.noyau=noyau;
        this.types[0]="varchar";
        this.types[1]="int";
        this.types[2]="double"; 
    }
    public Racine getNoyau() {
        return noyau;
    }
    public void setNoyau(Racine noyau) {
        this.noyau = noyau;
    }
    public Grammaire(Bdd dataBase){
        intitVocabulaire();
        this.dataBase = dataBase;
    }

    public void intitVocabulaire(){
        this.vocabulaire[0]="select";
        this.vocabulaire[1]="*";
        this.vocabulaire[2]="from";
        this.vocabulaire[3]="prod";
        this.vocabulaire[4]="join";
        this.vocabulaire[5]="union";
        this.vocabulaire[6]="intersect";
        this.vocabulaire[7]="soustraction";
        this.vocabulaire[8]="distinct";
        this.vocabulaire[9]="division";
        this.vocabulaire[10]="on";
        this.vocabulaire[11]="where";
        this.vocabulaire[12]="=";
        this.vocabulaire[13]="like";
        this.vocabulaire[14]="create";
        this.vocabulaire[15]="database";
        this.vocabulaire[16]="use";
        this.vocabulaire[17]="table";
        this.vocabulaire[18]="with";
        this.vocabulaire[19]="insert";
        this.vocabulaire[20]="into";
        this.vocabulaire[21]="values";
        this.vocabulaire[22]="delete";
        this.vocabulaire[23]="drop";
    }
    public String[] getTypes() {
        return types;
    }
    public void setTypes(String[] types) {
        this.types = types;
    }
    public String[] getVocabulaire() {
        return vocabulaire;
    }
    public Bdd getData() {
        return dataBase;
    }
    public void setData(Bdd data) {
        this.dataBase = data;
    }
    public Requete getRequete() {
        return requete;
    }
    public void setRequete(Requete requete) {
        this.requete = requete;
    }
    public Relation[] getTableReq() {
        return tableReq;
    }
    public void setTableReq(Relation[] tableReq) {
        this.tableReq = tableReq;
    }
    public void initTableReq(Vector nomTable) throws Exception {
        this.tableReq = new Relation[nomTable.size()];
        for(int i=0;i<nomTable.size();i++){
            // System.out.println(nomTable.get(i));
            try {
                this.tableReq[i] = this.dataBase.getRelation(String.valueOf(nomTable.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            
        }
    }
    public void addlistInVector(Vector splitedNomCol,String[] list){
        for(int i=0;i<list.length;i++){
            splitedNomCol.add(list[i]);
        }
    }
    public void trimer(String[] list){
        for(int i=0;i<list.length;i++){
            list[i] = list[i].trim();
        }
    }
    public Vector dashSplit(String list){
        Vector splitedNomCol = new Vector();
        String[] splited = list.split(",");
        this.trimer(splited);
        this.addlistInVector(splitedNomCol,splited);
        return splitedNomCol;
    }

    public void addColName(Vector allColName,String[] colName){
        for(int i=0;i<colName.length;i++){
            allColName.add(colName[i]);
        }
    }

    public Vector getAllColName(){
        Vector allColName = new Vector();
        for(int i=0;i<this.tableReq.length;i++){
            this.addColName(allColName, this.tableReq[i].get(0).getAllNomCol());
        }
        return allColName;
    }

    public boolean checkNomCol(String nomCol){
        Vector allColName = this.getAllColName();
        for(int i=0;i<allColName.size();i++){
            if(nomCol.compareToIgnoreCase(String.valueOf(allColName.get(i))) == 0){
                return true;
            }
        }
        return false;
    }
    public boolean checkListNomCol(Vector argsNomCol) throws Exception {
        for(int i=0;i<argsNomCol.size();i++){
            // System.out.println(argsNomCol.get(i));
            if(this.checkNomCol(String.valueOf(argsNomCol.get(i))) == false){
                throw new Exception("colonne '"+ String.valueOf(argsNomCol.get(i))+"' inexistante"); 
            }
        }
        return true;
    }
    
    public boolean checkVocabulaire(String mot){
        for(int i=0;i<this.vocabulaire.length;i++){
            if(mot.compareToIgnoreCase(this.vocabulaire[i]) == 0){
                return true;
            }
        }
        return false;
    }
    public boolean checkTableName(String tableName){
        for(int i=0;i<this.dataBase.getListRelations().size();i++){
            if(tableName.compareToIgnoreCase(String.valueOf(this.dataBase.getListRelations().get(i))) == 0){
                return true;
            }
        }
        return false;
    }
    public Vector getTableName(Mot preTable)throws Exception{
        Vector nomTable = new Vector();
        if(preTable.getArgs().isEmpty() == false){
            // System.out.println("niditra");
            for(int i=0;i<preTable.getArgs().size();i++){
                if(this.checkTableName(preTable.getArg(i)) == true){
                    nomTable.add(preTable.getArg(i));
                } else {
                    throw new Exception("ERREUR: table '"+preTable.getArg(i)+"' inexistante");
                }
            }
        } else {
            throw new Exception("ERREUR: requete incomplete 'nom de table manquant'");
        }
        return nomTable; 
    }
    public boolean checkSelectionValue(Mot like) throws Exception {
        if(like.getArgs().isEmpty() == true && this.getRequete().getEst_imbrique() == false){
            throw new Exception("ERREUR: arguments manquantes pour la selection");
        } else {
            if(like.getNext() == null){
                return true;
            } else {
                throw new Exception("ERREUR: pres de '"+like.getNext().getSyntaxe()+"'");
            }
        }
    }
    public boolean checkWhereNext(Mot where) throws Exception {
        if(where.getArgs().isEmpty() == true){
            throw new Exception("ERREUR: nom de colonne manquante pour la selection");
        } else {
            if(where.getArgs().size() > 1){
                throw new Exception("ERREUR: trop d'arguments pour la selection");
            } else {
                if(this.checkNomCol(where.getArg(0)) == false){
                    throw new Exception("ERREUR: nom de colonne innexistante pour la selection");
                } else {
                    if(where.getNext().getSyntaxe().compareToIgnoreCase("=") != 0 && where.getNext().getSyntaxe().compareToIgnoreCase("like") != 0){
                        throw new Exception("ERREUR: Syntaxe manquante: 'like' ou '='");
                    } else {
                        return this.checkSelectionValue(where.getNext());
                    }    
                }    
            }
        }
    }
    public boolean checkJoinNext(Mot join) throws Exception {
        if(join.getNext().getSyntaxe().compareToIgnoreCase("on") != 0){
            throw new Exception("ERREUR: Syntaxe manquante: 'on'");
        }
        Mot on = join.getNext();
        if(on.getArgs().isEmpty() == true){
            throw new Exception("ERREUR: nom de colonne absente pour la jointure");
        } else {
            if(on.getArgs().size() > 1){
                throw new Exception("ERREUR: trop d'arguments pour la jointure");
            }
            else if(this.checkNomCol(on.getArg(0)) == false){
                throw new Exception("ERREUR: nom de colonne '"+on.getArg(0)+"' innexistante pour la jointure");
            } else {
                if(on.getNext() != null){
                    if(on.getNext().getSyntaxe().compareToIgnoreCase("where") != 0){
                        throw new Exception("ERREUR: placement incorrect de '"+on.getNext().getSyntaxe()+"'");
                    } else {
                        return this.checkWhereNext(on.getNext());
                    }
                }
                return true;
            }
        }
    }
    public boolean checkDivisionNext(Mot division) throws Exception {
        if(division.getArgs().isEmpty() == true){
            throw new Exception("ERREUR: nom de colonne absente pour la division");
        } else {
            if(division.getArgs().size() > 1){
                throw new Exception("ERREUR: trop d'argument pour la division");
            }
            else if(this.checkNomCol(division.getArg(0)) == false){
                throw new Exception("ERREUR: nom de colonne '"+division.getArg(0)+"' innexistante pour la division");
            } else {
                if(division.getNext() != null){
                    if(division.getNext().getSyntaxe().compareToIgnoreCase("where") != 0){
                        throw new Exception("ERREUR: placement incorrect de '"+division.getNext().getSyntaxe()+"'");
                    } else {
                        return this.checkWhereNext(division.getNext());
                    }
                }
                return true;
            }
        }
    }
    public boolean checkFunction(Mot fonction) throws Exception {
        if(fonction.getSyntaxe().compareToIgnoreCase("prod") != 0 && fonction.getSyntaxe().compareToIgnoreCase("union") != 0 && fonction.getSyntaxe().compareToIgnoreCase("intersect") != 0 && fonction.getSyntaxe().compareToIgnoreCase("soustraction") != 0  && fonction.getSyntaxe().compareToIgnoreCase("distinct") != 0){
            return false;
        }
        Mot from = fonction.getPrev();
        if(fonction.getSyntaxe().compareToIgnoreCase("distinct") == 0){
            if(from.getArgs().size() != 1){
                throw new Exception("ERREUR: nombres de tables invalides pour la fonction:'"+fonction.getSyntaxe()+"'");
            }
        } else {
            if(from.getArgs().size() != 2){
                throw new Exception("ERREUR: nombres de tables invalides pour la fonction:'"+fonction.getSyntaxe()+"'");
            }
            return true;
        }
        return true;
    }
    public boolean checkFromNext(Mot from) throws Exception {
        if(from.getNext() != null){
            if(from.getNext().getSyntaxe().compareToIgnoreCase("where") == 0){
                return this.checkWhereNext(from.getNext());
            }
            else if(from.getNext().getSyntaxe().compareToIgnoreCase("join") == 0){
                return this.checkJoinNext(from.getNext());
            } 
            else if(from.getNext().getSyntaxe().compareToIgnoreCase("division") == 0){
                return this.checkDivisionNext(from.getNext());
            }
            else if(this.checkFunction(from.getNext()) == true ){
                return true;
            } else {
                throw new Exception("ERREUR: placement incorrect de '"+from.getNext().getSyntaxe()+"'");
            }
        } else {
            if(from.getArgs().size() > 1){
                throw new Exception("ERRERU: trop d'argument pour 'from'");
            }
            from.setAction("getRelation");
            return true;
        }
        
    }
    public boolean checkUntilFrom() throws Exception {
        Mot select = this.requete.getMot(0);
            Mot from;
            if(select.getArgs().isEmpty() == false){
                for(int i=0;i<select.getArgs().size();i++){
                    try {
                        this.checkListNomCol(select.getArgs());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
                if(select.getNext().getSyntaxe().compareTo("from") != 0){
                    throw new Exception("ERREUR: syntaxe manquante: 'from'");
                }
                select.setAction("projection");
                from = select.getNext();
                return this.checkFromNext(from);
            } else {
                Mot all = select.getNext();
                if(all.getSyntaxe().compareToIgnoreCase("*") != 0){
                    throw new Exception("ERREUR: placement incorrect de '"+all.getSyntaxe()+"'");
                }
                if(all.getArgs().isEmpty() == false){
                    throw new Exception("ERREUR: 'from' attendue a la place de '"+all.getArg(0)+"'");
                }
                if(all.getNext().getSyntaxe().compareToIgnoreCase("from") != 0){
                    throw new Exception("ERREUR: syntaxe manquante 'from'");
                }
                from = all.getNext();
                return this.checkFromNext(from);
            }
    }

    public boolean checkCreationBddRequest(Mot database) throws Exception {
        if(database.getNext() != null){   
            throw new Exception("ERREUR: pres de '"+database.getNext().getSyntaxe()+"'");
        }
        if(database.getArgs().isEmpty() == true){
            throw new Exception("ERREUR: nom de base de donnee manquante");
        }
        return true;
    }
    
    public boolean checkType(String type){
        for(int i=0;i<this.getTypes().length;i++){
            if(type.compareToIgnoreCase(this.getTypes()[i]) == 0){
                return true;
            }
        }
        return false;
    }
    public boolean checkColumns(Mot with) throws Exception {
        for(int i=0;i<with.getArgs().size();i++){
            String arg = String.valueOf(with.getArg(i));
            if(arg.contains(":") == false){
                throw new Exception("ERREUR: pres de '"+arg+"'");
            }
            String[] args = arg.split(":");
            String nomCol = args[0];
            String type = args[1];
            if(checkType(type) == false){
                throw new Exception("ERREUR: type inconnue '"+type+"'");
            }
        }
        return true;
    }
    public boolean checkNbrCol(Vector columns) throws Exception {
        if(this.getTableReq()[0].get(0).getFieldNumber() == columns.size()){
            return true;
        } else {
            throw new Exception("ERREUR: nombre de colonne incompatible");
        }
    }
    public boolean checkNomColInsert(Mot values){
        for(int i=0;i<values.getArgs().size();i++){
            if(values.getArg(i).compareToIgnoreCase(this.tableReq[0].get(0).getNomCol(i)) != 0){
                return false;
            }
        }
        return true;
    }
    public boolean checkTypeValue(Mot values) throws Exception {
        for(int i=0;i<values.getArgs().size();i++){
            if(values.getArg(i).contains(":") == true){
                String type = this.getTableReq()[0].get(0).getType(i);
                String value =  values.getArg(i).split(":")[1];
                if(type.compareToIgnoreCase("int") == 0){
                    if(value.contains(".") == false){ 
                        try {
                            int val = Integer.parseInt(value);
                        } catch (Exception e) {
                            // TODO: handle exception
                            throw new Exception("ERREUR: "+value+" n'est pas un nombre");
                        }
                    } else {
                        throw new Exception("ERREUR: de type:'"+value+"' doit etre int");
                    }
                }
                if(type.compareToIgnoreCase("double") == 0){
                    try {
                        double val = Double.parseDouble(value);
                    } catch (Exception e) {
                        // TODO: handle exception
                        throw new Exception("ERREUR: "+value+" n'est pas un nombre");
                    }
                }
            } else {
                throw new Exception("ERREUR: pres de '"+values.getArg(i)+"'");
            }
        }
        return true;
    }
    public boolean checkCreationTableRequest(Mot table) throws Exception {
        if(table.getArgs().isEmpty() == true){
            throw new Exception("nom de table manquante");
        } else {
            if(table.getArgs().size() == 1){
                if(table.getNext() != null){
                    if(table.getNext().getSyntaxe().compareToIgnoreCase("with") == 0){
                        Mot with = table.getNext();
                        if(with.getArgs().isEmpty() == false){
                            if(with.getNext() == null){
                                return checkColumns(with);
                            } else {
                                throw new Exception("ERREUR: pres de '"+with.getNext().getSyntaxe()+"'");
                            } 
                        } else {
                            throw new Exception("ERREUR: pres de '"+with.getSyntaxe()+"'");
                        }
                    } else {
                        throw new Exception("ERREUR: pres de'"+table.getNext().getSyntaxe()+"'");
                    }
                } else {
                    throw new Exception("ERREUR: syntaxe incomplete");
                }
            } else {
                throw new Exception("ERREUR: trop d'argument pour le nom de table");
            }
        }
    }
    public boolean checkRequest() throws Exception {
        // select
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("select") == 0){
            return this.checkUntilFrom();
        }
        // create
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("create") == 0){
            if(this.requete.getMot(0).getArgs().isEmpty() == false){
                throw new Exception("ERREUR: pres de:'"+this.requete.getMot(0).getArg(0)+"'");
            }
            if(this.requete.getMots().size() < 2){
                throw new Exception("syntaxe invalide");
            }
            if(this.requete.getMot(1).getSyntaxe().compareToIgnoreCase("database") == 0){
                if(this.checkCreationBddRequest(this.requete.getMot(1)) == true){
                    return true;
                }
            } 
            if(this.requete.getMot(1).getSyntaxe().compareToIgnoreCase("table") == 0){
                if(checkCreationTableRequest(this.requete.getMot(1)) == true){
                    return true;
                }
            }
            throw new Exception("ERREUR: pres de:'"+this.requete.getMot(1).getArg(0)+"'");
        }
        // use
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("use") == 0){
            if(this.requete.getMot(0).getArgs().isEmpty() == false){
                throw new Exception("ERREUR: pres de:'"+this.requete.getMot(0).getArg(0)+"'");
            }
            if(this.requete.getMots().size() < 2){
                throw new Exception("ERREUR: syntaxe invalide");
            }
            if(this.requete.getMot(1).getSyntaxe().compareToIgnoreCase("database") == 0){
                if(this.checkCreationBddRequest(this.requete.getMot(1)) == true){
                    return true;
                }
            } else {
                throw new Exception("ERREUR: pres de:'"+this.requete.getMot(1).getArg(0)+"'");
            }
        }
        // insert 
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("insert") == 0){
            Mot insert = this.requete.getMot(0);
            if(insert.getArgs().isEmpty() == true){
                if(insert.getNext().getSyntaxe().compareToIgnoreCase("into") == 0){
                    Mot into = insert.getNext();
                    if(into.getArgs().size() == 1){
                        if(into.getNext() != null){
                            if(into.getNext().getSyntaxe().compareToIgnoreCase("values") == 0){
                                Mot values = into.getNext();
                                if(values.getArgs().isEmpty() == false){
                                    if(values.getNext() == null){
                                        Vector tableName = this.getTableName(into);
                                        this.initTableReq(tableName);
                                        this.checkNbrCol(values.getArgs());
                                        this.checkNomColInsert(values);
                                        this.checkTypeValue(values);
                                        return true;
                                    } else {
                                        throw new Exception("ERREUR: pres de '"+values.getNext().getSyntaxe()+"'");
                                    }
                                } else {
                                    throw new Exception("ERREUR: pres de 'values'");
                                }
                            } else {
                                throw new Exception("ERREUR: syntaxe manquante 'values'");
                            }
                        } else {
                            throw new Exception("ERREUR: synataxe incomplete");
                        }
                    } else {
                        throw new Exception("ERREUR: pres de 'into'");
                    }
                } else {
                    throw new Exception("ERREUR: pres de '"+insert.getNext().getSyntaxe()+"'");
                }
            } else {
                throw new Exception("ERREUR: pres de '"+insert.getArg(0)+"'");
            }
        }
        // delete
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("delete") == 0){
            Mot delete = this.requete.getMot(0);
            if(delete.getArgs().isEmpty()){
                if(delete.getNext() != null){
                    if(delete.getNext().getSyntaxe().compareToIgnoreCase("from") == 0){
                        Mot from = this.getRequete().getMot(1);
                        if(from.getArgs().size() == 1){
                            if(from.getNext() != null){
                                if(from.getNext().getSyntaxe().compareToIgnoreCase("where") == 0){
                                    Mot where = from.getNext();
                                    checkWhereNext(where);
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        } else {
                            throw new Exception("ERREUR: pres de 'from'");
                        }
                    } else {
                        throw new Exception("ERREUR: pres de '"+delete.getNext().getSyntaxe()+"'");
                    }
                } else {
                    throw new Exception("ERREUR: syntaxe manquante 'from'");
                }
                
            } else {
                throw new Exception("ERREUR: pres de 'delete "+delete.getArg(0)+"'");
            }
        }
        // drop
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("drop") == 0){
            Mot drop = this.requete.getMot(0);
            if(drop.getArgs().isEmpty()){
                if(drop.getNext() != null){
                    if(drop.getNext().getSyntaxe().compareToIgnoreCase("table") == 0){
                        Mot table = drop.getNext();
                        if(table.getArgs().size() == 1){
                            if(table.getNext() == null){
                                if(this.getData().checkRelationExistence(table.getArg(0)) == true){
                                    return true;
                                } else {
                                    throw new Exception("ERREUR: table '"+table.getArg(0)+"' inexistante");
                                }
                            } else {
                                throw new Exception("ERREUR: pres de '"+table.getNext().getSyntaxe()+"'");
                            }
                        } else {
                            throw new Exception("ERREUR: pres de 'table'");
                        }
                    }
                    else if(drop.getNext().getSyntaxe().compareToIgnoreCase("database") == 0){
                        Mot database = drop.getNext();
                        if(database.getArgs().size() == 1){
                            if(database.getNext() == null){
                                if(this.getNoyau().checkBDExistence(database.getArg(0)) == true){
                                    return true;
                                } else {
                                    throw new Exception("ERREUR: base '"+database.getArg(0)+"' inexistante");
                                }
                            } else {
                                throw new Exception("ERREUR: pres de '"+database.getNext().getSyntaxe()+"'");
                            }
                        } else {
                            throw new Exception("ERREUR: pres de 'database'");
                        }

                    } else {
                        throw new Exception("ERREUR: syntaxe incorrect '"+drop.getNext().getSyntaxe()+"'");
                    }
                } else {
                    throw new Exception("ERREUR: syntaxe incomplete");
                }
            } else {
                throw new Exception("ERREUR: pres de 'delete "+drop.getArg(0)+"'");
            }
        }

        return false;
    }
    public void checkAction(){
        for(int i=0;i<this.requete.getMots().size();i++){
            if(this.requete.getMot(i).getAction() != null){
                System.out.println(this.requete.getMot(i).getAction());
            }
        }
    }
    public boolean checkInitialRequest() throws Exception {
        if(this.requete.getMots().size() < 2){
            throw new Exception("ERREUR: aucunne base selectionne");
        }
        if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("create") == 0 || this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("use") == 0){
            if(this.requete.getMot(1) != null){
                if(this.requete.getMot(1).getSyntaxe().compareToIgnoreCase("database") == 0){
                    if(this.requete.getMot(1).getNext() == null){
                        return true;
                    } else {
                        throw new Exception("ERREUR: pres de '"+this.requete.getMot(2).getSyntaxe()+"'");
                    }
                } else {
                    throw new Exception("ERREUR: pres de '"+this.requete.getMot(1).getSyntaxe()+"'");
                }
            } else {
                throw new Exception("ERREUR: syntaxe incomplet");
            }
        } else if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("drop") == 0){
            Mot drop = this.requete.getMot(0);
            if(drop.getArgs().isEmpty()){
                if(drop.getNext() != null){
                    if(drop.getNext().getSyntaxe().compareToIgnoreCase("database") == 0){
                        Mot database = drop.getNext();
                        if(database.getArgs().size() == 1){
                            if(database.getNext() == null){
                                if(this.getNoyau().checkBDExistence(database.getArg(0)) == true){
                                    return true;
                                } else {
                                    throw new Exception("ERREUR: base '"+database.getArg(0)+"' inexistante");
                                }
                            } else {
                                throw new Exception("ERREUR: pres de '"+database.getNext().getSyntaxe()+"'");
                            }
                        } else {
                            throw new Exception("ERREUR: pres de 'database'");
                        }
                    } else {
                        throw new Exception("ERREUR: syntaxe incorrect '"+drop.getNext().getSyntaxe()+"'");
                    }
                } else {
                    throw new Exception("ERREUR: syntaxe incomplet");
                }
            } else {
                throw new Exception("ERREUR: pres de '"+drop.getArg(0)+"'");
            }
        } else {
            throw new Exception("ERREUR: aucunne base selectionne");
        }
    }
    // public createRequest(Vector request){
    //     Requete req = new Requete();
    //     int nbMot = 0;
    //         for(int i=0;i<request.size();i++) {
    //             boolean check = this.checkVocabulaire(String.valueOf(request.get(i)));
    //             if(check == true){
    //                 Mot mot = new Mot(String.valueOf(request.get(i)));
    //                 if(i != 0){
    //                     mot.setPrev(req.getMot(nbMot-1));
    //                     req.getMot(nbMot-1).setNext(mot);
    //                 }
    //                 req.addMot(mot); 
    //                 nbMot++;
    //             } else {
    //                 if(i-1 >= 0){
    //                     req.getMot(nbMot-1).addArg(String.valueOf(request.get(i)));
    //                 } else {
    //                     throw new Exception("ERREUR: syntaxe inconnue: '"+request.get(i)+"'");
    //                 }
    //             }
    //         }
    // }
    public Relation traitementReq(Vector request) throws Exception {
        //  identifie les mots cles
        try {
            this.requete=new Requete();
            int nbMot = 0;
            for(int i=0;i<request.size();i++) {
                boolean check = this.checkVocabulaire(String.valueOf(request.get(i)));
                if(check == true){
                    Mot mot = new Mot(String.valueOf(request.get(i)));
                    if(i != 0){
                        mot.setPrev(this.requete.getMot(nbMot-1));
                        this.requete.getMot(nbMot-1).setNext(mot);
                    }
                    this.requete.addMot(mot); 
                    nbMot++;
                } else {
                    if(i-1 >= 0){
                        this.requete.getMot(nbMot-1).addArg(String.valueOf(request.get(i)));
                    } else {
                        throw new Exception("ERREUR: syntaxe inconnue: '"+request.get(i)+"'");
                    }
                }
            }
            
            // Raha mbola tsy nisafidy base ny client
            if(this.getData() == null){
                this.checkInitialRequest();
                this.requete.initAction();
                String message = this.requete.initializing(this.noyau);
                throw new Exception(message);
            } else {
                if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("select") == 0 || this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("delete") == 0){
                    Mot from = this.requete.getMot("from");
                    // requete mireturn relation
                    if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("select") == 0){
                        if(from != null){
                            Vector tableName = this.getTableName(from);
                            this.initTableReq(tableName);
                    
                            if(this.checkRequest() == false){
                                throw new Exception("ERREUR: Syntaxe incorrect");
                            }
                            this.requete.initAction();                                          //  mameno ny action tokony atao
                            Relation result = this.requete.query(tableReq);
                            if(result.isEmpty()){
                                throw new Exception("aucunne ligne selectionne");
                            } else {
                                return result;
                            }   
                        }
                    } 
                    // tsy mireturn relation fa mila table anaovana operation
                    else 
                    {
                        if(from != null){
                            Vector tableName = this.getTableName(from);
                            this.initTableReq(tableName);
                    
                            if(this.checkRequest() == false){
                                throw new Exception("ERREUR: Syntaxe incorrect");
                            }
                            this.requete.initAction();                                          //  mameno ny action tokony atao                
                            this.requete.query(tableReq);
                            throw new Exception("ok");
                        }   
                    }
                }
                // requete tsy mireturn relation 
                else 
                {
                    if(this.checkRequest() == false){
                        throw new Exception("ERREUR: Syntaxe incorrect");
                    }
                    this.requete.initAction();                                                  //  mameno ny action tokony atao  
                    String message = this.requete.exec(noyau);
                    throw new Exception(message);
                }
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }
    public Relation traitementReq(Vector request,Relation temp) throws Exception {
        //  identifie les mots cles
        try {
            this.requete=new Requete();
            this.requete.setEst_imbrique(true);
            int nbMot = 0;
            for(int i=0;i<request.size();i++) {
                boolean check = this.checkVocabulaire(String.valueOf(request.get(i)));
                if(check == true){
                    Mot mot = new Mot(String.valueOf(request.get(i)));
                    if(i != 0){
                        mot.setPrev(this.requete.getMot(nbMot-1));
                        this.requete.getMot(nbMot-1).setNext(mot);
                    }
                    this.requete.addMot(mot); 
                    nbMot++;
                } else {
                    if(i-1 >= 0){
                        this.requete.getMot(nbMot-1).addArg(String.valueOf(request.get(i)));
                    } else {
                        throw new Exception("ERREUR: syntaxe inconnue: '"+request.get(i)+"'");
                    }
                }
            }
            
            // Raha mbola tsy nisafidy base ny client
            if(this.getData() == null){
                this.checkInitialRequest();
                this.requete.initAction();
                String message = this.requete.initializing(this.noyau);
                throw new Exception(message);
            } else {
                if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("select") == 0 || this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("delete") == 0){
                    Mot from = this.requete.getMot("from");
                    // requete mireturn relation
                    if(this.requete.getMot(0).getSyntaxe().compareToIgnoreCase("select") == 0){
                        if(from != null){
                            Vector tableName = this.getTableName(from);
                            this.initTableReq(tableName);
                    
                            if(this.checkRequest() == false){
                                throw new Exception("ERREUR: Syntaxe incorrect");
                            }
                            this.requete.initAction();                                          //  mameno ny action tokony atao
                            Relation result = this.requete.query(tableReq,temp);
                            if(result.isEmpty()){
                                throw new Exception("aucunne ligne selectionne");
                            } else {
                                return result;
                            }   
                        }
                    } 
                    // tsy mireturn relation fa mila table anaovana operation
                    else 
                    {
                        if(from != null){
                            Vector tableName = this.getTableName(from);
                            this.initTableReq(tableName);
                    
                            if(this.checkRequest() == false){
                                throw new Exception("ERREUR: Syntaxe incorrect");
                            }
                            this.requete.initAction();                                          //  mameno ny action tokony atao                
                            this.requete.query(tableReq,temp);
                            throw new Exception("ok");
                        }   
                    }
                }
                // requete tsy mireturn relation 
                else 
                {
                    if(this.checkRequest() == false){
                        throw new Exception("ERREUR: Syntaxe incorrect");
                    }
                    this.requete.initAction();                                                  //  mameno ny action tokony atao  
                    String message = this.requete.exec(noyau);
                    throw new Exception(message);
                }
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }
    public String[] splitRequest(String requete){
        String req = requete.replace("(", ",");
        // System.out.println(req);
        String[] listeRequete = req.split(",");
        // System.out.println(listeRequete.length);
        for(int i=0;i<listeRequete.length;i++){
            listeRequete[i] = listeRequete[i].replace(")", " ");
            // System.out.println(listeRequete[i]);
        }
        return listeRequete;
    }
    public Vector requestIntoVector(String requete){
        Scanner scan = new Scanner(requete);
        Vector request = new Vector();
        while(scan.hasNext()){
            request.add(scan.next());
        }
        return request;
    }
    public Relation executeAllRequest(String[] listeRequete) throws Exception {
        try {
            Relation table = null;
            int length = listeRequete.length;
            for(int i=length-1;i>=0;i--){
                Vector req = requestIntoVector(listeRequete[i]);
                table = this.traitementReq(req,table);
                table.affiche();
                // return table;
            }
            return table;
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }
    public Relation traitementImbrique(String requete) throws Exception {
        String[] listeRequete = splitRequest(requete);
        Relation resultat = executeAllRequest(listeRequete);
        return resultat;
    }

}