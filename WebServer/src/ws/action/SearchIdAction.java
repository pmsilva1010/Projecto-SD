package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class SearchIdAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private int id;
	private DetailAuction auctionId;
	
	public String execute(){	
		setAuctionId(this.getBean().searchId(id));
			
		if(auctionId==null){
			addActionError("Error getting auction by id or that id doesnt exist!");
			return ERROR;
		}
		else{			
			return SUCCESS;
		}		
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setAuctionId(DetailAuction auctionId){
		this.auctionId=auctionId;
	}
	
	public DetailAuction getAuctionId(){
		return auctionId;
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
