package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class MakeBidAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String value=null;
	private int idBid;
	
	public String execute(){
		if(this.value != null && !this.value.equals("")){
			String temp=this.getBean().makeBid(idBid,value);
			
			if(temp.equals("Bid Successfull!")){
				addActionMessage(temp);
				return SUCCESS;
			}
			else{
				addActionError(temp);
				return ERROR;
			}
		}
		else{
			addActionError("Error making bid!");
			return ERROR;
		}
	}
	
	public int getIdBid(){
		return idBid;
	}
	
	public void setIdBid(int idBid){
		this.idBid=idBid;
	}
	
	public void setValue(String value){
		this.value=value;
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
