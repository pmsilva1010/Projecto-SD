package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class cAuctionAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String code=null, title=null, description=null, deadline=null, Price=null;
	
	public String execute(){
		if(this.code != null && !this.code.equals("") && (this.title != null && !this.title.equals("")) && (this.description != null && !this.description.equals("")) && (this.deadline != null && !this.deadline.equals("")) && (this.Price != null && !this.Price.equals(""))){
			this.getBean().setCode(this.code);
			this.getBean().setTitle(this.title);
			this.getBean().setDescription(this.description);
			this.getBean().setDeadline(this.deadline);
			this.getBean().setPrice(this.Price);
			
			String temp=this.getBean().createAuction();
			
			if(temp.equals("Auction created!")){
				addActionMessage(temp);
				return SUCCESS;
			}
			else{
				addActionError(temp);
				return ERROR;
			}
		}
		else{
			addActionError("Error creating auction!");
			return ERROR;
		}
	}
	
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
