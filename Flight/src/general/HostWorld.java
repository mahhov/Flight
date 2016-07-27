package general;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import network.ClientHandler;
import network.ServerListener;
import toon.Toon;
import toon.ToonStackMonster;

public class HostWorld {

	final int NUM_PLAYERS = 10;

	Map map;
	Toon[] toon;
	ServerListener server;
	ClientHandler[] client;

	public static void main(String[] args) throws InterruptedException,
			IOException {
		HostWorld host = new HostWorld();
		host.begin();
		System.exit(0);
	}

	HostWorld() throws IOException {
		toon = new Toon[NUM_PLAYERS];
		for (int i = 1; i < toon.length; i++)
			toon[i] = new ToonStackMonster(10 * i, 25, 1, false);
		map = new Map(ImageIO.read(new File("map1.png")));

		client = new ClientHandler[NUM_PLAYERS];
		server = new ServerListener(NUM_PLAYERS);
	}

	void begin() throws InterruptedException {
		long timeStart = 0, timeEnd;
		boolean running;
		while (running) {

			// update toons
			for (int i = 0; i < toon.length; i++)
				map.updateToon(toon[i], client[i].getControlView(), toon);

			// fps
			timeEnd = System.currentTimeMillis();
			// (timeEnd - timeStart)
			timeStart = System.currentTimeMillis();

			//
			Thread.sleep(5);
		}
		server.serverOnline = false;
	}
}

// map no need to recompute cubex
// use new cube paint system