/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Pedro
 */
public class User implements Serializable{
    private String user;
    private String pass;
    private ArrayList<String> msgOffline;
    
    public User(String user, String pass){
        this.user=user;
        this.pass=pass;
        msgOffline=new ArrayList();
    }
    
    public String getName(){
        return user;
    }
    
    public String getPass(){
        return pass;
    }
    
    public void setMsg(String str){
        msgOffline.add(str);
    }
    
    public ArrayList<String> getMsgOff(){
        return msgOffline;
    }
    
    public void apagaMsgOff(){
        msgOffline=new ArrayList();
    }
}
