/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Pedro
 */
public interface WebSInterface extends Remote {
    public ArrayList<String> getUsersOn() throws RemoteException;
    public void sendNotification(String str, ArrayList<String> users) throws RemoteException;
}
