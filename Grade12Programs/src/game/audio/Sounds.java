package game.audio;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import game.GameJava;
import game.audio.Sound;
import game.drawing.Sprite;

public class Sounds {
	public static HashMap<String, Sound> soundList = new HashMap<String, Sound>();
	
	

    // absolute path of the images folder
    private static String soundsDirectory;

    // finds images, loads them, and puts them in the hashmap
    public static void loadSounds() {
    	
    	soundsDirectory = GameJava.baseDirectory + GameJava.directoryChar + "audio" + GameJava.directoryChar;
    	
        System.out.println("[Sounds] loading sounds from: " + soundsDirectory);
        
        StringBuilder debugMsg = new StringBuilder("[Sounds] Loaded sounds: ");
        
        debugMsg.append(loadFromDirectory(soundsDirectory));

        System.out.println(debugMsg.toString().substring(0, debugMsg.toString().length()-1));
    }
    
    private static String loadFromDirectory(String path) {
    	StringBuilder loadedFiles = new StringBuilder();
    	
    	File dir = new File(path);

        // get array of other files
        String[] children = dir.list();
        
        for (int i = 0; i < children.length; i++) {
            String name = children[i];

            if (name.endsWith(".wav") || name.endsWith(".mp3")) {
                String soundName = name.substring(0, name.indexOf("."));
                soundList.put(soundName, new Sound(path + name));
                loadedFiles.append(soundName + ",");
            } else if(!name.contains(".")){
            	loadedFiles.append(loadFromDirectory(path + name + GameJava.directoryChar));
            }
        }
        
        return loadedFiles.toString();
    }
	
    /**
     * plays a sound
     * @param soundName
     */
	public static void play(String soundName) {
		Sound s = soundList.get(soundName);
        if (s == null) {
            System.out.println("error: " + soundName + " not found");
        }
        soundList.get(soundName).play();
	}

	/**
	 * stops a sound
	 * @param soundName
	 */
	public static void stop(String soundName) {
		Sound s = soundList.get(soundName);
        if (s == null) {
            System.out.println("error: " + soundName + " not found");
        }
        soundList.get(soundName).stop();
	}
	
	/**
	 * starts a sound and sets it to loop
	 * @param soundName
	 */
	public static void loop(String soundName) {
		Sound s = soundList.get(soundName);
        if (s == null) {
            System.out.println("error: " + soundName + " not found");
        }
        soundList.get(soundName).loop();
	}
	
	/**
	 * 
	 * @param soundName
	 * @param level number between 0.0 and 1.0
	 */
	public static void ajustGain(String soundName, float level) {
		Sound s = soundList.get(soundName);
        if (s == null) {
            System.out.println("error: " + soundName + " not found");
        }
        level = Math.max(Math.min(level, 1.0f), 0.0f);
        
        soundList.get(soundName).ajustGain(level);
	}
}