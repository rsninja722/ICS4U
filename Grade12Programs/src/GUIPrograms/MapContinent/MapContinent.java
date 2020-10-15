package GUIPrograms.MapContinent;

/**
 * James N 
 * 2020.10.14
 * MapContinent
 */

import game.*;
import game.drawing.Camera;
import game.drawing.Draw;

import GUIPrograms.MapContinent.SimplexNoise;

import java.awt.Color;
import java.util.ArrayList;

public class MapContinent extends GameJava {

    public enum Tiles {
        EMPTY, DEEPOCEAN, OCEAN, LAKE, BEACH, LAND, MOUNTAIN;
    }

    final Color[] COLORS = { new Color(60, 60, 60), new Color(51, 48, 145), new Color(58, 78, 158),
            new Color(53, 138, 166), new Color(143, 166, 68), new Color(61, 145, 65), new Color(112, 128, 107) };

    // just map size, screen can be resized
    final int WIDTH = 800;
    final int HEIGHT = 600;

    // smallest size a tile can be at
    final int RESOLUTION = 16;

    final int SEED = 572648;

    Tiles[][] map;
    Tiles[][] lowRes;
    int scale = RESOLUTION;
    int lastScale = RESOLUTION;

    boolean rendering = false;

    public MapContinent() {
        super(800, 600, 60, 60);

        map = new Tiles[HEIGHT][WIDTH];
        for (double y = 0; y < HEIGHT; y++) {
            for (double x = 0; x < WIDTH; x++) {
                Tiles tile = Tiles.EMPTY;
                double noise = SimplexNoise.noise(x / 500.0, y / 500.0) + SimplexNoise.noise(x / 100.0, y / 100.0) / 2
                        + SimplexNoise.noise(x / 10.0, y / 10.0) / 4;
                // if (noise < -0.5) {
                // tile = Tiles.DEEPOCEAN;
                // } else if (noise < 0.3) {
                // tile = Tiles.OCEAN;
                // } else
                if(noise < 0.3) {
                    tile = Tiles.EMPTY;
                } else if (noise < 0.35) {
                    tile = Tiles.BEACH;
                } else if (noise < 0.8) {
                    tile = Tiles.LAND;
                } else {
                    tile = Tiles.MOUNTAIN;
                }
                map[(int) y][(int) x] = tile;
            }
        }
        renderLowResView();

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new MapContinent();
    }

    @Override
    public void update() {
        int move = 10 / (int) Camera.zoom;
        move = move < 1 ? 1 : move;
        if (Input.keyDown(KeyCodes.W)) {
            Camera.move(0, -move);
        }
        if (Input.keyDown(KeyCodes.S)) {
            Camera.move(0, move);
        }
        if (Input.keyDown(KeyCodes.A)) {
            Camera.move(-move, 0);
        }
        if (Input.keyDown(KeyCodes.D)) {
            Camera.move(move, 0);
        }
        if (Input.scroll != 0) {
            if (Input.scroll < 0) {
                Camera.zoom *= 1.25;
            }
            if (Input.scroll > 0) {
                Camera.zoom *= 0.75;
            }
            Camera.zoom = (float) Utils.limit(Camera.zoom, 48, 1);
            scale = RESOLUTION / (int) Camera.zoom;
            scale = scale < 1 ? 1 : scale;
            if (scale != lastScale) {
                lastScale = scale;
                renderLowResView();
            }
        }

        if (Input.mouseClick(0)) {
            floodFill((int)Input.mousePos.x - gw/2, (int)Input.mousePos.y - gh/2);
            renderLowResView();
        }
    }

