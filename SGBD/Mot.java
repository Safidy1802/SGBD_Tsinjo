package grammaire;

import java.io.Serializable;
import java.util.Vector;

public class Mot implements Serializable {
    String syntaxe;
    Mot next;
    Mot prev;
    Vector args = new Vector();
    String action;
    public Mot(){

    }
    public Mot(String syntaxe){
        this.setSyntaxe(syntaxe);
        this.checkAction(syntaxe);
    }

    public void checkAction(String syntaxe){
        if(syntaxe.compareToIgnoreCase("where") == 0){
            this.setAction("selection");
        }
        if(syntaxe.compareToIgnoreCase("prod") == 0){
            this.setAction("produit");
        }
        if(syntaxe.compareToIgnoreCase("union") == 0){
            this.setAction("union");
        }
        if(syntaxe.compareToIgnoreCase("join") == 0){
            this.setAction("jointure");
        }
        if(syntaxe.compareToIgnoreCase("intersect") == 0){
            this.setAction("intersection");
        }
        if(syntaxe.compareToIgnoreCase("soustraction") == 0){
            this.setAction("soustraction");
        }
        if(syntaxe.compareToIgnoreCase("distinct") == 0){
            this.setAction("distinct");
        }
        if(syntaxe.compareToIgnoreCase("division") == 0){
            this.setAction("division");
        }
        if(syntaxe.compareToIgnoreCase("create") == 0){
            this.setAction("create");
        }
        if(syntaxe.compareToIgnoreCase("database") == 0){
            this.setAction("database");
        }
        if(syntaxe.compareToIgnoreCase("use") == 0){
            this.setAction("use");
        }
        if(syntaxe.compareToIgnoreCase("table") == 0){
            this.setAction("table");
        }
        if(syntaxe.compareToIgnoreCase("insert") == 0){
            this.setAction("insert");
        }
        if(syntaxe.compareToIgnoreCase("delete") == 0){
            this.setAction("delete");
        }
        if(syntaxe.compareToIgnoreCase("drop") == 0){
            this.setAction("drop");
        }
    }
    public String getSyntaxe() {
        return syntaxe;
    }
    public void setSyntaxe(String syntaxe) {
        this.syntaxe = syntaxe;
    }
    public Mot getNext() {
        return next;
    }
    public void setNext(Mot next) {
        this.next = next;
    }
    public Mot getPrev() {
        return prev;
    }
    public void setPrev(Mot prev) {
        this.prev = prev;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Vector getArgs() {
        return args;
    }
    public void setArgs(Vector args) {
        this.args = args;
    }
    public void addArg(String arg){
        this.args.add(arg);
    }
    public String getArg(int index){
        return String.valueOf(this.args.get(index));
    }
}