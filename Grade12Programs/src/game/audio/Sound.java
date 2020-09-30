package game.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.Utils;

public class Sound {

	String filePathCache;
	ArrayList<Clip> clips = new ArrayList<Clip>();
	int clipIndex = 0;
	boolean loopSet = false;
	public boolean isMusic = false;

	Sound(String filePath) {
		try {
			filePathCache = filePath;
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
			clips.add(AudioSystem.getClip());
			clips.get(clips.size() - 1).open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println("Error loading sound" + filePath);
		}
	}

	void addClip(int index) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePathCache));
			clips.add(index, AudioSystem.getClip());
			clips.get(index).open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.out.println("Error loading sound" + filePathCache);
		}
	}

	public void play() {
		Clip c = clips.get(clipIndex);
		if (c.isRunning()) {
			addClip(clipIndex);
			c = clips.get(clipIndex);
		}
		c.setFramePosition(0);
		c.start();
		++clipIndex;
		if (clipIndex == clips.size()) {
			clipIndex = 0;
		}
	}

	public void stop() {
		Iterator<Clip> clipItor = clips.iterator();
		while(clipItor.hasNext()) {
			Clip c = clipItor.next();
			c.stop();
			c.setFramePosition(c.getFrameLength());
		}
	}

	public void loop() {
		clipIndex = 0;
		if(!loopSet) {
			clips.get(0).loop(-1);
			loopSet = true;
		}
		clips.get(0).setFramePosition(0);
		clips.get(0).start();
	}
	
	public void ajustGain(float gainLevel) {
		Iterator<Clip> clipItor = clips.iterator();
		while(clipItor.hasNext()) {
			Clip c = clipItor.next();
			
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			
			float gain = (float) Utils.mapRange(gainLevel, 0.0f, 1.0f, gainControl.getMinimum(), gainControl.getMaximum());

			gainControl.setValue(gain);
			
		}
		
	}
}
