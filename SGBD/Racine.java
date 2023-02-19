package dataBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Vector;
import grammaire.Grammaire;

public class Racine implements Serializable {
    Vector listBdd = new Vector();
    Grammaire gram;
    public Racine() throws Exception {
        try {
            initListBdd();
            this.gram = new Grammaire(this);
        } catch (Exception e) {
            throw e;
        }
    }
    public Vector getListBdd() {
        return listBdd;
    }
    public void setListBdd(Vector listBdd) {
        this.listBdd = listBdd;
    }
    public Grammaire getGram() {
        return gram;
    }
    public void setGram(Grammaire gram) {
        this.gram = gram;
    }

    public void initListBdd() throws Exception{
        File fichier = new File("dataBase");
        File[] listFile = fichier.listFiles();
        for(int i=0;i<listFile.length;i++){
            String BddName = listFile[i].getName();
            if(BddName.contains(".") == false){
                listBdd.add(BddName);
            }
        }
    }

    //  miverifier rah misy ilay Bdd
    public boolean checkBDExistence(String baseName){
        for(int i=0;i<this.listBdd.size();i++){
            if(baseName.compareToIgnoreCase(String.valueOf(this.listBdd.get(i))) == 0){
                return true;
            }
        }
        return false;
    }

    public void createDataBase(String dataBaseName) throws Exception {
        if(checkBDExistence(dataBaseName) == false){
            if(dataBaseName.contains(".")){
                throw new Exception("ERREUR: le nom de la base de donnee ne doit pas contenir de '.'");
            }
            File fichier = new File("dataBase/"+dataBaseName);
            fichier.mkdir();
            this.initListBdd();
        } else {
            throw new Exception("ERREUR: cette base existe deja");
        }
    }
    public void dropDatabase(String dataBaseName) throws Exception {
        Bdd dataBase = useDataBase(dataBaseName);
        dataBase.dropAllTable();
        File fichier = new File("dataBase/"+dataBaseName);
        fichier.delete();
        this.listBdd.clear();
        initListBdd();
        if(getGram().getData() != null){
            if(dataBaseName.compareToIgnoreCase(getGram().getData().getBaseName()) == 0){
                getGram().setData(null);
            }
        }
    }

    //  mampiasa an'ilay Bdd
    public Bdd useDataBase(String baseName) throws Exception {
        if(checkBDExistence(baseName) == false){
            throw new Exception("ERREUR: Base de donnees inexistante");
        }
        Bdd dataBase = new Bdd(baseName,this.getGram());
        return dataBase;
    }
}