package pl.killerapps.academia.pad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//import java.util.LinkedList;
//import java.util.Queue;
import java.util.Arrays;

import android.util.Log;

public abstract class PadClient implements Runnable {

	static final int CHUNK_SIZE = 1024;
	
	Socket socket;
	String ip;
	int port;
//	Queue<byte[]> fifo = new LinkedList<byte[]>();
	char[] buffer = new char[CHUNK_SIZE];
	char[] bufferBackup = new char[CHUNK_SIZE];
	char[] bufferTmp;
	int offsetRead = 0;
	int offsetProcessed = 0;
	static final char DELIMITER = '\u2424';
	
	Thread thread;
	
	boolean ready = true;
	
	
	public PadClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		try {
			//socket = new Socket(ip, port);
			socket = new Socket("192.168.0.102", 5001);
			int size = 0;
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			Log.i("PadSocket", "Waiting for data.");
			while (true) {
				if (ready) {
					String chunk = br.readLine();
					if (chunk.endsWith(String.valueOf(chunk))) {
						PadMessage msg = new PadMessage();
						msg.decode(chunk);
						onMessage(msg);
					}
				
				
				
				
//					size = isr.read(buffer, offsetRead, CHUNK_SIZE);
//					if (size < 0) {
//						offsetRead = CHUNK_SIZE;
//						ready = false;
//					} else {
//						offsetRead += size;
//					}
//					int delimiterIndex = Arrays.binarySearch(buffer, DELIMITER);
//					if (delimiterIndex > -1 && delimiterIndex < CHUNK_SIZE) {
//						int nextBufferStart = delimiterIndex + 1;
//						int nextBufferLength = buffer.length - delimiterIndex;
//						if (buffer[nextBufferStart] == PadMessage.VERSION) {
//							onMessage((new PadMessage(Arrays.copyOfRange(buffer, 0, delimiterIndex)))); // copy subset of byte[] into new byte[]
//							System.arraycopy(buffer, nextBufferStart, bufferBackup, 0, nextBufferLength); // copy subset of byte[] to existing byte[]
//							bufferTmp = buffer;
//							buffer = bufferBackup;
//							bufferBackup = bufferTmp;
//						}
//					}
}
			}
		} catch (IOException e) {
			onFailure(e);
		} catch (IndexOutOfBoundsException e) {
			onFailure(e);
		}
	}
	
	void stop() {
		
	}

	protected abstract void onMessage(PadMessage message);
	protected abstract void onFailure(Exception e);
}
