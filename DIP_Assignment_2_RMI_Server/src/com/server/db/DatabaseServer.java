package com.server.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.interf.db.Database;

public class DatabaseServer {
	private static byte[] recordsByteArray;
	
	public static void main(String args[]) {
		if (args.length < 1)
			return;
		
		String recordsFilename = args[0];
		int recordKeyByteSize = 4;			// Always an integer
		int recordDataByteSize = 1024;		// Chars (2 bytes per char)
		int numRecords = 0;
		
		if (args.length > 3) {
			recordKeyByteSize = Integer.valueOf(args[1]);
			recordDataByteSize = Integer.valueOf(args[2]);
			numRecords = Integer.valueOf(args[3]);
		}
		
		int recordSize = recordKeyByteSize + recordDataByteSize;
		
		// Now we load the generated records into memory
		recordsByteArray = loadRecordsFromFile(recordsFilename, recordKeyByteSize, recordDataByteSize);
		numRecords = recordsByteArray.length / recordSize;
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {			
			//Database stub = new DatabaseImpl();
			Database stub = new DatabaseImpl(recordsByteArray, numRecords, recordKeyByteSize, recordDataByteSize);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Database", stub);
			
			System.out.println("DatabaseServer up and running!");
		} catch (Exception e) {
			System.err.println("DatabaseServer exception: " + e.toString());
			e.printStackTrace();
		}
	}
	
	private static byte[] loadRecordsFromFile(String filename, int keyByteSize, int dataByteSize) {
		Path path = Paths.get(filename);
		byte[] data = null;
		
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Failed to load record file! (" + e.toString() + ")");
			e.printStackTrace();
		}
		
		return data;
	}

}