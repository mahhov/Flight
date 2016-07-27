package general;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {

	public static final Music BGMUSIC = new Music("nightwishTaikatalvi",
			650000, 7230000, true, 6, 0);
	// keep volume between -25 and 0 (except the background music)

	File file;
	int start, end;
	boolean loop;
	float volume, balance;

	// volume from -80 to 6 inclusive
	// balance from -1 to 1 inclusive

	Music(String wav, int start, int end, boolean loop, float volume,
			float balance) {
		file = new File(wav + ".wav");
		this.start = start;
		this.end = end;
		this.loop = loop;
		this.volume = volume;
		this.balance = balance;
	}

	private Clip getClip() {
		Clip clip = null;
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audio.getFormat();
			if (format.getChannels() == 1) {
				format = new AudioFormat(format.getSampleRate(),
						format.getSampleSizeInBits(), 2, true, false);
				audio = AudioSystem.getAudioInputStream(format, audio);
			}
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return clip;
	}

	public void play() {
		play(0, 0);
	}

	public void play(short dx, short dy) {
		short d = (short) Util.magnitude(dx, dy);
		if (d < 50)
			d = 50;
		float volume = (float) (50 - Math.log(d * d) * 5);
		if (volume < -55)
			volume = -55;
		float balance = dx / 350f;
		if (balance > 1)
			balance = 1;
		if (balance < -1)
			balance = -1;
		play(volume, balance);
	}

	private void play(float volume, float balance) {
		Clip clip = getClip();

		((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN))
				.setValue(volume + this.volume);
		((FloatControl) clip.getControl(FloatControl.Type.PAN))
				.setValue(balance + this.balance);

		if (loop) {
			clip.setFramePosition(start);
			clip.setLoopPoints(start, end);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.setFramePosition(start);
			clip.start();
		}
	}
}
