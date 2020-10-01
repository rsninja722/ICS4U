package GUIPrograms;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;

import game.*;
import game.drawing.Camera;
import game.drawing.Draw;

public class PerfomanceTest extends GameJava {

    boolean testDone = false;

    int testIndex = 0;

    long startFrame;

    Date date = new Date();
    long startTime = date.getTime();

    String[] testNames = { "images", "transforms", "camera", "text", "shapes" };
    long[] testTimes = new long[5];
    int[] fps = new int[5];

    // image test
    double[][] imageOffsets = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0.5, 0.5 }, { 0, 1 }, { 1, -1 },
            { 1, 0 }, { 1, 1 }, { -0.5, -0.5 } };

    public PerfomanceTest() {
        super(1280, 720, 10000, 60);

        startFrame = frameCount;
        startTime = date.getTime();

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new PerfomanceTest();
    }

    @Override
    public void update() {
    }

    @Override
    public void draw() {
        if (testDone) {
            testTimes[testIndex] = new Date().getTime() - startTime;
            fps[testIndex] = (int) ((frameCount - startFrame) / (testTimes[testIndex] / 1000.0));
            testIndex++;
            testDone = false;
            startFrame = frameCount;
            startTime = new Date().getTime();
            if (testIndex == testTimes.length) {
                for (int i = 0; i < testTimes.length; i++) {
                    System.out.println(testNames[i] + ": " + testTimes[i] + "ms, fps: " + fps[i]);
                }
            }
        }

        if (testIndex == testTimes.length) {
            Draw.setColor(Color.GRAY);
            for (int i = 0; i < testTimes.length; i++) {
                Draw.text(testNames[i] + ": " + testTimes[i] + "ms, fps: " + fps[i], 10, 16 + i * 16);
            }
        } else {
            switch (testIndex) {
                // images
                case 0:
                    Draw.setColor(new Color(30, 30, 30));
                    Draw.rect(gw / 2, gh / 2, gw, gh);
                    Draw.image("pbig", gw / 2, gh / 2 + ((int) frameCount % 50));
                    double multi = Math.sin((double) frameCount / 100.0) * 50;
                    for (int y = (int) frameCount % 100; y < gh; y += 100) {
                        for (int x = (int) frameCount % 100; x < gw; x += 100) {
                            for (int i = 0; i < 10; i++) {
                                Draw.image("p" + i, (int) (x + imageOffsets[i][1] * multi),
                                        (int) (y + imageOffsets[i][0] * multi));
                            }
                        }
                    }
                    if (frameCount - startFrame > 200) {
                        testDone = true;
                    }
                    break;
                // transforms
                case 1:
                    Draw.setColor(new Color(30, 30, 30));
                    Draw.rect(gw / 2, gh / 2, gw, gh);
                    int j = 0;
                    for (int y = 0; y < gh; y += 77) {
                        for (int x = 0; x < gw; x += 77) {
                            Draw.image("p" + j, x, y, Math.sin((double) (frameCount + x + y) / 20.0),
                                    1.0 + ((double) ((frameCount + x - y) % 20)) / 5.0);
                            j++;
                            j = j > 9 ? 0 : j;
                        }
                    }
                    if (frameCount - startFrame > 200) {
                        testDone = true;
                    }
                    break;
                // camera
                case 2:
                    Draw.imageIgnoreCutoff("pback", gw / 2, gh / 2, 0, 5);
                    Camera.x = (int) (10 + Math.sin(frameCount / 100.0) * 200.0);
                    Camera.y = (int) (Math.cos(frameCount / 100.0) * 200.0 - 10);
                    Camera.angle = (float) Math.tan(frameCount / 100.0);
                    Camera.zoom = (float) (1.0 + (frameCount % 50) / 25.0);
                    if (frameCount - startFrame > 200) {
                        testDone = true;
                        Camera.x = 0;
                        Camera.y = 0;
                        Camera.angle = 0;
                        Camera.zoom = 1;
                    }
                    break;
                // text
                case 3:
                    Draw.setColor(new Color(30, 30, 30));
                    Draw.rect(gw / 2, gh / 2, gw, gh);
                    for (int i = 0; i < 50; i++) {
                        Draw.setColor(new Color(Utils.rand(0, 255), Utils.rand(0, 255), Utils.rand(0, 255)));
                        Draw.setFontSize(1 + i % 3);
                        Draw.text(
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                                i + (int) frameCount % (i + 1), i * 16);
                    }
                    if (frameCount - startFrame > 200) {
                        testDone = true;
                    }
                    break;
                // shapes
                case 4:
                    Draw.setColor(new Color(30, 30, 30));
                    Draw.rect(gw / 2, gh / 2, gw, gh);
                    for (int y = 0; y < gh; y += 100) {
                        Draw.setColor(new Color(Utils.rand(0, 255), Utils.rand(0, 255), Utils.rand(0, 255)));
                        for (int x = 0; x < gw; x += 100) {
                            Draw.rect(x, y, Math.abs(x - y) / 10 + (int) frameCount % 100,
                                    10 + y / 10 + (int) frameCount % 100);
                        }
                    }
                    for (int y = 50; y < gh; y += 100) {
                        Draw.setColor(new Color(Utils.rand(0, 255), Utils.rand(0, 255), Utils.rand(0, 255)));
                        for (int x = 50; x < gw; x += 100) {
                            Draw.circle(x, y, Math.abs(y-x) / 10 + (int) frameCount % 50);
                        }
                    }
                    if (frameCount - startFrame > 200) {
                        testDone = true;
                    }
                    break;
            }
        }

    }

    @Override
    public void absoluteDraw() {
        // called immediately after draw, all drawing is the same but without the camera
        // affecting anything
    }
}