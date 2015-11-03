package com.server.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.interf.test.TestInterface;

public class TestServer {
	
	public static void main(String args[]) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			//TestInterface stub = (TestInterface) UnicastRemoteObject.exportObject(server, 0);
			//Naming.rebind("rmi://localhost/Test", test);
			
			TestInterface stub = new TestImpl();
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("TestInterface", stub);
			
			System.out.println("TestServer up and running!");
		} catch (Exception e) {
			System.err.println("TestServer exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
