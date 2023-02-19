package thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;

import grammaire.Grammaire;
import dataBase.Racine;
import java.util.Vector;
import java.util.Scanner;
import relation.Relation;

public class ThreadRequest extends Thread{
    Socket client;
    public ThreadRequest(Socket client){
        setClient(client);
    }

    public Socket getClient() {
        return client;
    }
    public void setClient(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Racine racine = new Racine();
            Grammaire gram = racine.getGram();
            OutputStream os = client.getOutputStream();                         
            ObjectOutputStream message = new ObjectOutputStream(os);  
            boolean end = false;    
            // System.out.println("niditra");
            while(end == false){
                
                String requete = reader.readLine();                         //  maka ilay requete avy any am client  
                if(requete.contains("(") == true){
                    try {
                        Relation table = gram.traitementImbrique(requete);
                        message.writeObject(table);
                    } catch (Exception e) {
                        // TODO: handle exception
                        message.writeObject(e);
                        e.printStackTrace();
                    }
                    
                } else {
                    Scanner scan = new Scanner(requete);
                    Vector request = new Vector();
                    while(scan.hasNext()){
                        request.add(scan.next());
                    }

                    if(request.isEmpty()){
                        message.writeObject(" ");
                    } else {
                        try {
                            Relation table = gram.traitementReq(request);
                            message.writeObject(table);
                        } catch (Exception e) {
                            // TODO: handle exception
                            message.writeObject(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}