package game;

import game.audio.Sounds;
import game.drawing.Draw;
import game.drawing.Sprites;

import java.awt.Color;
import java.nio.file.Paths;

/*
 * TODO:
 * line collition
 * text wrapping
 * scrolling
 * set name and icon
 * make fullscreen not lag
 */

public class GameJava {
    public static GameJava game;

    // game width/height
    public static int gw;
    public static int gh;

    public static boolean resizable = true;
    public static boolean allowFullScreen = true;

    // fps to run updates and drawing at
    public static int framePerSecond = 60;
    public static int updatesPerSecond = 60;

    /** value that increments every frame */
    public static long frameCount = 0;
    /** value that increments every update */
    public static long updateCount = 0;

    /** if set to false, game will stop running */
    public static boolean running = true;

    /** title that displays in the top frame bar */
    public static String frameTitle = "game title";

    // character that separates file paths
    public static String directoryChar = System.getProperty("file.separator");

    // base file directory
    public static String baseDirectory;

    /**
     * @param gameWidth  window width
     * @param gameHeight window height
     * @param fps        frames per second
     * @param ups        updates per second
     */
    public GameJava(int gameWidth, int gameHeight, int fps, int ups) {
        game = this;
        init(gameWidth, gameHeight, fps, ups);
    }

    // calculates frame rate nanosecond speeds and initializes drawing
    public static void init(int gameWidth, int gameHeight, int fps, int ups) {
        System.out.println("[GameJava] initizlizing");

        // If security stuff prevents the file separator from being accessed
        switch (Utils.getOperatingSystem()) {

            case WINDOWS:

                // Check for invalid directory char
                if (!directoryChar.equals("\\")) {
                    directoryChar = "\\";
                }

                break;

            default:

                // Check for invalid directory char
                if (!directoryChar.equals("/")) {
                    directoryChar = "//";
                }

                break;

        }

        baseDirectory = Paths.get("").toAbsolutePath().toString() + directoryChar + "assets";

        gw = gameWidth;
        gh = gameHeight;

        framePerSecond = fps;
        updatesPerSecond = ups;

        Sprites.loadSprites();

        Sounds.loadSounds();

        Draw.init();

        Input.init();
    }

    // default draw
    public void draw() {
        Draw.setFontSize(2);
        Draw.setColor(Color.BLACK);
        Draw.text("no draw method found in the main game file", 100, 50);
    }

    // default absolute draw
    public void absoluteDraw() {
        Draw.setFontSize(2);
        Draw.setColor(Color.BLACK);
        Draw.text("no absolute draw method found in the main game file", 100, 100);
    }

    // default update
    public void update() {
        System.out.println("no update method found in the main game file");
    }
}
