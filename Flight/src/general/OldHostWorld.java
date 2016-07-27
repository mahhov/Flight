package general;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import particle.ParticleSystem;
import toon.Toon;
import toon.ToonStackMonster;

public class OldHostWorld {

	Map map;
	Toon[] toon;
	ParticleSystem particleSys;
	Painter painter;
	Graphics2D brush;
	Control control;
	Control blankControl; // not connected to any jframe
	CameraZ camera;

	public static void main(String[] args) throws InterruptedException,
			IOException {
		OldHostWorld host = new OldHostWorld();
		host.begin();
		System.exit(0);
	}

	OldHostWorld() throws IOException {
		control = new Control();
		blankControl = new Control();
		painter = new Painter(control);
		brush = painter.getBrush();
		toon = new Toon[1];
		toon[0] = new Toon(10, 25, 0, true);
		for (int i = 1; i < toon.length; i++)
			toon[i] = new ToonStackMonster(10 * i, 25, 1, false);
		particleSys = new ParticleSystem();
		map = new Map(ImageIO.read(new File("map1.png")));
		camera = new CameraZ(toon[0], map.width, map.height);
	}

	void begin() throws InterruptedException {
		// Music.BGMUSIC.play();
		long timeStart = 0, timeEnd;
		while (!control.escPressd) {

			// update toons
			for (int i = 0; i < toon.length; i++)
				if (i == 0) {
					map.updateToon(toon[i], control.getView(camera), toon); //particleSys, toon);
				} // else
				//	map.updateToon(toon[i], blankControl, null, particleSys,
				//			toon);
					
			// update particle
			particleSys.update(map);

			// repainting
			camera.update();
			map.paint(brush, camera);
			for (Toon t : toon)
				t.paint(brush, camera);
			particleSys.paint(brush, camera);

			// fps
			timeEnd = System.currentTimeMillis();
			painter.drawTime(timeEnd - timeStart);
			timeStart = System.currentTimeMillis();

			//
			painter.doneDrawing();
			Thread.sleep(5);
		}
	}

}

// map no need to recompute cubex
// use new cube paint system