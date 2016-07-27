package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Messenger {

	static void write(ObjectOutputStream out, Message m) {
		m.write(out);
	}

	public static Message read(ObjectInputStream in) {
		try {
			byte code = in.readByte();
			System.out.println("decoded " + code);
			switch (code) {
			case 
			// case Init.CODE:
			// return new Init(in);
			// case Ready.CODE:
			// return new Ready(in);
			// case Heartbeat.CODE:
			// return new Heartbeat(in);
			// case SetStage.CODE:
			// return new SetStage(in);
			//
			// case AddBlock.CODE:
			// return new AddBlock(in);
			// case MoveBlock.CODE:
			// return new MoveBlock(in);
			// case RemoveBlock.CODE:
			// return new RemoveBlock(in);
			//
			// case AddParticle.CODE:
			// return new AddParticle(in);
			//
			// case UpdatePlayer.CODE:
			// return new UpdatePlayer(in);
			// case SetPlayerControl.CODE:
			// return new SetPlayerControl(in);
			// case UpdatePoints.CODE:
			// return new UpdatePoints(in);
			// default:
			// System.out.println("missing message type in Messager: "
			// + code);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
