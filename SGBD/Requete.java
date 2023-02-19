package grammaire;

import java.io.Serializable;
import java.util.Vector;

import dataBase.Bdd;
import dataBase.Racine;
import relation.Relation;
import requete.FunctReq;

public class Requete implements Serializable {
    Vector mots;
    String action;
    boolean est_imbrique;
    public Requete(){
        this.mots=new Vector<Mot>();
    }
    public boolean getEst_imbrique(){
        return this.est_imbrique;
    }
    public void setEst_imbrique(boolean est_imbrique) {
        this.est_imbrique = est_imbrique;
    }
    public Vector getMots() {
        return mots;
    }
    public Mot getMot(int index){
        return (Mot)(mots.get(index));
    }
    public Mot getMot(String syntaxe){
        for(int i=0;i<mots.size();i++){
            Mot mot = (Mot)(this.mots.get(i));
            if(syntaxe.compareToIgnoreCase(mot.getSyntaxe()) == 0){
                return mot;
            }
        }
        return null;
    }
    public void setMots(Vector mots) {
        this.mots = mots;
    }
    public void addMot(Mot mot){
        this.mots.add(mot);
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public int initializing(){
        for(int i=0;i<this.mots.size();i++){
            if(this.getMot(i).getAction() != null){
                return i;
            }
        }
        return -1;
    }
    public void initAction(){
        int index = this.initializing();
        this.action = this.getMot(index).getAction();
        for(int i=index+1;i<this.mots.size();i++){
            if(this.getMot(i).getAction() != null){
                this.action = this.action+" "+this.getMot(i).getAction();
            }
        }
    }
    public String[] intoString(Object[] list){
        String[] lString = new String[list.length];
        for(int i=0;i<list.length;i++){
            lString[i] = String.valueOf(list[i]);
        }
        return lString;
    } 

    // rehefa requete tsy mireturn Relation
    public String exec(Racine noyau) throws Exception {
        String message = "erreur";
        if(this.action.contains("create database") == true){
            Mot databse = this.getMot("database");
            noyau.createDataBase(databse.getArg(0));
            message = "ok";
        }
        if(this.action.contains("use database") == true){
            String baseName = this.getMot("database").getArg(0);
            noyau.getGram().setData(noyau.useDataBase(baseName));
            message = "ok";
        }
        if(this.action.contains("create table") == true){
            Mot table = this.getMot("table");
            Mot with = this.getMot("with");
            noyau.getGram().getData().createTable(table.getArg(0), intoString(with.getArgs().toArray()));
            message = "ok";
        }
        if(this.action.contains("insert") == true){
            Mot into = this.getMot("into");
            Mot values = this.getMot("values");
            noyau.getGram().getData().insert(into.getArg(0), intoString(values.getArgs().toArray()));
            message = "ok";
        }
        if(this.action.contains("drop table") == true){
            Mot table = this.getMot("table");
            String nomTable = table.getArg(0);
            noyau.getGram().getData().dropTable(nomTable);
            message = "ok";
        }
        if(this.action.contains("drop database") == true){
            Mot database = this.getMot("database");
            String databaseName = database.getArg(0);
            noyau.dropDatabase(databaseName);
            message = "ok";
        }
        return message;
    }

    //  rehefa mbola tsy nisafidy base ilay olona
    public String initializing(Racine noyau) throws Exception {
        String message = "aucunne base selectionne";
        if(this.action.contains("create database") == true){
            Mot databse = this.getMot("database");
            noyau.createDataBase(databse.getArg(0));
            message = "ok";
        }
        if(this.action.contains("use database") == true){
            String baseName = this.getMot("database").getArg(0);
            noyau.getGram().setData(noyau.useDataBase(baseName));
            message = "ok";
        }
        if(this.action.contains("drop database") == true){
            Mot database = this.getMot("database");
            String databaseName = database.getArg(0);
            noyau.dropDatabase(databaseName);
            message = "ok";
        }
        return message;
    }

    // rehegfa requete mireturn Relation
    public Relation query(Relation[] tableReq) throws Exception {
        // System.out.println("query tsotra");
        FunctReq funct = new FunctReq();
        Relation result = new Relation();
        boolean niditra = false;
        try {
            if(this.action.contains("produit") == true){
                result = funct.produitCartesien(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("intersection") == true){
                result = funct.intersection(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("soustraction") == true){
                result = funct.soustraction(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("distinct") == true){
                result = funct.distinct(tableReq[0]);
                niditra=true;
            }
            if(this.action.contains("union") == true){
                result = funct.union(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("jointure") == true){
                Mot on = this.getMot("on");
                result = funct.jointure(tableReq[0],tableReq[1], on.getArg(0));
                niditra = true;
            }
            if(this.action.contains("division") == true){
                Mot division = this.getMot("division");
                result = funct.division(tableReq[0], tableReq[1], division.getArg(0));
                niditra = true;
            }
            if(this.action.contains("delete") == true){
                if(this.getMot("where") != null){
                    String nomCol = this.getMot("where").getArg(0);
                    Mot option = new Mot();
                    if(this.getMot("=") != null){
                        option = this.getMot("=");
                    }
                    if(this.getMot("like") != null){
                        option = this.getMot("like");
                    }
                    String filtre = this.getMot(option.getSyntaxe()).getArg(0);
                    funct.delete(tableReq[0], nomCol, option.getSyntaxe(), filtre).insert();
                } else {
                    funct.delete(tableReq[0], null, null, null).insert();
                }
            }
            if(this.action.contains("selection") == true && this.action.contains("delete") == false){
                String nomCol = this.getMot("where").getArg(0);
                Mot option = new Mot();
                if(this.getMot("=") != null){
                    option = this.getMot("=");
                }
                if(this.getMot("like") != null){
                    option = this.getMot("like");
                }
                if(result.getAll().isEmpty()){
                    result= tableReq[0];
                }
                // bouclena
                String filtre = this.getMot(option.getSyntaxe()).getArg(0);
                result = funct.selection(result, nomCol, filtre, option.getSyntaxe());
                niditra = true;
                // atambatra anaty boucle le Relation
            }
            
            if(niditra == true){
                if(this.action.contains("projection") == true){
                    Mot select = this.getMot("select");
                    String[] nomCol = this.intoString(select.getArgs().toArray());
                    result = funct.projection(result, nomCol);
                }
            } else {  
                if(this.action.contains("projection") == true){
                    
                    Mot select = this.getMot("select");
                    String[] nomCol = this.intoString(select.getArgs().toArray());
                    result = funct.projection(tableReq[0], nomCol);
                } else {
                    result = tableReq[0];
                }
                
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw(e);
        }
    }
    public Relation query(Relation[] tableReq,Relation temp) throws Exception {
        // System.out.println("query imbrique");
        FunctReq funct = new FunctReq();
        Relation result = new Relation();
        boolean niditra = false;
        try {
            if(this.action.contains("produit") == true){
                result = funct.produitCartesien(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("intersection") == true){
                result = funct.intersection(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("soustraction") == true){
                result = funct.soustraction(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("distinct") == true){
                result = funct.distinct(tableReq[0]);
                niditra=true;
            }
            if(this.action.contains("union") == true){
                result = funct.union(tableReq[0],tableReq[1]);
                niditra = true;
            }
            if(this.action.contains("jointure") == true){
                Mot on = this.getMot("on");
                result = funct.jointure(tableReq[0],tableReq[1], on.getArg(0));
                niditra = true;
            }
            if(this.action.contains("division") == true){
                Mot division = this.getMot("division");
                result = funct.division(tableReq[0], tableReq[1], division.getArg(0));
                niditra = true;
            }
            if(this.action.contains("delete") == true){
                if(this.getMot("where") != null){
                    String nomCol = this.getMot("where").getArg(0);
                    Mot option = new Mot();
                    if(this.getMot("=") != null){
                        option = this.getMot("=");
                    }
                    if(this.getMot("like") != null){
                        option = this.getMot("like");
                    }
                    String filtre = this.getMot(option.getSyntaxe()).getArg(0);
                    funct.delete(tableReq[0], nomCol, option.getSyntaxe(), filtre).insert();
                } else {
                    funct.delete(tableReq[0], null, null, null).insert();
                }
            }
            if(this.action.contains("selection") == true && this.action.contains("delete") == false){
                String nomCol = this.getMot("where").getArg(0);
                Mot option = new Mot();
                if(this.getMot("=") != null){
                    option = this.getMot("=");
                }
                if(this.getMot("like") != null){
                    option = this.getMot("like");
                }
                if(result.getAll().isEmpty()){
                    result = tableReq[0];
                }

                // System.out.println("tonga teto");
                if(temp != null){
                    // System.out.println("requete imbrique");
                    Relation resultImbrique = new Relation();
                    for(int i=0;i<temp.size();i++){
                        String filtre = temp.get(i).get(nomCol);
                        // System.out.println(filtre);
                        // result.affiche();
                        Relation tableTemp = funct.selection(result, nomCol, filtre, option.getSyntaxe());
                        if(resultImbrique.isEmpty() == false){
                            resultImbrique.affiche();
                        }
                        if(resultImbrique.isEmpty() == false){
                            resultImbrique = funct.union(resultImbrique,tableTemp);
                        } else {
                            resultImbrique = tableTemp;
                        }
                        niditra = true;
                    }
                    result = resultImbrique;
                } else {
                    // System.out.println("requete  tsotra");
                    String filtre = this.getMot(option.getSyntaxe()).getArg(0);
                    result = funct.selection(result, nomCol, filtre, option.getSyntaxe());
                    niditra = true;
                }
                // bouclena

                
                // atambatra anaty boucle le Relation
            }
            
            if(niditra == true){
                if(this.action.contains("projection") == true){
                    Mot select = this.getMot("select");
                    String[] nomCol = this.intoString(select.getArgs().toArray());
                    result = funct.projection(result, nomCol);
                }
            } else {  
                if(this.action.contains("projection") == true){
                    
                    Mot select = this.getMot("select");
                    String[] nomCol = this.intoString(select.getArgs().toArray());
                    result = funct.projection(tableReq[0], nomCol);
                } else {
                    result = tableReq[0];
                }
                
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw(e);
        }
    }
}