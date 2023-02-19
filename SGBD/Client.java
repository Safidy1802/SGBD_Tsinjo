package client;
import java.io.*;
import java.util.Date;
import java.net.*;
import java.util.Scanner;

import relation.Relation;

public class Client {
    public static void main(String[] args) {

        try {   
            Socket client = new Socket("localhost" , 1796); //serveur dans ce pc local et le port pour communiquer
            System.out.println("connected");
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            InputStream is = client.getInputStream();                                      
            ObjectInputStream message = new ObjectInputStream(is);
            Scanner sc = new Scanner(System.in);                            //  scanner mandray requete ataon'ny client
            boolean end = false;                                            //  boolean afantarana raha nideconnecte ilay client

            while(end == false){
                System.out.print("sql>");
                String req = sc.nextLine();                                 //  maka ilay requete
                if(req.compareToIgnoreCase("quit") == 0){
                    end = true;
                    is.close();
                } else {
                    writer.println(req);                                    //  mandefa ilay requete makany am server
                    writer.flush();
                    Object obj = message.readObject();                      //  mandray ny reponse avy any am serveur
                    
                    if(obj instanceof Relation){
                        Relation table = (Relation)(obj);
                        table.affiche();
                        System.out.println();
                    }
                    else if(obj instanceof Exception){
                        Exception e = (Exception)(obj);
                        System.out.println(e.getMessage());
                        System.out.println();
                    } else {
                        System.out.println();
                    }
                }
                
            }                      
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}