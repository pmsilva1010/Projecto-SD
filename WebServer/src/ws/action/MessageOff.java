package ws.action;

public class MessageOff {
	private String nr;
	private String id;
	private String user;
	private String text;
	
	public MessageOff(String nr, String id, String user, String text){
		this.nr=nr;
		this.id=id;
		this.user=user;
		this.text=text;
	}
	
	public String getNr(){
		return nr;
	}
	
	public String getId(){
		return id;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getText(){
		return text;
	}
}
