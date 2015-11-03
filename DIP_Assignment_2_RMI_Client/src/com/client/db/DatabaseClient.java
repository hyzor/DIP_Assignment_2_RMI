package com.client.db;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import com.interf.db.Database;
import com.interf.db.Record;

public class DatabaseClient {
	private static int[] randAccessOrder;
	
	public static void main(String[] args) {
		if (args.length < 1)
			return;
		
		String host = args[0];
		int writeFlag = 0;			// 0 == read
		int sequentialReadFlag = 0;	// 0 == random
		String randAccessFilename = null;
		
		if (args.length > 2) {
			writeFlag = Integer.valueOf(args[1]);
			sequentialReadFlag = Integer.valueOf(args[2]);
		}
		
		if (args.length > 3 && sequentialReadFlag == 0) {
			randAccessFilename = args[3];
		}
		
		Database stub = null;
		int numDatabaseRecords = 0;
		
		try {
			Registry reg = LocateRegistry.getRegistry(host);
			stub = (Database) reg.lookup("Database");
			numDatabaseRecords = stub.getNumRecords();
			
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		
		if (sequentialReadFlag == 0) {
			randAccessOrder = new int[numDatabaseRecords];
			Path path = Paths.get(randAccessFilename);
			
			Scanner scanner = null;
			try {
				scanner = new Scanner(path);
			} catch (IOException e) {
				System.err.println("Client IOException: " + e.toString());
				e.printStackTrace();
			}
			
			int i = 0;
			
			while (scanner.hasNextInt()) {
				randAccessOrder[i++] = scanner.nextInt();
				scanner.nextLine();
			}
		}
		
		try {
			// Get records from database
			int numRecords = stub.getNumRecords();
			int recordDataByteSize = stub.getRecordDataByteSize();
			
			// We are reading from the database...
			if (writeFlag == 0)	{
				//... in a random manner
				if (sequentialReadFlag == 0) {
					System.out.println("Reading " + String.valueOf(numRecords) + " records randomly...");
					for (int i = 0; i < numRecords; ++i) {
						stub.getRecord(randAccessOrder[i]);
						//Record record = stub.getRecord(randAccessOrder[i]);
						//System.out.println(String.valueOf(i) + ": {" + String.valueOf(record.key) + ", " + String.valueOf(record.data).trim() + "}");
					}
				}
				
				//... in a sequential manner
				else {
					System.out.println("Reading " + String.valueOf(numRecords) + " records sequentially...");
					for (int i = 0; i < numRecords; ++i) {
						stub.getRecord(i);
						//Record record = stub.getRecord(i);
						//System.out.println(String.valueOf(i) + ": {" + String.valueOf(record.key) + ", " + String.valueOf(record.data).trim() + "}");
					}
				}
			}
			
			// We are writing to the database
			else {				
				char[] data = new char[recordDataByteSize];
				
				System.out.println("Writing " + numRecords + " records to server...");
				
				// We insert the amount of records we already have,
				// thus, we effectively double the database size
				for (int i = 0; i < numRecords; ++i) {
					data[0] = (char)i;
					//stub.insertRecord(new Record((numRecords + i), data));
					stub.createRecord((numRecords + i), data);
				}
				
				// For verification only...
				/*
				numRecords = stub.getNumRecords();
				
				// Print the result after writing (To verify correctness)
				for (int i = 0; i < numRecords; ++i) {
					Record record = stub.getRecord(i);
					System.out.println(String.valueOf(i) + ": {" + String.valueOf(record.key) + ", " + String.valueOf(record.data).trim() + "}");
				}
				*/
			}
			
			System.out.println("Done!");
		}  catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
