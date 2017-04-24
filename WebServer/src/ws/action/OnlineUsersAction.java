package ws.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class OnlineUsersAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private ArrayList<String> onlineUsers;
	
	public String execute(){
		setOnlineUsers(this.getBean().getOnUsers());
		
		if(onlineUsers==null){
			addActionError("Error getting online users!");
			return ERROR;
		}	
		
		return SUCCESS;
	}
	
	public ArrayList<String> getOnlineUsers(){
		return onlineUsers;
	}
	
	public void setOnlineUsers(ArrayList<String> onlineUsers){
		this.onlineUsers=onlineUsers;
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
