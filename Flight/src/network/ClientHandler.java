package network;

import general.Control.ControlView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Messenger;

public class ClientHandler {

	ObjectOutputStream out;
	ObjectInputStream in;

	ClientHandler(Socket clientSocket) {
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ControlView getControlView() {
		try {
			if (in.available() != 0)
				return (ControlView) Messenger.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void feedChange(Message m) {
		m.write(out);
	}

}
