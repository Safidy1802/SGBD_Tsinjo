package requete;
import java.lang.Class;
import java.lang.reflect.Field;
import java.util.Vector;

import relation.Ligne;
import relation.Relation;
public class FunctReq {
    public FunctReq() {

    }
    public Relation selection(Relation table,String nomCol,String filter,String option) throws Exception {
        try {
            Relation result = new Relation();
            if(option.compareToIgnoreCase("=") == 0){
                for(int i=0;i<table.size();i++) {
                    if(filter.compareToIgnoreCase(String.valueOf(table.get(i).get(nomCol))) == 0){
                        result.add(table.get(i));
                    }
                }
            }
            if(option.compareToIgnoreCase("like") == 0){
                for(int i=0;i<table.size();i++){
                    if(String.valueOf(table.get(i).get(nomCol)).contains(filter)){
                        result.add(table.get(i));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    public String[] vectorIntoString(Vector list){
        String[] result = new String[list.size()];
        for(int i=0;i<list.size();i++){
            result[i] = String.valueOf(list.get(i));
        }
        return result;
    }
    public Ligne projeter(Ligne line,String[] nomCol){
        Ligne res = new Ligne();
        for(int i=0;i<line.getFieldNumber();i++) {
            for(int j=0;j<nomCol.length;j++) {
                if(line.getNomCol(i).compareToIgnoreCase(nomCol[j]) == 0){
                    res.add(line.getNomCol(i),line.get(i),line.getType(i));
                }
            }
        }
        return res;
    }
    public Relation projection(Relation table,String[] nomCol) {
        Relation result = new Relation();
        for(int i=0;i<table.size();i++) {
            result.add(this.projeter(table.get(i),nomCol));
        }
        return result;
    }
    public void addToNewLine(Ligne line,Ligne newLine){
        for(int j=0;j<line.getFieldNumber();j++){
            newLine.add(line.getNomCol(j),line.get(j),line.getType(j));
        }
    }
    public void concatener(Ligne line,Relation table2,Relation result){
        for(int i=0;i<table2.size();i++){
            Ligne newLine = new Ligne();
            this.addToNewLine(line, newLine);
            this.addToNewLine(table2.get(i), newLine);
            result.add(newLine);
        }
    }
    public Relation produitCartesien(Relation table1,Relation table2){
        Relation result = new Relation();
        for(int i=0;i<table1.size();i++){
            this.concatener(table1.get(i),table2,result);
        }
        return result;
    }
    public int getIndMin(Relation table,String nomCol,int init,String option){
        int indMin = init;
        if(option.compareToIgnoreCase("number") == 0){
            for(int i=init;i<table.size();i++){
                if(Double.parseDouble(table.get(i).get(nomCol)) < Double.parseDouble(table.get(indMin).get(nomCol))){
                    indMin = i;
                }
            }
        } else if(option.compareToIgnoreCase("varchar") == 0){
            for(int i=init;i<table.size();i++){
                if(table.get(i).get(nomCol).compareTo(table.get(indMin).get(nomCol)) < 0){
                    indMin = i;
                }
            }
        }
        return indMin;
    }
    public void trier(Relation table,String nomCol){
        int indMin = 0;
        for(int i=0;i<table.size();i++){
            Ligne newLigne = table.get(i);
            if(table.get(0).getType(nomCol).compareToIgnoreCase("varchar") != 0){
                indMin = getIndMin(table,nomCol,i,"number");
            } else if(table.get(0).getType(nomCol).compareToIgnoreCase("varchar") == 0){
                indMin = getIndMin(table,nomCol,i,"varchar");
            }
            table.setElemetAt(table.get(indMin), i);
            table.setElemetAt(newLigne, indMin);
        }
        if(table.get(0).getType(nomCol).compareToIgnoreCase("String") == 0){

        }
    }
    public void join(Ligne line,Relation table2,Relation result,String nomCol){
        for(int i=0;i<table2.size();i++){
            if(line.get(nomCol).compareToIgnoreCase(table2.get(i).get(nomCol)) == 0){
                Ligne newLigne = new Ligne();
                this.addToNewLine(line, newLigne);
                this.addToNewLine(table2.get(i), newLigne);
                newLigne.remove(nomCol);
                result.add(newLigne);
                break;
            }
        }
    }
    public Relation jointure(Relation table1,Relation table2,String nomCol){
        Relation result = new Relation();
        this.trier(table1, nomCol);
        this.trier(table2, nomCol);
        for(int i=0;i<table1.size();i++){
            this.join(table1.get(i),table2,result,nomCol);
        }
        return result;
    }
    public String checkNbrCol(Relation table1,Relation table2) throws Exception{
        if(table1.get(0).getFieldNumber() != table2.get(0).getFieldNumber()){
            throw new Exception("ERREUR: nombre de colonnes different");
        } 
        for(int i=0;i<table1.get(0).getFieldNumber();i++){
            if(table1.get(0).getType(i).compareToIgnoreCase(table2.get(0).getType(i)) != 0){
                throw new Exception("ERREUR: type de colonne incompatible");
            }
        }
        return "ok";
    }
    public boolean checkAtr(Ligne line1,Ligne line2,String nomCol){
        if(line1.get(nomCol).compareToIgnoreCase(line2.get(nomCol)) != 0){
            return false;
        }
        return true;
    }
    public boolean checkAtr(Ligne line1,Ligne line2){
        for(int i=0;i<line1.getFieldNumber();i++){
            if(line1.get(i).compareToIgnoreCase(line2.get(i)) != 0){
                return false;
            }
        }
        return true;
    }
    
    public int checkDoublon(Relation table1,Ligne line,int init){
        for(int i=init;i<table1.size();i++){
            if(this.checkAtr(table1.get(i),line) == true){
                return i;
            }
        }
        return -1;
    }

    public int checkDoublon(Relation table1,Ligne line,int init,String nomCol){
        for(int i=init;i<table1.size();i++){
            if(this.checkAtr(table1.get(i),line,nomCol) == true){
                return i;
            }
        }
        return -1;
    }

    public void deleteDoublon(Relation table,Ligne ligne,int init){
        for(int i=init;i<table.size();i++){
            if(this.checkAtr(ligne, table.get(i)) == true){
                table.delete(i);
                i=i-1;
            }
        }
    }

    public Relation distinct(Relation table){
        for(int i=0;i<table.size();i++){
            this.deleteDoublon(table, table.get(i), i+1);
        }
        return table;
    }
    public void unir(Relation table1,Relation table2,Relation result){
        int init=0;
        for(int i=0;i<table2.size();i++){
            if(this.checkDoublon(table1, table2.get(i),init) == -1){
                result.add(table2.get(i));
            } else {
                init = this.checkDoublon(table1, table2.get(i),init);
            }
        }
    }
    public Relation union(Relation table1,Relation table2) throws Exception {
        try {
            Relation result = new Relation();
            String check = this.checkNbrCol(table1,table2);
            trier(table1,table1.get(0).getNomCol(0));
            trier(table2,table2.get(0).getNomCol(0));
            for(int i=0;i<table1.size();i++){
                result.add(table1.get(i));
            }
            this.unir(table1,table2,result);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    public Relation intersection(Relation table1,Relation table2) throws Exception {
        try {
            Relation result = new Relation();
            String check = this.checkNbrCol(table1, table2);
            trier(table1,table1.get(0).getNomCol(0));
            trier(table2,table2.get(0).getNomCol(0));
            int init=0;
            for(int i=0;i<table1.size();i++){
                if(this.checkDoublon(table2, table1.get(i), init) != -1){
                    init = this.checkDoublon(table2, table1.get(i), init);
                    result.add(table1.get(i));
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    public Relation intersectionByCol(Relation table1,Relation table2,String nomCol) throws Exception {
        try {
            Relation result = new Relation();
            trier(table1,table1.get(0).getNomCol(0));
            trier(table2,table2.get(0).getNomCol(0));
            int init=0;
            for(int i=0;i<table1.size();i++){
                if(this.checkDoublon(table2, table1.get(i), init,nomCol) != -1){
                    init = this.checkDoublon(table2, table1.get(i), init,nomCol);
                    result.add(table1.get(i));
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    public Relation soustraction(Relation table1,Relation table2) throws Exception {
        try {
                Relation result = new Relation();
                if(table1.isEmpty() == false && table2.isEmpty() == false){
                    String check = this.checkNbrCol(table1, table2);
                    trier(table1,table1.get(0).getNomCol(0));
                    trier(table2,table2.get(0).getNomCol(0));
                }
                int init=0;
                for(int i=0;i<table2.getAll().size();i++){
                    deleteDoublon(table1, table2.get(i), init);
                }
                return table1;
            } catch (Exception e) {
                throw e;
            }
        }
    public String[] projectWithout(Relation table,String nomCol){
        String[] allColName = table.get(0).getAllNomCol();
        Vector listNomCol = new Vector();
        for(int i=0;i<allColName.length;i++){
            if(nomCol.compareToIgnoreCase(allColName[i]) !=0){
                listNomCol.add(allColName[i]);
            }
        }
        String[] listFilterCol = this.vectorIntoString(listNomCol);
        return listFilterCol;
    }
    public Relation division(Relation table1,Relation table2,String nomCol) throws Exception {
        try {
            String[] listNomCol = this.projectWithout(table1, nomCol);
            String[] filter = new String[1];
            filter[0] = nomCol;
            Relation r = this.projection(table1, listNomCol);

            Relation r1 = this.distinct(r);
            
            Relation r2 = this.projection(table2, filter);

            Relation r3 = this.produitCartesien(r1, r2);
            
            Relation r4 = this.soustraction(r3, table1);
    
            Relation r5 = this.projection(r4, listNomCol);
            
            Relation r6 = this.distinct(r1);
            
            Relation r7 = this.distinct(r5);
            
            Relation result = this.soustraction(r6, r7);
            
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public Relation delete(Relation table,String nomCol, String option,String filtre){
        if(nomCol != null && filtre != null){
            int indiceNomCol = table.getIndexNomCol(nomCol);
            if(option.compareToIgnoreCase("=") == 0){
                for(int i=0;i<table.getAll().size();i++){
                    if(filtre.compareToIgnoreCase(table.get(i).get(indiceNomCol)) == 0){
                        if(table.getAll().size() == 1){
                            table.deleteAll();
                        } else {
                            table.delete(i);
                        }
                    }
                }
            }
            if(option.compareToIgnoreCase("like") == 0){
                for(int i=0;i<table.getAll().size();i++){
                    if(filtre.contains(table.get(i).get(indiceNomCol)) == true){
                        if(table.getAll().size() == 1){
                            table.deleteAll();
                        } else {
                            table.delete(i);
                        }
                    }
                }
            }
        } else {
            table.deleteAll();
        }
        return table;
    }
}