package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class SendMessageAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String msg=null;
	private int idMsg;
	
	public String execute(){
		if(this.msg != null && !this.msg.equals("")){
			String temp=this.getBean().sendMessage(idMsg,msg);
			
			if(temp.equals("Message Sent!")){
				addActionMessage(temp);
				return SUCCESS;
			}
			else{
				addActionError(temp);
				return ERROR;
			}
		}
		else{
			addActionError("Cant send message!");
			return ERROR;
		}
	}
	
	public int getIdMsg(){
		return idMsg;
	}
	
	public void setMsg(String msg){
		this.msg=msg;
	}
	
	public void setIdMsg(int idMsg){
		this.idMsg=idMsg;
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
