package GUIPrograms;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;

import game.*;
import game.drawing.Draw;

public class PerfomanceTest extends GameJava {

    boolean testDone = false;

    int testIndex = 0;

    long startFrame;

    Date date = new Date();
    long startTime = date.getTime();

    String[] testNames = { "images" };
    long[] testTimes = new long[1];

    // image test
    double[][] imageOffsets = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0.5, 0.5 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
            { 1, 1 }, { -0.5, -0.5 } };

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
            testIndex++;
            testDone = false;
            startFrame = frameCount;
            startTime = new Date().getTime();
            if (testIndex == testTimes.length) {
                for (int i = 0; i < testTimes.length; i++) {
                    System.out.println(testNames[i] + ": " + testTimes[i] + "ms");
                }
            }
        }

        if (testIndex == testTimes.length) {
            Draw.setColor(Color.GRAY);
            for (int i = 0; i < testTimes.length; i++) {
                Draw.text(testNames[i] + ": " + testTimes[i] + "ms", 10, 16 + i * 16);
            }
        } else {
            switch (testIndex) {
                // images
                case 0:
                    double multi = Math.sin((double)frameCount / 100.0) * 50;
                    for (int y = (int)frameCount%100; y < gh; y += 100) {
                        for (int x = (int)frameCount%100; x < gw; x += 100) {
                            for (int i = 0; i < 10; i++) {
                                Draw.image("p" + i, (int) (x + imageOffsets[i][1] * multi),
                                        (int) (y + imageOffsets[i][0] * multi));
                            }
                        }
                    }
                    if (frameCount - startFrame > 500) {
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