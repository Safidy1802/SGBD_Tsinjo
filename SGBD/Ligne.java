package relation;
import java.io.Serializable;
import java.util.Vector;
public class Ligne implements Serializable {
    Vector nomCol = new Vector();
    Vector valeur = new Vector();
    Vector type = new Vector();
    public Ligne(){

    }
    public Ligne(Vector nomCol,Vector valeur,Vector type) {
        this.nomCol=nomCol;
        this.valeur=valeur;
        this.type=type;
    }
    public int getIndex(String nomCol) {
        for(int i=0;i<this.nomCol.size();i++) {
            if(nomCol.compareToIgnoreCase(String.valueOf(this.nomCol.get(i))) == 0){
                return i;
            }
        }
        return -1;
    }
    public void remove(String nomCol){
        int index = this.getIndex(nomCol);
        this.nomCol.remove(index);
        this.valeur.remove(index);
        this.type.remove(index);
    }
    public String get(int index){
        return String.valueOf(this.valeur.get(index));
    }
    public String get(String nomCol){
        int index = this.getIndex(nomCol);
        return String.valueOf(this.valeur.get(index));
    }
    public Vector getNomCol(){
        return this.nomCol;
    }
    public String getNomCol(int index){
        return String.valueOf(this.nomCol.get(index));
    }
    public String getType(int index){
        return String.valueOf(this.type.get(index));
    }
    public String getType(String nomCol){
        int index = this.getIndex(nomCol);
        return String.valueOf(this.type.get(index));
    }
    public Vector getType(){
        return this.type;
    }
    public void set(String nomCol,Object value){
        int index = this.getIndex(nomCol);
        valeur.set(index,value);
    }
    public void add(Object nomCol,Object valeur,Object type){
        this.nomCol.add(nomCol);
        this.valeur.add(valeur);
        this.type.add(type);
    }
    public int getFieldNumber() {
        return this.nomCol.size();
    }
    public String[] getAllNomCol(){
        String[] allColName = new String[this.nomCol.size()];
        for(int i=0;i<this.nomCol.size();i++){
            allColName[i] = String.valueOf(this.nomCol.get(i));
        }
        return allColName;
    }   
    public String generateSpaceBetweenCOl(String value,int nbSpace){
        if(nbSpace > 0){
            String space = " ";
            for(int i=1;i<nbSpace;i++){
                space = space+" ";
            }
            String result = value+space;
            return result;
        } else {
            return value;
        }
        
    }
    public String intoTableValue(String value,int max){
        int size = max;
        int valSize = value.length();
        String result = this.generateSpaceBetweenCOl(value,size-valSize);
        result = "|"+result+"|";
        return result;
    }
    
    public void afficheNomCol(int[] maxColLength){
        for(int i=0;i<this.nomCol.size();i++){
            System.out.print(this.intoTableValue(String.valueOf(this.nomCol.get(i)),maxColLength[i]));
        }
        System.out.println();
    }
    public void afficheValue(int[] maxColLength){
        for(int i=0;i<this.valeur.size();i++){
            System.out.print(this.intoTableValue(String.valueOf(this.valeur.get(i)),maxColLength[i]));
        }
        System.out.println();
    }
}