package ws.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class ListarMyAuctionsAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private ArrayList<Auction> myAuctions, myAuctionsClosed;
	
	public String execute(){
		setMyAuctions(this.getBean().getMyAuctions());
		setMyAuctionsClosed(this.getBean().getMyAuctionsClosed());
		
		if(myAuctions==null){
			addActionError("Error getting my auctions!");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public ArrayList<Auction> getMyAuctions(){
		return myAuctions;
	}
	
	public void setMyAuctions(ArrayList<Auction> myAuctions){
		this.myAuctions=myAuctions;
	}
	
	public void setMyAuctionsClosed(ArrayList<Auction> myAuctionsClosed){
		this.myAuctionsClosed=myAuctionsClosed;
	}
	
	public ArrayList<Auction> getMyAuctionsClosed(){
		return myAuctionsClosed;
	}
	
	public Bean getBean() {
		if(!session.containsKey("bean"))
			this.setBean(new Bean());
		
		return (Bean) session.get("bean");
	}
	
	public void setBean(Bean bean){
		this.session.put("bean", bean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session=session;
	}
}
