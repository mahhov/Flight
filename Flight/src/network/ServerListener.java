package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerListener implements Runnable {
	private ServerSocket serverSocket;
	public boolean serverOnline;
	ClientHandler[] clientHandler;
	int numClients;

	public ServerListener(int maxNumClients) {
		clientHandler = new ClientHandler[maxNumClients];

		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(serverSocket.getLocalPort());
		System.out.println(serverSocket.getInetAddress().getHostAddress());
		System.out.println(serverSocket.getInetAddress().getHostName());
		System.out.println(serverSocket.getInetAddress().getAddress());
		System.out.println(serverSocket.getLocalSocketAddress());
	}

	public void run() {
		try {
			serverSocket.setSoTimeout(3000);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		while (serverOnline && numClients < clientHandler.length) {
			try {
				Socket clientSocket = serverSocket.accept();
				clientHandler[numClients++] = new ClientHandler(clientSocket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	void stop() {
		serverOnline = false;
	}

}