    @Override
    public void draw() {
        Draw.setColor(new Color(30, 30, 30));
        Draw.rect(-Camera.x + gw / 2, -Camera.y + gh / 2, gw / (int) Camera.zoom, gh / (int) Camera.zoom);
        int startX = (-Camera.x - (int) (gw / 2 / Camera.zoom)) / scale - 1;
        int startY = (-Camera.y - (int) (gh / 2 / Camera.zoom)) / scale - 1;
        int endX = (-Camera.x + (int) (gw / 2 / Camera.zoom)) / scale + 1;
        int endY = (-Camera.y + (int) (gh / 2 / Camera.zoom)) / scale + 1;

        startX = startX < 0 ? 0 : startX;
        startY = startY < 0 ? 0 : startY;
        endX = endX > lowRes[0].length ? lowRes[0].length : endX;
        endY = endY > lowRes.length ? lowRes.length : endY;

        outer: for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (rendering) {
                    break outer;
                }
                Draw.setColor(COLORS[lowRes[y][x].ordinal()]);
                Draw.rect(x * scale + gw / 2, y * scale + gh / 2, scale, scale);
            }
        }
    }

    @Override
    public void absoluteDraw() {
    }

    // copied from my art editing program https://github.com/rsninja722/rsninja722.github.io/blob/master/notGames/pixelJS/scripts/tools.js 
    void floodFill(int x, int y) {
        int tx = x;
        tx = tx < 0 ? 0 : tx;
        tx = tx > WIDTH - 1 ? WIDTH - 1 : tx;
        int ty = y;
        ty = ty < 0 ? 0 : ty;
        ty = ty > HEIGHT - 1 ? HEIGHT - 1 : ty;
  
        if(map[ty][tx] != Tiles.EMPTY) {
            return;
        }
        Tiles replace = Tiles.LAKE;

        ArrayList<int[]> q = new ArrayList<int[]>();
        int[] start = { tx, ty };
        q.add(start);

        boolean[][] haveGoneTo = new boolean[HEIGHT][WIDTH];
        // 0 = empty 1 = land 2 = water
        int[][] tilesToReplace = new int[HEIGHT][WIDTH];
        for (int y2 = 0; y2 < HEIGHT; y2++) {
            for (int x2 = 0; x2 < WIDTH; x2++) {
                haveGoneTo[y2][x2] = false;
                tilesToReplace[y2][x2] = map[y2][x2] == Tiles.EMPTY ? 0 : 1;
            }
        }

        while (q.size() > 0) {
            int[] n = q.get(q.size() - 1);
            q.remove(q.size() - 1);
            if (haveGoneTo[n[1]][n[0]]) {
                continue;
            }
            tilesToReplace[n[1]][n[0]] = 2;

            if (n[1] < HEIGHT-1) {
                if (tilesToReplace[n[1] + 1][n[0]] == 0) {
                    int[] next = { n[0], n[1] + 1 };
                    q.add(next);
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[0] < WIDTH-1) {
                if (tilesToReplace[n[1]][n[0] + 1] == 0) {
                    int[] next = { n[0] + 1, n[1] };
                    q.add(next);
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[1] > 0) {
                if (tilesToReplace[n[1] - 1][n[0]] == 0) {
                    int[] next = { n[0], n[1] - 1 };
                    q.add(next);
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[0] > 0) {
                if (tilesToReplace[n[1]][n[0] - 1] == 0) {
                    int[] next = { n[0] - 1, n[1] };
                    q.add(next);
                }
            } else {
                replace = Tiles.OCEAN;
            }
            haveGoneTo[n[1]][n[0]] = true;
        }

        for (int y2 = 0; y2 < HEIGHT; y2++) {
            for (int x2 = 0; x2 < WIDTH; x2++) {
                if (tilesToReplace[y2][x2] == 2) {
                    map[y2][x2] = replace;
                }
            }
        }
    }

    // creates a 
    void renderLowResView() {
        int lowResWidth = (int) Math.ceil(WIDTH / scale);
        int lowResHeight = (int) Math.ceil(HEIGHT / scale);

        rendering = true;
        lowRes = new Tiles[lowResHeight][lowResWidth];

        for (int y = 0; y < lowRes.length; y++) {
            for (int x = 0; x < lowRes[0].length; x++) {
                lowRes[y][x] = averageOfMapPoint(x * scale, y * scale);
            }
        }
        rendering = false;
    }

    Tiles averageOfMapPoint(int x, int y) {
        if (scale == 1) {
            return map[y][x];
        }

        int halfScale = scale / 2;
        int startX = x - halfScale;
        int startY = y - halfScale;
        int endX = x + halfScale;
        int endY = y + halfScale;

        startX = startX < 0 ? 0 : startX;
        startY = startY < 0 ? 0 : startY;
        endX = endX > map[0].length ? map[0].length : endX;
        endY = endY > map.length ? map.length : endY;

        int[] counts = new int[Tiles.values().length];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 0;
        }

        for (int y2 = startY; y2 < endY; y2++) {
            for (int x2 = startX; x2 < endX; x2++) {
                counts[map[y2][x2].ordinal()]++;
            }
        }

        int biggest = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] >= counts[biggest]) {
                biggest = i;
            }
        }

        return Tiles.values()[biggest];
    }
}