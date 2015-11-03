package com.server.db;

import java.nio.ByteBuffer;
//import java.nio.CharBuffer;
//import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import com.interf.db.Database;
import com.interf.db.Record;

public class DatabaseImpl extends UnicastRemoteObject implements Database {

	protected DatabaseImpl() throws RemoteException {
		super();
	}
	
	//private static Charset latin1Charset = Charset.forName("ISO-8859-1");
	
	private int recordKeyByteSize;
	private int recordDataByteSize;
	
	protected DatabaseImpl(byte[] recordsByteArray, int numRecords,
			int recordKeyByteSize, int recordDataByteSize) throws RemoteException {
		super();		
		records = new ArrayList<Record>(numRecords);
		int recordSize = recordKeyByteSize + recordDataByteSize;
		this.recordKeyByteSize = recordKeyByteSize;
		this.recordDataByteSize = recordDataByteSize;
		
		ByteBuffer byteBuffer;
		//CharBuffer charBuffer;
		
		for (int i = 0; i < numRecords; ++i) {			
			// Get the bytes of the current record, and then get the corresponding key & data bytes
			byte[] recordBytes = Arrays.copyOfRange(recordsByteArray, (i*recordSize), (i*recordSize) + recordSize);
			byte[] keyBytes = Arrays.copyOfRange(recordBytes, 0, recordKeyByteSize);
			byte[] dataBytes = Arrays.copyOfRange(recordBytes, recordKeyByteSize, recordSize);
			//charBuffer = latin1Charset.decode(ByteBuffer.wrap(charBytes));
			//records.get(i).data = charBuffer.array();
			
			// Put the key bytes into a wrapped buffer so that we can convert it into an integer
			byteBuffer = ByteBuffer.wrap(keyBytes).order(java.nio.ByteOrder.LITTLE_ENDIAN); // Intel processor => Little endian storage
			
			//records.add(new Record(byteBuffer.getInt(), charBuffer.array()));
			records.add(new Record(byteBuffer.getInt(), new String(dataBytes).toCharArray()));
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -605608118325697624L;
	
	private ArrayList<Record> records;

	@Override
	public int getNumRecords() throws RemoteException {
		return records.size();
	}

	@Override
	public Record getRecord(int recordId) throws RemoteException {
		if (recordId >= records.size())
			return null;
		
		return records.get(recordId);
	}
	
	@Override
	public void insertRecord(Record record) throws RemoteException {
		records.add(record);
	}

	@Override
	public int getRecordKeyByteSize() throws RemoteException {
		return recordKeyByteSize;
	}

	@Override
	public int getRecordDataByteSize() throws RemoteException {
		return recordDataByteSize;
	}

	@Override
	public void createRecord(int key, char[] data) {
		records.add(new Record(key, data));
	}
}
