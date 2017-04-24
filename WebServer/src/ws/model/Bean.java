package ws.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import sdinterfaces.RMIInterface;
import ws.action.MessageOff;
import ws.action.MsgMural;
import ws.action.Auction;
import ws.action.DetailAuction;
	
public class Bean {
	private RMIInterface h;
	private String username, password;							//login
	private String code, title, description, deadline, Price; 	//create auction
	private String id, campo, newValue;							//edit auction
	private String dirRMI1="rmi://localhost:1099/ProjectoSD";
	
	public Bean(){
		try{
			h=(RMIInterface) Naming.lookup(dirRMI1);
			System.out.println("Liguei ao RMI");
		}
		catch(MalformedURLException|NotBoundException|RemoteException e){
			e.printStackTrace();
		}
	}
	
	public String doLogin(){
		try {
			String resposta=h.login(username, password);
			
			if(resposta.equals("type: login, ok: true"))
				return "Login Successfull!";
			else
				return "Login Failed!";
		} catch (RemoteException e) {
			return "Error connecting to DataBase!";
		}
	}
	
	public String doRegister(){
		try {
			String resposta=h.register(username, password);
			
			if(resposta.equals("type: register, ok: true"))
				return "Register Successfull!";
			else
				return "Register Failed!";
		} catch (RemoteException e) {
			return "Error connecting to DataBase!";
		}
	}
	
	public ArrayList<MessageOff> getMsgOff(){
		try {
			return setMsgOff(h.getMsgOffline(username));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Auction> getMyAuctions(){
		try {
			return setMyAuctions(h.getMeusLeiloes(username));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Auction> getMyAuctionsClosed(){
		try {
			return setMyAuctions(h.getMeusLeiloesClosed(username));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public ArrayList<String> getOnUsers(){
		try {
			return setUsersOn(h.listUsersOn());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String createAuction(){
		try {
			String resposta=h.criaLeilao(code, title, description, deadline, Integer.parseInt(Price), username);
			
			if(resposta.equals("type: create_auction, ok: true"))
				return "Auction created!";
			else
				return "Auction not created!";
		} catch (NumberFormatException | RemoteException e) {
			e.printStackTrace();
			return "Auction not created!";
		}
	}
	
	public String editAuction(){
		try {
			String resposta=h.editarLeilao(Integer.parseInt(id), campo+":"+newValue, username);
			
			if(resposta.equals("type: edit_auction, ok: true"))
				return "Auction edited!";
			else
				return "Auction not edited!";
		} catch (NumberFormatException | RemoteException e) {			
			e.printStackTrace();
			return "Auction not edited";
		}
	}
	
	public ArrayList<Auction> searchCode(String codeS){
		try {
			String resposta=h.pesquisaLeilao(codeS);
			return setMyAuctions(resposta); //o metodo serve para os dois casos
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public DetailAuction searchId(int id){
		try {
			String resposta=h.detalhesLeilao(id);
			
			if(resposta.equals("type: detail_auction, ok: false"))
				return null;
			else
				return setDetailAuction(resposta);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String sendMessage(int idMsg, String msg){
		try {
			String resposta=h.escreveMural(idMsg, msg, username);
			
			if(resposta.equals("type: message, ok: true"))
				return "Message Sent!";
			else
				return "Failed to sent message!";
		} catch (NumberFormatException | RemoteException e) {
			e.printStackTrace();
			return "Failed to sent message!";
		}
	}
	
	public String makeBid(int idBid, String value){
		try {
			String resposta=h.makeBid(idBid, Double.parseDouble(value.replaceAll(" ", "")), username);
			
			if(resposta.equals("type: bid, ok: true"))
				return "Bid Successfull!";
			else
				return "Failed to make bid!";
		} catch (NumberFormatException | RemoteException e) {
			e.printStackTrace();
			return "Failed to make bid!";
		}
	}
	
	//login
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setPassword(String password){
		this.password=password;
	}
	//end login
	
	//create auction
	public void setCode(String code){
		this.code=code;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public void setDeadline(String deadline){
		this.deadline=deadline;
	}
	
	public void setPrice(String Price){
		this.Price=Price;
	}
	//end create auction
	
	//edit auction
	public void setId(String id){
		this.id=id;
	}
	
	public void setCampo(String campo){
		this.campo=campo;
	}
	
	public void setNewValue(String newValue){
		this.newValue=newValue;
	}
	//end edit auction
	
	//private methods
	private ArrayList<MessageOff> setMsgOff(ArrayList<String> temp){
		ArrayList<MessageOff> mo=new ArrayList<MessageOff>();
		
		for(int i=0;i<temp.size();i++){
			String[] split=temp.get(i).split(":");
			mo.add(new MessageOff(""+i,split[2].split(",")[0],split[3].split(",")[0],split[4]));
		}
		
		return mo;
	}
	
	private ArrayList<Auction> setMyAuctions(String temp){
		ArrayList<Auction> ma=new ArrayList<Auction>();
		String[] resposta=temp.split(",");
		int nrAuctions=Integer.parseInt(resposta[1].split(":")[1].replaceAll(" ", ""));
		int j=2;
		
		for(int i=0;i<nrAuctions;i++){
			ma.add(new Auction(resposta[j].split(":")[1], resposta[j+1].split(":")[1], resposta[j+2].split(":")[1]));
			j=j+3;
		}
		
		return ma;
	}
	
	private DetailAuction setDetailAuction(String temp){
		DetailAuction da;
		ArrayList<MsgMural> mu=new ArrayList<MsgMural>();
		String[] resposta=temp.split(",");
		String titulo, codigo, descricao, prazo, nrBids, bestBid, userBestBid;
		int nrMsgMural=Integer.parseInt(resposta[5].split(":")[1].replaceAll(" ", ""));
		
		titulo=resposta[1].split(":")[1];
		codigo=resposta[2].split(":")[1];
		descricao=resposta[3].split(":")[1];
		prazo=resposta[4].split(":")[1];
		nrBids=resposta[5+(2*nrMsgMural+1)].split(":")[1];
		bestBid=resposta[6+(2*nrMsgMural+1)].split(":")[1];
		userBestBid=resposta[7+(2*nrMsgMural+1)].split(":")[1];

		int j=6;
		for(int i=0;i<nrMsgMural;i++){
			mu.add(new MsgMural(resposta[j].split(":")[1], resposta[j+1].split(":")[1]));
			j=j+2;
		}
		
		da=new DetailAuction(titulo, codigo, descricao, prazo, nrBids, bestBid, userBestBid, mu);
		
		return da;
	}
	
	private ArrayList<String> setUsersOn(String temp){
		ArrayList<String> uo=new ArrayList<String>();
		String[] resposta=temp.split(",");
		int nrUsers=Integer.parseInt(resposta[1].split(":")[1].replaceAll(" ", ""));
		
		for(int i=2;i<nrUsers+2;i++){
			uo.add(resposta[i].split(":")[1]);
		}	
		
		return uo;
	}
}
