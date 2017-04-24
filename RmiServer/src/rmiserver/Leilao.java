/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Pedro
 */
public class Leilao implements Serializable{
    private int id;
    private String code;
    private double precoMax;
    private Bid melhorBid;
    private String titulo, descricao;
    private String prazo; // Temporario arranjar soluçao para a data
    private String criador;
    private ArrayList<Mensagem> mural;
    private ArrayList<Bid> bidsAnteriores;
    
    public Leilao(int id, String code, int precoMax, String titulo, String descricao, String prazo, String criador){
        this.id=id;
        this.code=code;
        this.precoMax=precoMax;
        this.titulo=titulo;
        this.descricao=descricao;
        this.prazo=prazo;
        this.criador=criador;
        mural= new ArrayList();
        bidsAnteriores= new ArrayList();
    }
    
    public int getID(){
        return id;
    }
    
    public String getCode(){
        return code;
    }
    
    public String getTitulo(){
        return titulo;
    }
    
    public String getDescricao(){
        return descricao;
    }
    
    public String getPrazo(){
        return prazo;
    }
    
    public double getAmount(){
        return precoMax;
    }
    
    public ArrayList<Mensagem> getMural(){
        return mural;
    }
    
    public int getNrBids(){
        if(melhorBid==null)
            return bidsAnteriores.size();
        else
            return bidsAnteriores.size()+1;
    }
    
    public String getCriador(){
        return criador;
    }
    
    public double getValMelhorBid(){
        if(melhorBid==null)
            return precoMax;
        else
            return melhorBid.getVal();
    }
    
    public String getUserMelhorBid(){
        if(melhorBid==null)
            return criador;
        else
            return melhorBid.getUser();
    }
    
    public void setMelhorBid(Bid b){
        melhorBid=b;
    }

    public void setCode(String c){
        code=c;
    }
    
    public void setAmount(String a){
        precoMax=Double.parseDouble(a);
    }
    
    public void setTitle(String t){
        titulo=t;
    }
    
    public void setDescription(String d){
        descricao=d;
    }
    
    public void setDeadLine(String dl){
        prazo=dl;
    }
    
    public void setMensagem(Mensagem m){
        mural.add(m);
    }
    
    public void mudaBidsAnteriores(){
        bidsAnteriores.add(melhorBid);
    }
    
    //Em bids
    public ArrayList<String> getUsersActivos(){
        ArrayList<String> temp=new ArrayList();
        temp.add(criador);
        
        for(int i=0;i<bidsAnteriores.size();i++){
            if(!temp.contains(bidsAnteriores.get(i).getUser()))
                temp.add(bidsAnteriores.get(i).getUser());
        }
        return temp;
    }
    
    public ArrayList<String> getUserDoMural(){
        ArrayList<String> temp=new ArrayList();
        temp.add(criador);
        
        for(int i=0;i<mural.size();i++){
            if(!temp.contains(mural.get(i).getUser()))
                temp.add(mural.get(i).getUser());
        }
        return temp;
    }
}
