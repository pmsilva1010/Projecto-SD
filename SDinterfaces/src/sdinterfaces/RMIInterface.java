/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Pedro
 */
public interface RMIInterface extends Remote {
    //TCP SERVER
    public String login(String user, String pass) throws RemoteException;
    public String register(String user, String pass) throws RemoteException;
    public String criaLeilao(String code, String titulo, String descricao, String prazo, int precoMax, String criador) throws RemoteException;
    public String pesquisaLeilao(String code) throws RemoteException;
    public String detalhesLeilao(int id) throws RemoteException;
    public String getMeusLeiloes(String user) throws RemoteException;
    public String getMeusLeiloesClosed(String user) throws RemoteException;
    public String makeBid(int id, double preco, String user) throws RemoteException;
    public String editarLeilao(int id, String campoEditar, String user) throws RemoteException;
    public String escreveMural(int id, String texto, String user) throws RemoteException;
    public String listUsersOn() throws RemoteException;
    public void subscribe(String nomeServidor, TCPInterface cliente) throws RemoteException;
    public void unsubscribe(String nomeServidor) throws RemoteException;
    public ArrayList<String> getMsgOffline(String user) throws RemoteException;
    
    //WEB SERVER
    public void subscribeWeb(WebSInterface server) throws RemoteException;
    public void unsubscribeWeb() throws RemoteException;
}
