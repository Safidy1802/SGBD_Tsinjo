package dataBase;
import java.util.Vector;

import grammaire.Grammaire;
import relation.Ligne;
import relation.Relation;

import java.io.*;
public class Bdd implements Serializable {
    String baseName;
    Vector listRelations = new Vector();
    Grammaire gram;
    public Bdd(String baseName,Grammaire gram) {
        this.baseName=baseName;
        setListRelations();
        setGram(gram);
    }
    public void initGrammaire(){
        this.gram = new Grammaire(this);
    } 
    public Grammaire getGram() {
        return gram;
    }
    public void setGram(Grammaire gram) {
        this.gram = gram;
    }
    public String getBaseName() {
        return baseName;
    }
    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }
    public Vector getListRelations() {
        return listRelations;
    }

    public void setListRelations(){
        File fichier = new File("dataBase/"+baseName);
        File[] listFile = fichier.listFiles();
        for(int i=0;i<listFile.length;i++){
            String replaced = listFile[i].getName().replace(".",":");
            String[] splited = replaced.split(":");
            listRelations.add(splited[0]);
        }
    }

    public void dropTable(String nomTable){
        File table = new File("dataBase/"+baseName+"/"+nomTable+".txt");
        table.delete();
        getListRelations().clear();
        setListRelations();
    }

    public void dropAllTable(){
        File fichier = new File("dataBase/"+baseName);
        File[] listFile = fichier.listFiles();
        for(int i=0;i<listFile.length;i++){
            listFile[i].delete();
        }
        getListRelations().clear();
        setListRelations();
    }
    
    public Ligne getLigne(String ligne){
        String[] listAtr = ligne.split(",");
        Vector nomCol = new Vector();
        Vector valeur = new Vector();
        Vector type = new Vector();
        for(int i=0;i<listAtr.length;i++) {
            String[] atr = listAtr[i].split(":");
            nomCol.add(atr[0]);
            valeur.add(atr[1]);
            type.add(atr[2]);
        }
        Ligne line = new Ligne(nomCol, valeur, type);
        return line;
    }

    public Relation getRelation(String relationName) throws Exception {
        File fichier = new File("dataBase/"+baseName+"/"+relationName+".txt");
        
        try(BufferedReader lecteur = new BufferedReader(new FileReader(fichier)))
        {
            Relation table = new Relation(this,relationName);

            String ligne = lecteur.readLine();
            
            while(ligne != null) {
                table.add(this.getLigne(ligne));
                ligne = lecteur.readLine();
            }
            return table;
        }
        catch(Exception a)
        {
            a.printStackTrace();
            System.err.println(a);
            throw a;
        }
    }
    public boolean checkRelationExistence(String nomTable){
        for(int i=0;i<this.getListRelations().size();i++){
            // System.out.println(String.valueOf(this.getListRelations().get(i)));
            if(nomTable.compareToIgnoreCase(String.valueOf(this.getListRelations().get(i))) == 0){
                return true;
            }
        }
        return false;
    }
    public String traitementColumns(String[] columns){
        String nomCol = columns[0].split(":")[0];
        String type = columns[0].split(":")[1];
        String col = nomCol+":null:"+type;
        for(int i=1;i<columns.length;i++){
            nomCol = columns[i].split(":")[0];
            type = columns[i].split(":")[1];
            col = col+","+nomCol+":null:"+type;
        }
        return col;
    }

    public String traitementInsertColumns(String[] columns){
        String nomCol = columns[0].split(":")[0];
        String value = columns[0].split(":")[1];
        Relation table = this.getGram().getTableReq()[0];   
        Vector listeType = table.get(0).getType();
        String col = nomCol+":"+value+":"+String.valueOf(listeType.get(0));
        for(int i=1;i<columns.length;i++){
            nomCol = columns[i].split(":")[0];
            value = columns[i].split(":")[1];
            col = col+","+nomCol+":"+value+":"+String.valueOf(listeType.get(i));
        }
        return col;
    }

    public void createTable(String nomTable,String[] columns) throws Exception {
        if(checkRelationExistence(nomTable) == false){
            File table = new File("dataBase/"+baseName+"/"+nomTable+".txt");
            try(BufferedWriter ecrivain = new BufferedWriter(new FileWriter(table))) 
            {
                String col = traitementColumns(columns);
                ecrivain.write(col);
                setListRelations();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw e;
            }
        } else {
            throw new Exception("ERREUR: table deja existante");
        }
        
    }

    public void insert(String nomTable, String[] colValues) throws Exception {
        Relation tab = this.getRelation(nomTable);
        File table = new File("dataBase/"+baseName+"/"+nomTable+".txt");
        if(tab.isEmpty() == true){
            try(BufferedWriter ecrivain = new BufferedWriter(new FileWriter(table))) 
            {
                String col = traitementInsertColumns(colValues);
                ecrivain.write(col);
                ecrivain.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            try(BufferedWriter ecrivain = new BufferedWriter(new FileWriter(table,true))) 
            {
                String col = traitementInsertColumns(colValues);
                ecrivain.write(col);
                ecrivain.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    
}