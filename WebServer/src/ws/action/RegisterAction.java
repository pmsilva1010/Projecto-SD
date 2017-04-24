package ws.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

import ws.model.Bean;

public class RegisterAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String username = null, password = null;
	
	public String execute(){
		if(this.username != null && !this.username.equals("") && (this.password != null && !this.password.equals(""))){
			this.getBean().setUsername(this.username);
			this.getBean().setPassword(this.password);
			
			String temp=this.getBean().doRegister();
			
			if(temp.equals("Register Successfull!")){
				session.put("username", username);
				session.put("loggedin", true);
				addActionMessage(temp);
				
				return SUCCESS;
			}
			else{
				addActionError(temp);
				return ERROR;
			}
		}
		else{
			addActionError("Register Failed!bb");
			return ERROR;
		}
	}
	
		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
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
