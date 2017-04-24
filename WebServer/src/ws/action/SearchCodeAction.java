package ws.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class SearchCodeAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String code=null;
	private ArrayList<Auction> auctionsCode;
	
	public String execute(){	
		setAuctionsCode(this.getBean().searchCode(code));
			
		if(auctionsCode==null){
			addActionError("Error getting auctions by code!11");
			return ERROR;
		}
		else{
			if(auctionsCode.isEmpty())
				addActionMessage("No auctions found with that code!");
			
			return SUCCESS;
		}		
	}
	
	public void setCode(String code){
		this.code=code;
	}
	
	public void setAuctionsCode(ArrayList<Auction> auctionsCode){
		this.auctionsCode=auctionsCode;
	}
	
	public ArrayList<Auction> getAuctionsCode(){
		return auctionsCode;
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
