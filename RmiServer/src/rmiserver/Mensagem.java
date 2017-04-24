/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import java.io.Serializable;

/**
 *
 * @author Pedro
 */
public class Mensagem implements Serializable{
    private String autor;
    private String texto;
    
    public Mensagem(String autor, String texto){
        this.autor=autor;
        this.texto=texto;
    }
    
    public String getUser(){
        return autor;
    }
    
    public String getText(){
        return texto;
    }
}
