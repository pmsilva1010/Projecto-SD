package ws.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class ListarMsgOffAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private ArrayList<MessageOff> msgOff;
	
	public String execute(){
		setMsgOff(this.getBean().getMsgOff());
		
		if(msgOff==null){
			addActionError("Error getting offline messages!");
			return ERROR;
		}	
		
		return SUCCESS;
	}
	
	public ArrayList<MessageOff> getMsgOff(){
		return msgOff;
	}
	
	public void setMsgOff(ArrayList<MessageOff> msgOff){
		this.msgOff=msgOff;
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
