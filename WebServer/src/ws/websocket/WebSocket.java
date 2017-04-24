package ws.websocket;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import sdinterfaces.RMIInterface;
import sdinterfaces.WebSInterface;

@ServerEndpoint(value = "/wsSocket")
public class WebSocket extends UnicastRemoteObject implements WebSInterface, Serializable{
	private static final long serialVersionUID = 1L;
	private static final CopyOnWriteArrayList<WebSocket> users= new CopyOnWriteArrayList<>();
    private Session session;
    private String username;
    private RMIInterface h;
    private String dirRMI1="rmi://localhost:1099/ProjectoSD";
    
    public WebSocket() throws RemoteException{
    	super();
    }
    
    @OnOpen
	public void start(Session session) {
    	users.add(this);
        this.setSession(session);
            
        try{
        	h=(RMIInterface) Naming.lookup(dirRMI1);
    		System.out.println("WS: Liguei ao RMI subscribing");
    		h.subscribeWeb(this);
    	}
    	catch(MalformedURLException|NotBoundException|RemoteException e){
    		e.printStackTrace();
    	}
	}
	
	@OnClose
	public void end() {		
        users.remove(this); //Cliente disconecta
	}
	
	@OnMessage
    public void receiveMessage(Session session, String message){		
		this.username=message;
	}
	
	@OnError
    public void handleError(Throwable t) {
    	users.remove(this);
    	System.out.println(t);
    }
	
	public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

	@Override
	public ArrayList<String> getUsersOn() throws RemoteException {
		
		ArrayList<String> temp=new ArrayList<String>();
		
		for(int i=0;i<users.size();i++){
			temp.add(users.get(i).username);
		}
		
		return temp;
	}

	@Override
	public void sendNotification(String notification, ArrayList<String> usersToNotify) throws RemoteException {
		for(int i=0;i<users.size();i++){
			for(int z=0;z<usersToNotify.size();z++){				
				if(users.get(i).username.equals(usersToNotify.get(z))){					
					try {
						users.get(i).session.getBasicRemote().sendText(notification);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Failed to send notification!");
					}										
				}
			}
		}
	}
}
