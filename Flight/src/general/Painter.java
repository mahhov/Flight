package general;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Painter extends JFrame {

	BufferedImage canvas, bufferCanvas;
	Graphics2D brush, bufferBrush;
	public static final int WIDTH = 800, HEIGHT = 800;
	public static final int SCALE = 4, TOP = HEIGHT - SCALE;

	public Painter(Control control) {
		super();

		this.setSize(WIDTH, HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - WIDTH) / 2,
				(screenSize.height - HEIGHT) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.addMouseListener(control);
		this.addMouseMotionListener(control);
		this.addKeyListener(control);
		this.setVisible(true);

		canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		brush = canvas.createGraphics();
		brush.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		bufferCanvas = new BufferedImage(WIDTH, HEIGHT, // 2
				BufferedImage.TYPE_INT_RGB);
		bufferBrush = bufferCanvas.createGraphics(); // 2
	}

	public Graphics2D getBrush() {
		return brush;
	}

	void doneDrawing() {
		bufferBrush.drawImage(canvas, 0, 0, WIDTH, HEIGHT, null); // 2
		paint(getGraphics());
	}

	public void paint(Graphics g) { // paintComponent
		if (brush != null) {
			// draw
			// g.drawImage(canvas, 0, 0, WIDTH, HEIGHT, null); // 1
			g.drawImage(bufferCanvas, 0, 0, WIDTH, HEIGHT, null); // 2

			// erase
			// brush.setColor(new Color(230f / 255, 230f / 255, 230f / 255,
			// .7f));
			brush.setColor(Color.WHITE);
			brush.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	void drawTime(long time) {
		brush.setColor(Color.WHITE);
		brush.fillRect(20, 5, 20, 20);
		brush.setColor(Color.BLACK);
		brush.drawString("" + time, 20, 20);
	}
}
