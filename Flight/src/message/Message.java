package message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Message {

	Message() {
	}

	Message(ObjectInputStream in) {
		read(in);
	}

	public abstract byte getCode();

	abstract void read(ObjectInputStream in);

	public abstract void write(ObjectOutputStream out);
}
