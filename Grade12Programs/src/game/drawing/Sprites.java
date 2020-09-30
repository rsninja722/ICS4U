package game.drawing;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import game.GameJava;

/** loads and stores all png files drom the assets/images folder in a hashmap */
public class Sprites {

    // hash map of all the sprites
    private static HashMap<String, Sprite> spriteList = new HashMap<String, Sprite>();

    // absolute path of the images folder
    private static String imagesDirectory;

    // finds images, loads them, and puts them in the hashmap
    public static void loadSprites() {
    	
    	imagesDirectory = GameJava.baseDirectory + GameJava.directoryChar + "images" +GameJava.directoryChar;
    	
        System.out.println("[Sprites] loading sprites from: " + imagesDirectory);
        
        StringBuilder debugMsg = new StringBuilder("[Sprites] Loaded images: ");
        
        debugMsg.append(loadFromDirectory(imagesDirectory));
        
        System.out.println(debugMsg.toString().substring(0, debugMsg.toString().length()-1));
    }
    
    private static String loadFromDirectory(String path) {
    	StringBuilder loadedFiles = new StringBuilder();
    	
    	File dir = new File(path);

        // get array of other files
        String[] children = dir.list();
        
        for (int i = 0; i < children.length; i++) {
            String name = children[i];

            // if it is a portable network graphic, create a sprite with the image, and add it to the hashmap
            if (name.endsWith(".png") || name.endsWith(".jpg")) {
                String spriteName = name.substring(0, name.indexOf("."));
                spriteList.put(spriteName, new Sprite(path + name));
                loadedFiles.append(spriteName + ",");
            } else {
            	loadedFiles.append(loadFromDirectory(path + name + GameJava.directoryChar));
            }
        }
        
        return loadedFiles.toString();
    }

    /**
     * get a sprite from the hashmap of sprites
     * @param spriteName name of the image without .png
     * @return {@link game.drawing.Sprite#Sprite(String) Sprite}
     */
    public static Sprite get(String spriteName) {
        Sprite s = spriteList.get(spriteName);
        if (s == null) {
            System.out.println("error: " + spriteName + " not found");
        }
        return spriteList.get(spriteName);
    }
}
