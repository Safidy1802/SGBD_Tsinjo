package relation;
import java.io.*;
import java.util.Vector;

import dataBase.Bdd;
import relation.Ligne;
public class Relation implements Serializable {
    Vector lignes = new Vector<Ligne>();
    Bdd database;
    String name;
    public Relation() {

    }
    public Relation(Bdd database,String name){
        this.setDatabase(database);
        this.setName(name);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Bdd getDatabase() {
        return database;
    }
    public void setDatabase(Bdd database) {
        this.database = database;
    }
    public void add(Object ligne) {
        this.lignes.add(ligne);
    }
    public Ligne get(int index) {
        return (Ligne)(this.lignes.get(index));
    }
    public Vector getAll() {
        return this.lignes;
    }
    public int size() {
        return this.lignes.size();
    }
    public void setElemetAt(Ligne line ,int index){
        this.lignes.setElementAt(line, index);
    }
    public boolean checkEmptyLine(Ligne line){
        int nbrNull = 0;
        for(int i=0;i<line.getFieldNumber();i++){
            if(line.get(i).compareToIgnoreCase("null") == 0){
                nbrNull++;
            }
        }
        if(line.getFieldNumber() == nbrNull){
            return true;
        }
        return false;
    }

    public int getMaxSizeCol(int indexCol){
        int nbrLigne = this.getAll().size();
        String value = String.valueOf(this.get(0).get(indexCol));
        String nomCol = String.valueOf(this.get(0).getNomCol(indexCol));
        int nomColLength = nomCol.length();
        int max = value.length();
        for(int i=0;i<nbrLigne;i++){
            value = String.valueOf(this.get(i).get(indexCol));
            if(value.length() > max){
                max = value.length();
            }
            if(max < nomColLength){
                max = nomColLength;
            }
        }
        return max;
    }

    public int[] getMaxColLength(){
        int nbrCol = this.get(0).getFieldNumber();
        int[] length = new int[nbrCol];
        for(int i=0;i<nbrCol;i++){
            length[i] = getMaxSizeCol(i);
        }
        return length;
    }

    public boolean isEmpty(){
        if(this.lignes.size() == 0){
            return true;
        } else {
            if(checkEmptyLine(this.get(0)) == true){
                return true;
            }
            return false;
        }
    }

    public int separateTitle(int[] maxColLength){
        int length = maxColLength.length*2;
        for(int i=0;i<maxColLength.length;i++){
            length = length+maxColLength[i];
        }
        return length;
    }

    public void affiche(){
        int[] maxColLength = getMaxColLength();
        this.get(0).afficheNomCol(maxColLength);
        for(int i=0; i<separateTitle(maxColLength) ;i++){
            System.out.print("-");
        }
        System.out.println();
        for(int i=0;i<this.lignes.size();i++){
            this.get(i).afficheValue(maxColLength);
        }
    }
    public void delete(int index){
        this.lignes.remove(index);
    }
    public void deleteAll(){
        Vector nomCol = this.get(0).getNomCol();
        Vector value = new Vector();
        for(int i=0;i<nomCol.size();i++){
            value.add("null");
        }
        Vector type = this.get(0).getType();
        this.lignes.clear();
        Ligne line = new Ligne(nomCol,value,type);
        this.add(line);
    }
    public int getIndexNomCol(String nomCol){
        String[] listNomCol = this.get(0).getAllNomCol();
        for(int i=0;i<listNomCol.length;i++){
            if(nomCol.compareToIgnoreCase(listNomCol[i]) == 0){
                return i;
            }
        }
        return -1;
    }

    public String traitementColumns(Ligne line){
        String ligne = line.getNomCol(0)+":"+line.get(0)+":"+line.getType(0);
        for(int i=1;i<line.getFieldNumber();i++){
            ligne = ligne+","+line.getNomCol(i)+":"+line.get(i)+":"+line.getType(i);
        }
        return ligne;
    }

    public void insert() throws Exception {
        File fichier = new File("dataBase/"+this.getDatabase().getBaseName()+"/"+this.getName()+".txt");
        try(BufferedWriter ecrivain = new BufferedWriter(new FileWriter(fichier)))
        {
            for(int i=0;i<this.getAll().size();i++){
                String col = traitementColumns(this.get(i));
                ecrivain.write(col);
                ecrivain.newLine();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}