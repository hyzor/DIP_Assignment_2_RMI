package com.server.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.interf.test.TestInterface;

public class TestImpl extends UnicastRemoteObject implements TestInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3511258933727842430L;

	public TestImpl() throws RemoteException{}
	
	public String testFunction() throws RemoteException {
		System.out.println("Calling testFunction() in TestImpl!");
		return "Test";
	}
}
