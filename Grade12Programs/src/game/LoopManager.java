package game;

import java.awt.Color;

import game.drawing.Draw;

// manages timing and calls update, draw, and absouluteDraw
public class LoopManager {

    private static GameJava mainGameClass;

    // variables for how long to wait between each frame
    public static final double nanosecondsPerSecond = 1000000000;
    public static double nanosPerFrame;
    private static double lastDrawTime = System.nanoTime();

    public static double nanosPerUpdate;
    private static double lastUpdateTime = System.nanoTime();

    // fps counting
    private static double fpsCounter = 0;
    private static double timeSinceLastSecond = System.nanoTime();

    public static double averageFps = 0;

    // update thread, so update speed is independent of drawing
    static UpdateThread ut;

    // multi threading code from https://www.tutorialspoint.com/java/java_multithreading.htm
    
    // update thread
    private static class UpdateThread implements Runnable {
        private Thread t;
        private String threadName;

        UpdateThread(String name) {
            threadName = name;
            System.out.println("Creating " + threadName);
        }

        public void run() {
            System.out.println("Running " + threadName);
            while (GameJava.running) {
                double currentTime = System.nanoTime();
                if (currentTime - lastUpdateTime >= nanosPerUpdate) {
                    // full screen toggle
                    if (Input.keyClick(KeyCodes.F11) && GameJava.allowFullScreen) {
                        Draw.toggleFullSreen();
                    }
                    // debug info toggle
                    if (Input.keyClick(KeyCodes.F3)) {
                        Utils.debugMode = !Utils.debugMode;
                    }
                    // calculate mouse position in world
                    Input.setMousePosition((int) Input.rawMousePos.x, (int) Input.rawMousePos.y);
                    // reset debug string
                    Utils.debugString.setLength(0);
                    // add fps to debug
                    Utils.putInDebugMenu("FPS", LoopManager.averageFps);
                    // call update and reset time
                    mainGameClass.update();
                    lastUpdateTime = currentTime;
                    // increment count
                    GameJava.updateCount++;
                    // set clicked keys to held
                    Input.handleHolding();
                }
            }
            System.out.println("Thread " + threadName + " exiting.");
        }

        public void start() {
            System.out.println("Starting " + threadName);
            if (t == null) {
                t = new Thread(this, threadName);
                t.start();
            }
        }
    }

    public static void startLoops(GameJava mainGameObject) {
        System.out.println("[LoopManager] starting loops with: " + mainGameObject.toString());
        mainGameClass = mainGameObject;

        // if resizable has been turned off
        if(GameJava.resizable == false) {
            Draw.frame.setResizable(false);
        }

        nanosPerFrame = nanosecondsPerSecond / GameJava.framePerSecond;
        nanosPerUpdate = nanosecondsPerSecond / GameJava.updatesPerSecond;

        ut = new UpdateThread("update thread");
        ut.start();

        // drawing thread
        while (GameJava.running) {
            // drawing
            double currentTime = System.nanoTime();
            if (currentTime - lastDrawTime >= nanosPerFrame) {
                // set up buffers
                Draw.preRender();
                // draw using camera
                mainGameClass.draw();
                Draw.renderCameraMovement();

                // set offset to 0 and reselect buffer
                Draw.preAbsoluteRender();
                // draw without camera
                mainGameClass.absoluteDraw();
                // draw debug text
                if (Utils.debugMode) {
                    String[] debugMessages = new String(Utils.debugString).split("\n");
                    Draw.setFontSize(1);
                    for (int i = 0; i < debugMessages.length; i++) {
                        Draw.setColor(new Color(0.2f, 0.2f, 0.2f, 0.7f));
                        int textW = Draw.getWidthOfText(debugMessages[i]);
                        Draw.rect(textW / 2 + 2, 5 + i * 9, textW + 4, 9);
                        Draw.setColor(Color.WHITE);
                        Draw.text(debugMessages[i], 2, 9 + i * 9);
                    }
                }
                // draw buffer to panel
                Draw.renderToScreen();

                lastDrawTime = currentTime;
                // increment count
                GameJava.frameCount++;
                // increment fps count
                fpsCounter++;
            }
            // fps counting
            if (currentTime - timeSinceLastSecond >= nanosecondsPerSecond) {
                averageFps = fpsCounter;
                fpsCounter = 0;
                timeSinceLastSecond = currentTime;
            }
        }
    }
}