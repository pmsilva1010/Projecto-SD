package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class EditAuctionAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String id=null, campo=null, newValue=null;
	
	public String execute(){
		if(this.id != null && !this.id.equals("") && (this.campo != null && !this.campo.equals("")) && (this.newValue != null && !this.newValue.equals(""))){
			this.getBean().setId(this.id);
			this.getBean().setCampo(this.campo);
			this.getBean().setNewValue(this.newValue);
			
			String temp=this.getBean().editAuction();
			
			if(temp.equals("Auction edited!")){
				addActionMessage(temp);
				return SUCCESS;
			}
			else{
				addActionError(temp);
				return ERROR;
			}
		}
		else{
			addActionError("Error editing auction!");
			return ERROR;
		}	
	}
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setCampo(String campo){
		this.campo=campo;
	}
	
	public void setNewValue(String newValue){
		this.newValue=newValue;
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
