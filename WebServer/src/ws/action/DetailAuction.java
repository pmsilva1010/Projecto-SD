package ws.action;

import java.util.ArrayList;

public class DetailAuction {
	private String title;
	private String code;
	private String description;
	private String deadline;
	private String nrBids;
	private String bestBid;
	private String userBestBid;
	private ArrayList<MsgMural> mural;
	
	public DetailAuction(String title, String code, String description, String deadline, String nrBids, String bestBid, String userBestBid, ArrayList<MsgMural> mural){
		this.title=title;
		this.code=code;
		this.description=description;
		this.deadline=deadline;
		this.nrBids=nrBids;
		this.bestBid=bestBid;
		this.userBestBid=userBestBid;
		this.mural=mural;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getDeadline(){
		return deadline;
	}
	
	public String getNrBids(){
		return nrBids;
	}
	
	public String getBestBid(){
		return bestBid;
	}
	
	public String getUserBestBid(){
		return userBestBid;
	}
	
	public ArrayList<MsgMural> getMural(){
		return mural;
	}
}
