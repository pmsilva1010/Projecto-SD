/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import sdinterfaces.TCPInterface;

/**
 *
 * @author Pedro
 */
public class TcpS {
    private String nomeServidor;
    private TCPInterface tcpI;
    
    public TcpS(String nomeServidor, TCPInterface tcpI){
        this.nomeServidor=nomeServidor;
        this.tcpI=tcpI;
    }
    
    public String getNomeServidor(){
        return nomeServidor;
    }
    
    public TCPInterface getTcpI(){
        return tcpI;
    }
}
