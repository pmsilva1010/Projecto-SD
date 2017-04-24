/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdinterfaces.TCPInterface;
import sdinterfaces.RMIInterface;
import sdinterfaces.WebSInterface;

/**
 *
 * @author Pedro
 */
public class RmiServer extends UnicastRemoteObject implements RMIInterface{
    private CopyOnWriteArrayList<User> users;
    private CopyOnWriteArrayList<Leilao> leiloes;
    private CopyOnWriteArrayList<Leilao> historicoLeiloes;
    private CopyOnWriteArrayList<TcpS> serversTCP;
    private WebSInterface server = null;
    private String dirBDusers="usersBD";
    private String dirBDleiloes="leiloesBD";
    private String dirBDhistLeiloes="histBD";
    private String idDir;
    private int id;
    
    public RmiServer() throws RemoteException{
        super();
        loadConfigs();
        loadBDusers(dirBDusers);
        loadBDleiloes(dirBDleiloes);
        loadBDhistoricoLeiloes(dirBDhistLeiloes);
        serversTCP=new CopyOnWriteArrayList();
        
        //thread que verifica de min a min se alguma leilao terminou
        new Thread(){
            public void run(){
                DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH-mm");  
                while(true){
                    ArrayList<Leilao> temp=new ArrayList();
                    Date data=new Date();
                    for(int i=0;i<leiloes.size();i++){
                        try {
                            //if(data.after(leiloes.get(i).getPrazo())==true){
                            if(data.after(format.parse(leiloes.get(i).getPrazo()))==true){
                                temp.add(leiloes.get(i));
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(temp.isEmpty()==false){
                        historicoLeiloes.addAll(temp);
                        leiloes.removeAll(temp);
                        saveBDleiloes(dirBDleiloes);
                        saveBDhistLeiloes(dirBDhistLeiloes);
                    }
                    
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    private void loadConfigs(){
        try {
            FileReader frd = new FileReader("configs.txt");
            BufferedReader fr=new BufferedReader(frd);
            dirBDusers=fr.readLine();
            dirBDleiloes=fr.readLine();
            dirBDhistLeiloes=fr.readLine();
            idDir=fr.readLine();
            fr.close();
            frd.close(); 
            
            frd = new FileReader(idDir);
            fr=new BufferedReader(frd);
            id=Integer.parseInt(fr.readLine());
            fr.close();
            frd.close(); 
        } catch (IOException ex) {
            System.out.println("Nao foi possivel carregar as configurações! A desligar o servidor!");
            System.exit(0);
        }
    }
    
    public String login(String user, String pass) throws RemoteException{
        for(int i=0;i<users.size();i++){
            if(users.get(i).getName().equals(user))
                if(users.get(i).getPass().equals(pass))
                    return "type: login, ok: true";
                else
                    return "type: login, ok: false";
        }
        return "type: login, ok: false";
    }
    
    public ArrayList<String> getMsgOffline(String user){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getName().equals(user)){
                ArrayList<String> temp=users.get(i).getMsgOff();
                users.get(i).apagaMsgOff();
                saveBDusers(dirBDusers);
                return temp;
            } 
        }
        return null;
    }
    
    public String register(String user, String pass){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getName().equals(user))
                return "type: register, ok: false";
        }
        
        User temp=new User(user,pass);
        users.add(temp);
        
        saveBDusers(dirBDusers);
        System.out.println("User "+user+" registado com sucesso!");
        return "type: register, ok: true";
    }
    
    public String criaLeilao(String code, String titulo, String descricao, String prazo, int precoMax, String criador){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH-mm");
        try {
            Date data=format.parse(prazo);
            Leilao temp=new Leilao(getUniqueId(), code, precoMax, titulo, descricao, format.format(data),  criador);
            
            if(leiloes.contains(temp))
                return "type: create_auction, ok: false";
            else{
                leiloes.add(temp);
                saveBDleiloes(dirBDleiloes);
                return "type: create_auction, ok: true";
            }
        } catch (ParseException ex) {
            Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
            return "type: create_auction, ok: false";
        } 
    }
    
    public String pesquisaLeilao(String code){
        String resposta;
        ArrayList<Leilao> temp=new ArrayList();
        
        for(int i=0;i<leiloes.size();i++){
            if(leiloes.get(i).getCode().equals(code))
                temp.add(leiloes.get(i));
        }

        if(temp.isEmpty())
            resposta="type: search_auction, items_count: 0";
        else{
            resposta="type: search_auction, items_count: "+temp.size();
            for(int i=0;i<temp.size();i++){
                resposta=resposta+", items_"+i+"_id: "+temp.get(i).getID()+", items_"+i+"_code: "+temp.get(i).getCode()+", items_"+i+"_title: "+temp.get(i).getTitulo();
            }
        }
        return resposta;  
    }
    
    public String detalhesLeilao(int id){
        String resposta, msg;
        Leilao temp=null;
        ArrayList<Mensagem> temp2;
        
        //Procura o leilao
        for(int i=0;i<leiloes.size();i++){
            if(leiloes.get(i).getID()==id){
                temp=leiloes.get(i);
                break;
            }   
        }
        if(temp==null){
            for(int i=0;i<historicoLeiloes.size();i++){
                if(historicoLeiloes.get(i).getID()==id){
                    temp=historicoLeiloes.get(i);
                    break;
                }   
            }
        }
        
        //Procura e cria a string com as mensagens no mural do leilao
        if(temp!=null){
            resposta="type: detail_auction, title: "+temp.getTitulo()+", code: "+temp.getCode()+", description: "+temp.getDescricao()+", deadline: "+temp.getPrazo()+", ";
            temp2=temp.getMural();
            
            if(temp2.isEmpty())
                msg="messages_count: 0, ";
            else{
                msg="messages_count: "+temp2.size();
                for(int i=0;i<temp2.size();i++){
                    msg=msg+", messages_"+i+"_user: "+temp2.get(i).getUser()+", messages_"+i+"_text: "+temp2.get(i).getText();
                }
                msg=msg+", ";
            }
            //Devolve a resposta e acrescenta o nr de bids feitas no leilao
            return resposta+msg+"bids_count: "+temp.getNrBids()+", best_bid: "+temp.getValMelhorBid()+", user_best_bid: "+temp.getUserMelhorBid();
        }
        else
            return "type: detail_auction, ok: false";
    }
    
    public String getMeusLeiloes(String user){
        String resposta;
        ArrayList<Leilao> meusLeiloes=new ArrayList();
        
        for(int i=0;i<leiloes.size();i++){
            if(leiloes.get(i).getCriador().equals(user))
                meusLeiloes.add(leiloes.get(i));
        }
        
        resposta="type: my_auctions, items_count: "+meusLeiloes.size();
        
        if(meusLeiloes.isEmpty())
            return resposta;
        else{
            for(int i=0;i<meusLeiloes.size();i++){
                resposta=resposta+", items_"+i+"_id: "+meusLeiloes.get(i).getID()+", items_"+i+"_code: "+meusLeiloes.get(i).getCode()+", items_"+i+"_title: "+meusLeiloes.get(i).getTitulo();
            }
            
            return resposta;
        }
    }
    
    public String getMeusLeiloesClosed(String user){
        String resposta;
        ArrayList<Leilao> meusLeiloes=new ArrayList();
        
        for(int i=0;i<historicoLeiloes.size();i++){
            if(historicoLeiloes.get(i).getCriador().equals(user))
                meusLeiloes.add(historicoLeiloes.get(i));
        }
        
        resposta="type: my_auctions, items_count: "+meusLeiloes.size();
        
        if(meusLeiloes.isEmpty())
            return resposta;
        else{
            for(int i=0;i<meusLeiloes.size();i++){
                resposta=resposta+", items_"+i+"_id: "+meusLeiloes.get(i).getID()+", items_"+i+"_code: "+meusLeiloes.get(i).getCode()+", items_"+i+"_title: "+meusLeiloes.get(i).getTitulo();
            }           
            return resposta;
        }
    }
    
    public String makeBid(int id, double preco, String user){
       for(int i=0;i<leiloes.size();i++){
           if(leiloes.get(i).getID()==id){
               if(leiloes.get(i).getNrBids()==0 && preco<leiloes.get(i).getAmount()){
                   leiloes.get(i).setMelhorBid(new Bid(user,preco));     //Se nao houver bids fica logo a ser a melhor
                   saveBDleiloes(dirBDleiloes);
                   return "type: bid, ok: true";
               }
               else{
                   if(preco<leiloes.get(i).getValMelhorBid()){              //caso ja haja bids verifica se esta é melhor ou nao
                       leiloes.get(i).mudaBidsAnteriores();
                       leiloes.get(i).setMelhorBid(new Bid(user,preco));
                       saveBDleiloes(dirBDleiloes);
                       sendNotificationBid(user, id, preco, leiloes.get(i));
                       return "type: bid, ok: true";
                   }
                   else
                       return "type: bid, ok: false";
               }
           }
       }
       return "type: bid, ok: false";
    }
    
    public String editarLeilao(int id, String campoEditar, String user){ //So muda um campo de cada vez
        for(int i=0;i<leiloes.size();i++){
            if(leiloes.get(i).getID()==id && leiloes.get(i).getCriador().equals(user)){
                String[] temp=campoEditar.split(":");
                temp[0]=temp[0].replaceAll(" ", "");
                switch(temp[0]){
                    case "code":
                        temp[1]=temp[1].replaceAll(" ", "");
                        leiloes.get(i).setCode(temp[1]);
                        saveBDleiloes(dirBDleiloes);
                        return "type: edit_auction, ok: true";
                    case "amount":
                        temp[1]=temp[1].replaceAll(" ", "");
                        leiloes.get(i).setAmount(temp[1].substring(1));
                        saveBDleiloes(dirBDleiloes);
                        return "type: edit_auction, ok: true";
                    case "title":
                        leiloes.get(i).setTitle(temp[1]);
                        saveBDleiloes(dirBDleiloes);
                        return "type: edit_auction, ok: true";
                    case "description":
                        leiloes.get(i).setDescription(temp[1]);
                        saveBDleiloes(dirBDleiloes);
                        return "type: edit_auction, ok: true";
                    case "deadline":
                        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH-mm");
                        try {
                            Date data=format.parse(temp[1]);
                            leiloes.get(i).setDeadLine(format.format(data));
                            saveBDleiloes(dirBDleiloes);
                            return "type: edit_auction, ok: true";
                        } catch (ParseException ex) {
                            Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
                            return "type: edit_auction, ok: false";
                        }
                    default:
                        return "type: edit_auction, ok: false";
                }
            }
        }
        return "type: edit_auction, ok: false";
    }
    
    public String escreveMural(int id, String texto, String user){
        for(int i=0;i<leiloes.size();i++){
            if(leiloes.get(i).getID()==id){
                leiloes.get(i).setMensagem(new Mensagem(user, texto));
                saveBDleiloes(dirBDleiloes);
                sendNotificationMsg(user, id, texto, leiloes.get(i));
                return "type: message, ok: true"; 
            }
        }
        return "type: message, ok: false";
    }
    
    public String listUsersOn(){
        ArrayList<String> usersOn=getUsersOn();
        
        if(server!=null){
            try {
                usersOn.addAll(server.getUsersOn());
            } catch (RemoteException ex) {
                System.out.println("Server Web não encontrado!"+ex);
                server=null;
            }
        }
        
        String resposta="type: online_users, users_count: "+usersOn.size();
        for(int i=0;i<usersOn.size();i++){
            resposta=resposta+", users_"+i+"_username: "+usersOn.get(i);
        }
        
        return resposta;
    }
    
    public void subscribe(String nomeServidor, TCPInterface cliente){
        for(int i=0;i<serversTCP.size();i++){
            if(serversTCP.get(i).getNomeServidor().equals(nomeServidor)){
                //Do nothing
                return;
            }
        }
        serversTCP.add(new TcpS(nomeServidor, cliente));
        System.out.println("Servidor "+nomeServidor+" subscribed!");
    }
    
    public void unsubscribe(String nomeServidor){ //Nao esta a ser usado
        TcpS temp=null;
        
        for(int i=0;i<serversTCP.size();i++){
            if(serversTCP.get(i).getNomeServidor().equals(nomeServidor)){
                temp=serversTCP.get(i);
                break;
            }     
        }
        
        if(temp!=null)
            serversTCP.remove(temp);
    }
    
    //gerador de ids para leiloes
    synchronized private int getUniqueId(){
        id++;
        
        FileWriter fwr;
        try {
            fwr = new FileWriter(idDir);
            BufferedWriter fw=new BufferedWriter(fwr);
            fw.write(""+id);
            fw.close();
            fwr.close();
        } catch (IOException ex) {
            Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private void sendNotificationBid(String user, int idLeilao, double preco, Leilao le){
        String notification="type: notification_bid, id: "+idLeilao+", user: "+user+", amount: "+preco;
        ArrayList<String> usersOn=getUsersOn();
        ArrayList<String> usersAnotificar=le.getUsersActivos();
        ArrayList<String> usersOffLine=le.getUsersActivos();
        
        ArrayList<String> usersOnWeb=new ArrayList();
        if(server!=null){ //acrescenta os que estao on no webserver
            try {
                usersOnWeb=server.getUsersOn();
            } catch (RemoteException ex) {
                System.out.println("Server Web não encontrado!"+ex);
                server=null;
            }
        }
        usersOn.addAll(usersOnWeb);
        
        usersOffLine.removeAll(usersOn);    //fica com os users que estao offline
        
        if(usersOffLine.isEmpty()==false){           
            for(int i=0;i<users.size();i++){
                if(usersOffLine.contains(users.get(i).getName()))
                    users.get(i).setMsg(notification);
            }
            saveBDusers(dirBDusers);
        }

        usersAnotificar.removeAll(usersOffLine);  //fica apenas com os users que se encontram online
        usersAnotificar.removeAll(usersOnWeb);     //fica apenas com os users online no TCP
        
        if(usersAnotificar.isEmpty()==false){           
            for(int i=0;i<serversTCP.size();i++){
                try {
                    serversTCP.get(i).getTcpI().sendNotification(notification, usersAnotificar);
                } catch (RemoteException ex) {
                    System.out.println("Server TCP não encontrado!"+ex);
                }
            }
        }
        
        if(server!=null){
            try {
                server.sendNotification(notification, usersOnWeb);
            } catch (RemoteException ex) {
                System.out.println("Server Web não encontrado!"+ex);
                server=null;
            }
        }
    }
    
    private void sendNotificationMsg(String user, int idLeilao, String texto, Leilao le){
        String notification="type: notification_message, id: "+idLeilao+", user: "+user+", text: "+texto;
        ArrayList<String> usersOn=getUsersOn();
        ArrayList<String> usersAnotificar=le.getUserDoMural();
        ArrayList<String> usersOffLine=le.getUserDoMural();
        
        ArrayList<String> usersOnWeb=new ArrayList();
        if(server!=null){ //acrescenta os que estao on no webserver
            try {
                usersOnWeb=server.getUsersOn();
            } catch (RemoteException ex) {
                System.out.println("Server Web não encontrado!"+ex);
                server=null;
            }
        }
        usersOn.addAll(usersOnWeb);
        
        usersOffLine.removeAll(usersOn);    //fica com os users que estao offline
        usersAnotificar.removeAll(usersOnWeb);     //fica apenas com os users online no TCP
        
        if(usersOffLine.isEmpty()==false){           
            for(int i=0;i<users.size();i++){
                if(usersOffLine.contains(users.get(i).getName()))
                    users.get(i).setMsg(notification);
            }
            saveBDusers(dirBDusers);
        }

        usersAnotificar.removeAll(usersOffLine);  //fica apenas com os users que se encontram online
        
        if(usersAnotificar.isEmpty()==false){
            for(int i=0;i<serversTCP.size();i++){
                try {
                    serversTCP.get(i).getTcpI().sendNotification(notification, usersAnotificar);
                } catch (RemoteException ex) {
                    System.out.println("Server TCP não encontrado!"+ex);
                }
            }
        }
        
        if(server!=null){
            try {
                server.sendNotification(notification, usersOnWeb);
            } catch (RemoteException ex) {
                System.out.println("Server Web não encontrado!"+ex);
                server=null;
            }
        }
    }
    
    //metodo que vai buscar os users online nos servers tcp
    private ArrayList<String> getUsersOn(){
        ArrayList<String> usersOn=new ArrayList();
        ArrayList<TcpS> temp=new ArrayList();  //Caso seja preciso eliminar
        
        for(int i=0;i<serversTCP.size();i++){
            try {
                usersOn.addAll(serversTCP.get(i).getTcpI().getUsersOn());
            } catch (RemoteException ex) {
                System.out.println("Server TCP não encontrado!"+ex);
                temp.add(serversTCP.get(i));
            }
        }
        
        serversTCP.removeAll(temp); //remove as interfaces inacessiveis
        return usersOn;
    }
    
    //Subscribers do web server
    synchronized public void subscribeWeb(WebSInterface server){
        this.server=server;
    }
    
    synchronized public void unsubscribeWeb(){
        this.server=null;
    }
    
    //Gestao da "Base de Dados"
    private void loadBDusers(String nomeBD){
        try{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(nomeBD));
            Object temp=ois.readObject();
            ois.close();
            users=(CopyOnWriteArrayList<User>) temp;
        }
        catch(IOException e){
            System.out.println("Ficheiro não encontrado! A criar ficheiro "+nomeBD+"! -"+e);
            users=new CopyOnWriteArrayList();
            saveBDusers(nomeBD);
        }
        catch(ClassNotFoundException e){
            System.out.println("Problema ao carregar dados da Base de Dados "+nomeBD+"! -"+e);
        }
    }
    
    private void loadBDleiloes(String nomeBD){
        try{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(nomeBD));
            Object temp=ois.readObject();
            ois.close();
            leiloes=(CopyOnWriteArrayList<Leilao>) temp;
        }
        catch(IOException e){
            System.out.println("Ficheiro não encontrado! A criar ficheiro "+nomeBD+"! -"+e);
            leiloes=new CopyOnWriteArrayList();
            saveBDleiloes(nomeBD);
        }
        catch(ClassNotFoundException e){
            System.out.println("Problema ao carregar dados da Base de Dados "+nomeBD+"! -"+e);
        }
    }
    
    private void loadBDhistoricoLeiloes(String nomeBD){
        try{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(nomeBD));
            Object temp=ois.readObject();
            ois.close();
            historicoLeiloes=(CopyOnWriteArrayList<Leilao>) temp;
        }
        catch(IOException e){
            System.out.println("Ficheiro não encontrado! A criar ficheiro "+nomeBD+"! -"+e);
            historicoLeiloes=new CopyOnWriteArrayList();
            saveBDhistLeiloes(nomeBD);
        }
        catch(ClassNotFoundException e){
            System.out.println("Problema ao carregar dados da Base de Dados "+nomeBD+"! -"+e);
        }
    }
    
    private void saveBDusers(String nomeBD){
        try{
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(nomeBD));
            oos.writeObject(users);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Problema ao gravar dados na Base de Dados "+nomeBD+"!");
        }
    }
    
    private void saveBDleiloes(String nomeBD){
        try{
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(nomeBD));
            oos.writeObject(leiloes);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Problema ao gravar dados na Base de Dados "+nomeBD+"!");
        }
    }
    
