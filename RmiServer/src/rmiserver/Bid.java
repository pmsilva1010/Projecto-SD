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
public class Bid implements Serializable{
    private String user;
    private double valor;
    
    public Bid(String user, double valor){
        this.user=user;
        this.valor=valor;
    }
    
    public double getVal(){
        return valor;
    }
    
    public String getUser(){
        return user;
    }
}
