package com.client.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.interf.test.TestInterface;

public class TestClient {
	
	public static void main(String[] args) {
		if (args.length < 1)
			return;
		
		String host = args[0];
		
		try {
			Registry reg = LocateRegistry.getRegistry(host);
			TestInterface stub = (TestInterface) reg.lookup("TestInterface");
			
			//TestInterface stub = (TestInterface)Naming.lookup(host);
			String response = stub.testFunction();
			System.out.println("Response: " + response);
		} catch (Exception e) {
			System.err.println("Client exception:" + e.toString());
			e.printStackTrace();
		}
	}

}
