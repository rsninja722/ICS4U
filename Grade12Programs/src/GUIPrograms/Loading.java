package GUIPrograms;

/**
 * James N 
 * 2020.10.13 
 * Loading
 * loading icon that transitions between all tetrominoes
 */

import java.awt.Color;

import game.*;
import game.drawing.Camera;
import game.drawing.Draw;
import game.physics.Physics;
import game.physics.Point;
import game.physics.Rect;

import java.awt.Color;

public class Loading extends GameJava {

    // must be above 3, powers of 2 are recommended
    static final double TIME_SCALE = 32;
    // time scale of 16 looks good with rotation
    static final boolean ROTATION = true;

    // rect index, direction 1=up   2=right   3=down   4=left, r, g, b
    static int[][] moves = {    // z
        { 1, 2, 255, 106, 31 }, 
        { 0, 3, 255, 140, 31 }, // L
        { 2, 1, 255, 199, 31 }, 
        { 0, 2, 255, 255, 31 }, // o
        { 0, 4, 192, 255, 31 }, 
        { 3, 4, 68, 255, 31 },  // s
        { 3, 2, 31, 255, 87 }, 
        { 3, 2, 31, 255, 147 }, 
        { 2, 3, 31, 255, 184 }, 
        { 1, 3, 31, 255, 244 }, // i
        { 2, 1, 31, 177, 255 }, 
        { 0, 2, 31, 53, 255 },  // j
        { 0, 4, 113, 31, 255 }, 
        { 1, 4, 165, 31, 255 }, 
        { 3, 4, 203, 31, 255 }, // t
        { 0, 1, 255, 31, 31 }   // z
    };

    static int movementIndex = 0;

    static int movementTime = 0;

    static Rect[] rects = { new Rect(0, 1, 1, 1), new Rect(1, 1, 1, 1), new Rect(1, 2, 1, 1), new Rect(2, 2, 1, 1) };
    static Point[] rectsOffsets = { new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0) };

    public Loading() {
        super(256, 256, 60, 60);

        Draw.alphaBetweenFrames = 0.2f;
        Draw.undecorate();

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new Loading();
    }

    @Override
    public void update() {
        if (ROTATION) {
            Camera.angle += (Math.PI * 2.0) / (moves.length * TIME_SCALE);
        }
        movementTime++;

        // go to next movement
        if (movementTime > TIME_SCALE) {
            movementTime = 0;
            movementIndex++;

            // round blocks to nearest 32
            for (int i = 0; i < rects.length; i++) {
                rects[i].x += Math.round(rectsOffsets[i].x / 32);
                rects[i].y += Math.round(rectsOffsets[i].y / 32);
                rectsOffsets[i] = new Point(0, 0);
            }

            // reset loop
            if (movementIndex == moves.length) {
                movementIndex = 0;
                rects[0] = new Rect(0, 1, 1, 1);
                rects[1] = new Rect(1, 1, 1, 1);
                rects[2] = new Rect(1, 2, 1, 1);
                rects[3] = new Rect(2, 2, 1, 1);
                for (int i = 0; i < 4; i++) {
                    rectsOffsets[i] = new Point(0, 0);
                }
            }
        }

        // move block
        int i = moves[movementIndex][0];
        switch (moves[movementIndex][1]) {
            case 1:
                rectsOffsets[i].y -= 32.0 / TIME_SCALE;
                break;
            case 2:
                rectsOffsets[i].x += 32.0 / TIME_SCALE;
                break;
            case 3:
                rectsOffsets[i].y += 32.0 / TIME_SCALE;
                break;
            case 4:
                rectsOffsets[i].x -= 32.0 / TIME_SCALE;
                break;
        }
    }

    @Override
    public void draw() {
        Draw.setColor(new Color(30, 30, 30));
        Draw.rect(gw / 2, gh / 2, gw * 2, gh * 2);
        int[] c = moves[movementIndex];
        Draw.setColor(new Color(c[2], c[3], c[4]));
        for (int i = 0; i < rects.length; i++) {
            Draw.rect((int) (rects[i].x * 32 + rectsOffsets[i].x) + 64, (int) (rects[i].y * 32 + rectsOffsets[i].y) + 64, 30, 30);
        }
    }

    @Override
    public void absoluteDraw() {}
}