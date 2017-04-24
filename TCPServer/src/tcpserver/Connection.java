/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdinterfaces.RMIInterface;

/**
 *
 * @author Pedro
 */
public class Connection extends Thread{
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    private String user;
    private int flag=0;
    private TCPServer tcp;
    
    public Connection(Socket aclientSocket, TCPServer tcp){
        try{
            this.tcp=tcp;
            clientSocket=aclientSocket;
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out=new PrintWriter(clientSocket.getOutputStream(), true);
            this.start();
        }
        catch(IOException ex){
            System.out.println("Problema com a coneção do cliente!\n"+ex);
        }
    }
    
    public void run(){
        try{
            while(true){
                String msg=in.readLine();
                String resposta=trataMensagem(msg);
                out.println(resposta);
                if(flag==1){                                //Ve se apos o login tem msg enquanto esteve offline
                    RMIInterface h=tcp.ligaRMI();
                    ArrayList<String> msgOff=h.getMsgOffline(user);
                    if(msgOff!=null){
                        for(int i=0;i<msgOff.size();i++)
                            out.println(msgOff.get(i));
                    }
                    flag=0;
                }
            }
        } catch (IOException ex) {
            tcp.removeUserOn(user);
            System.out.println("IO: Possivel desconecção do cliente!\n"+ ex);
        }catch(NullPointerException e){
            System.out.println("Erro: "+e);
        }
        
    }
    
    private String trataMensagem(String msg){
        String type;
        String[] partes=msg.split(",");
        partes[0]=partes[0].replaceAll(" ", "");
        
        if(partes[0].startsWith("type:"))
            type=partes[0].split(":")[1];
        else
            return "Sequencia introduzida inválida!";
        
        try{
            System.out.println("A aceder ao servidor RMI!");
            RMIInterface h=tcp.ligaRMI();
            
            if(h==null){            //Em caso de nenhum dos servidores RMI estarem acessiveis espera 30s e volta a tentar
                System.out.println("A procura dos servidores RMI!");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                h=tcp.ligaRMI();
            }
            if(h==null)
                return "type "+type+", RMI: false";
            
            switch (type) {
            case "login":
                return doLogin(h, partes);
            case "register":
                return doRegister(h, partes);
            case "create_auction":
                return doCreateAuction(h, partes);
            case "search_auction":
                return doSearchAuction(h, partes);
            case "detail_auction":
                return doDetailAuction(h, partes);
            case "my_auctions":
                return doMyAuctions(h);
            case "bid":
                return doBid(h, partes);
            case "edit_auction":
                return doEditAuction(h, partes);
            case "message":
                return doMessage(h, partes);
            case "online_users":
                return doOnlineUsers(h);
            default:
                return "Invalid type!";
            }
        }
        catch(RemoteException ex){
                System.out.println("Problema ao ligar ao servidor RMI!\n"+ex);
        }
        
        return "Operação não executada!";
    }
    
    private String doLogin(RMIInterface h, String[] partes) throws RemoteException{
        String nome=partes[1].replaceAll(" ","").split(":")[1];
        String pass=partes[2].replaceAll(" ","").split(":")[1];
        
        String resposta=h.login(nome, pass);
        
        if(resposta.contains("true")){
            user=nome;
            tcp.setUserOn(user, out);
            flag=1;                         //sinaliza que o login foi feito e pode ir ver se ha msg enquanto esteve offline
        }

        return resposta;
    }
    
    private String doRegister(RMIInterface h, String[] partes) throws RemoteException{
        String nome=partes[1].replaceAll(" ","").split(":")[1];
        String pass=partes[2].replaceAll(" ","").split(":")[1];
        
        String resposta=h.register(nome, pass);
        
        if(resposta.contains("true")){
            user=nome;
            tcp.setUserOn(user, out);
        }
 
        return resposta;
    }
    
    private String doCreateAuction(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String code=partes[1].replaceAll(" ","").split(":")[1];
        String titulo=partes[2].split(":")[1];
        String descricao=partes[3].split(":")[1];
        String prazo=partes[4].substring(" deadline: ".length());
        String precoMax=partes[5].replaceAll(" ","").split(":")[1];
        
        String resposta=h.criaLeilao(code, titulo, descricao, prazo, Integer.parseInt(precoMax), user);
        
        return resposta;
    }
    
    private String doSearchAuction(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String code=partes[1].replaceAll(" ","").split(":")[1];
        
        String resposta=h.pesquisaLeilao(code);
        
        return resposta;
    }
    
    private String doDetailAuction(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String id=partes[1].replaceAll(" ","").split(":")[1];
        
        String resposta=h.detalhesLeilao(Integer.parseInt(id));
        
        return resposta;
    }
    
    private String doMyAuctions(RMIInterface h) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String resposta=h.getMeusLeiloes(user);
        
        return resposta;
    }
    
    private String doBid(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String id=partes[1].replaceAll(" ","").split(":")[1];
        String preco=partes[2].replaceAll(" ","").split(":")[1];
        
        String resposta=h.makeBid(Integer.parseInt(id), Float.parseFloat(preco), user);
        
        return resposta;
    }
    
    private String doEditAuction(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String id=partes[1].replaceAll(" ","").split(":")[1];
        
        String resposta=h.editarLeilao(Integer.parseInt(id), partes[2], user);
        
        return resposta;
    }
    
    private String doMessage(RMIInterface h, String[] partes) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String id=partes[1].replaceAll(" ","").split(":")[1];
        String msg=partes[2].split(":")[1];
        
        String resposta=h.escreveMural(Integer.parseInt(id), msg, user);
        
        return resposta;
    }
    
    private String doOnlineUsers(RMIInterface h) throws RemoteException{
        if(user==null)
            return "O logIn não foi efectuado!";
        
        String resposta=h.listUsersOn();
        
        return resposta;
    }
}
