package ws.action;

public class MsgMural {
	private String user;
	private String text;
	
	public MsgMural(String user, String text){
		this.user=user;
		this.text=text;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getText(){
		return text;
	}
}