    private void saveBDhistLeiloes(String nomeBD){
        try{
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(nomeBD));
            oos.writeObject(historicoLeiloes);
            oos.close();
        } catch (IOException ex) {
            System.out.println("Problema ao gravar dados na Base de Dados "+nomeBD+"!");
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int state=1;
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
        
        String dirOutroRMI=getDir();
        
        try{
            while(state==1){
                try {
                    Naming.lookup(dirOutroRMI);
                    System.out.println("O outro servidor parece funcional!");
                    Thread.sleep(30000);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    System.out.println("O outro servidor não responde! A passar a primário!");
                    state=0;
                } catch (InterruptedException ex) {
                    Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            RMIInterface h=new RmiServer();
            LocateRegistry.createRegistry(1099).rebind("ProjectoSD", h);
            System.out.println("Servidor RMI a funcionar!");
        } catch (RemoteException ex) {
            Logger.getLogger(RmiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //vai buscar a localizacao do outro servidor rmi
    static String getDir(){
        try {
            FileReader frd = new FileReader("rmiList.txt");
            BufferedReader fr=new BufferedReader(frd);
            String temp=fr.readLine();
            fr.close();
            frd.close();
            return temp;
        } catch (IOException ex ) {
            System.out.println("Nao foi possivel carregar as configurações! A desligar o servidor!");
            System.exit(0);
            return "";
        }
    }
}
