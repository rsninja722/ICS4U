package GUIPrograms;

import java.awt.Color;
import java.util.Arrays;

import game.*;
import game.drawing.Draw;
import game.physics.Physics;
import game.physics.Point;
import game.physics.Rect;

import java.awt.Color;

public class Loading extends GameJava {

    Color[] colors = {
        new Color(3, 248, 252),
        new Color(3, 40, 252),
        new Color(188, 0, 191),
        new Color(13, 191, 0),
        new Color(191, 16, 0),
        new Color(232, 140, 12),
        new Color(234, 245, 37)
    };

    /*
     * 1 = up 2 = right 3 = down 4 = left 5 = stay
     */
    int[][][] movements = {
            // i
            { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 2, 1, 5, 5 }, { 0, 0, 0, 0 } },
            // j
            { { 0, 0, 0, 0 }, { 0, 2, 0, 0 }, { 0, 5, 5, 5 }, { 0, 0, 0, 0 } },
            // t
            { { 0, 0, 0, 0 }, { 0, 0, 5, 0 }, { 0, 5, 5, 1 }, { 0, 0, 0, 0 } },
            // s
            { { 0, 0, 0, 0 }, { 0, 0, 5, 3 }, { 0, 1, 5, 0 }, { 0, 0, 0, 0 } },
            // z
            { { 0, 0, 0, 0 }, { 0, 3, 3, 0 }, { 0, 0, 2, 1 }, { 0, 0, 0, 0 } },
            // l
            { { 0, 0, 0, 0 }, { 0, 0, 0, 4 }, { 0, 1, 4, 4 }, { 0, 0, 0, 0 } },
            // o
            { { 0, 0, 0, 0 }, { 0, 3, 3, 0 }, { 0, 4, 2, 0 }, { 0, 0, 0, 0 } } };

    int movementIndex = -1;

    int movementTime = 32;

    Rect[] rects = new Rect[4];
    Point[] rectsOffsets = new Point[4];
    int[] rectsMoves = new int[4];

    public Loading() {
        super(400, 400, 60, 60);

        for (int i = 0; i < 4; i++) {
            rects[i] = new Rect(i, 2, 1, 1);
            rectsOffsets[i] = new Point(0, 0);
        }

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new Loading();
    }

    @Override
    public void update() {
        movementTime++;
        if (movementTime > 32) {
            movementTime = 0;
            movementIndex++;
            if (movementIndex > 6) {
                movementIndex = 0;
            }
            for (int i = 0; i < rects.length; i++) {
                rects[i].x += Math.round(rectsOffsets[i].x / 32);
                rects[i].y += Math.round(rectsOffsets[i].y / 32);
                rectsOffsets[i] = new Point(0, 0);
            }
            // assign new directions
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    if (movements[movementIndex][y][x] != 0) {
                        for (int i = 0; i < rects.length; i++) {
                            if ((int) rects[i].x == x && (int) rects[i].y == y) {
                                rectsMoves[i] = movements[movementIndex][y][x];
                            }
                        }
                    }
                }
            }
        }

        // for(int i=0;i<rects.length;i++) {
        int i = (int) Math.min(Math.floor(movementTime / 8), 3);
        switch (rectsMoves[i]) {
            case 1:
                rectsOffsets[i].y -= 4;
                break;
            case 2:
                rectsOffsets[i].x += 4;
                break;
            case 3:
                rectsOffsets[i].y += 4;
                break;
            case 4:
                rectsOffsets[i].x -= 4;
                break;
        }
        // }

    }

    @Override
    public void draw() {
        Draw.setColor(colors[movementIndex+1 > 6 ? 0 : movementIndex+1]);
        for (int i = 0; i < rects.length; i++) {
            Draw.rect((int) (rects[i].x * 32 + rectsOffsets[i].x) + 100,
                    (int) (rects[i].y * 32 + rectsOffsets[i].y) + 100, 30, 30);
        }
    }

    @Override
    public void absoluteDraw() {
        // called immediately after draw, all drawing is the same but without the camera
        // affecting anything
    }
}