package com.interf.test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestInterface extends Remote {
	String testFunction() throws RemoteException;
}
