/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdinterfaces.RMIInterface;
import sdinterfaces.TCPInterface;


/**
 *
 * @author Pedro
 */
public class TCPServer extends UnicastRemoteObject implements TCPInterface, Serializable{
    private String nomeServidor;
    private String dirRMI1, dirRMI2;
    private int serverPort;
    private RMIInterface h;
    private CopyOnWriteArrayList<UserOn> usersOn;
            
    public TCPServer() throws RemoteException{
        super();
        usersOn=new CopyOnWriteArrayList();
        loadConfigs();
    }
    
    private void loadConfigs(){
        try {
            FileReader frd = new FileReader("configs.txt");
            BufferedReader fr=new BufferedReader(frd);
            nomeServidor=fr.readLine();
            dirRMI1=fr.readLine();
            dirRMI2=fr.readLine();
            serverPort=Integer.parseInt(fr.readLine());
            fr.close();
            frd.close(); 
        } catch (IOException ex) {
            System.out.println("Nao foi possivel carregar as configurações! A desligar o servidor!");
            System.exit(0);
        }
    }
    
    public RMIInterface ligaRMI(){
        h=null;
        try {
            h=(RMIInterface) Naming.lookup(dirRMI1);
            h.subscribe(nomeServidor, (TCPInterface) this);
            return h;
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Problema ao ligar ao RMI primario!"+ ex);
            try {
                h=(RMIInterface) Naming.lookup(dirRMI2);
                h.subscribe(nomeServidor, (TCPInterface) this);
                String temp=dirRMI1;    //troca o servidor primario com o secundario
                dirRMI1=dirRMI2;
                dirRMI2=temp;
                return h;
            }catch (NotBoundException | MalformedURLException | RemoteException ex1) {
                System.out.println("Problema a ligar ao RMI secundario!"+ ex1);
                return h; 
            }
        }
    }
    
    public void setUserOn(String user, PrintWriter out){
        usersOn.add(new UserOn(user,out));
    }
    
    public void removeUserOn(String user){
        UserOn temp=null;
        
        for(int i=0;i<usersOn.size();i++){
            if(usersOn.get(i).getNome().equals(user)){
                temp=usersOn.get(i);
                break;
            }
        }
        
        if(temp!=null)
            usersOn.remove(temp);
    }
    
    //Remote Access
    public ArrayList<String> getUsersOn(){
        ArrayList<String> temp=new ArrayList();
        
        for(int i=0;i<usersOn.size();i++)
            temp.add(usersOn.get(i).getNome());
            
        return temp;
    }
    
    public void sendNotification(String str, ArrayList<String> users){
        for(int i=0;i<usersOn.size();i++){
            for(int z=0;z<users.size();z++){
                if(usersOn.get(i).getNome().equals(users.get(z)))
                    usersOn.get(i).sendMsg(str);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        TCPServer tcp=null;
        try {
            tcp = new TCPServer();
        } catch (RemoteException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try{
            System.out.println("A escutar no porto "+tcp.serverPort);
            ServerSocket listenSocket=new ServerSocket(tcp.serverPort);
            
            while(true){
                Socket clientSocket=listenSocket.accept();
                System.out.println("UM novo cliente");
                new Connection(clientSocket, tcp);
            }
        } catch (IOException ex) {
            System.out.println("Falha no Servidor TCP ao obter ligações!\n"+ex);
        }  
    }
}
