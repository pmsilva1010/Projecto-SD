/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.PrintWriter;

/**
 *
 * @author Pedro
 */
public class UserOn {
    private String nome;
    private PrintWriter out;
    
    public UserOn(String nome, PrintWriter out){
        this.nome=nome;
        this.out=out;
    }
    
    public String getNome(){
        return nome;
    }
    
    public void sendMsg(String str){
        out.println(str);
    }
}
