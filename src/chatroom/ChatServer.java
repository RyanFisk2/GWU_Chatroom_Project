package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Ryan Fisk
 */
public class ChatServer extends ChatWindow {

	private ClientHandler handler;
	private ClientHandler[] clients = new ClientHandler[25]; //max of 25 clients
	private int clientIndex = 0;

	public ChatServer(){
		super();
		this.setTitle("Chat Server");
		this.setLocation(80,80);

		try {
			// Create a listening service for connections
			// at the designated port number.
			ServerSocket srv = new ServerSocket(2113); //create threads for each socket

			while (true) {
				// The method accept() blocks until a client connects.
				printMsg("Waiting for a connection");
				Socket socket = srv.accept();

				handler = new ClientHandler(socket);
				clients[clientIndex] = handler; //create array to hold all clients
				clientIndex ++;					//used for sending message to all clients

				Thread t = new Thread(handler);
				t.start();
			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/** This innter class handles communication to/from one client. */
	//this class now implements runnable to create separate threads for each client
	class ClientHandler implements Runnable {
		private PrintWriter writer;
		private BufferedReader reader;

		public ClientHandler(Socket socket) {
			try {
				InetAddress serverIP = socket.getInetAddress();
				printMsg("Connection made to " + serverIP);
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch (IOException e){
					printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
				}
		}
		public void run() {
			try {
				while(true) {
					// read a message from the client
					String s = readMsg();
					//sendMsg(s);
				}
			}
			catch (IOException e){
				printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
			}
		}

		/** Receive and display a message */
		public String readMsg() throws IOException {
			String s = reader.readLine();
			printMsg(s);
			sendMsg(s);
			return s;
			
		}
		/** Send a string */
		public void sendMsg(String s){
			for(int i = 0; i < clientIndex; i++)
			{
				clients[i].writer.println(s);
			}
		}

	}

	public static void main(String args[]){
		new ChatServer();
	}
}
