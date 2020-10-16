package GUIPrograms.MapContinent;

/**
 * James N 
 * 2020.10.14
 * MapContinent
 * 
 * Terrain generated with multiple layers of Simplex noise
 * each pass of noise affects the terrain less, 
 * resulting in interesting terrain at a small and large scale
 * 
 * depending on the zoom level, a different low resolution version of 
 * the map is rendered by finding the most common tile in an area for
 * each low res tile 
 *
 * if window is not displaying properly, resize by dragging first
 * 
 * controls: 
 *      w,a,s,d - pan
 *      scroll  - zoom
 *      lmb     - fill with water
 *      rmb     - toggle tile
 */

import game.*;
import game.drawing.Camera;
import game.drawing.Draw;

import GUIPrograms.MapContinent.SimplexNoise;

import java.awt.Color;
import java.util.ArrayList;

public class MapContinent extends GameJava {

    // just map size, screen can be resized
    final int WIDTH = 800;
    final int HEIGHT = 600;

    // smallest size a tile can be at
    final int RESOLUTION = 16;

    // should noise be randomized
    final boolean RANDOMIZE = true;

    // should water be filled automatically
    final boolean FILLWATER = true;

    // tile information
    final Color[] COLORS = { new Color(60, 60, 60), new Color(51, 48, 145), new Color(58, 78, 158), new Color(53, 138, 166), new Color(143, 166, 68), new Color(61, 145, 65), new Color(112, 128, 107) };

    public enum Tiles {
        EMPTY, DEEPOCEAN, OCEAN, LAKE, BEACH, LAND, MOUNTAIN;
    }

    // tiles stored in map
    Tiles[][] map;
    // this is what is displayed
    Tiles[][] lowRes;

    int scale = RESOLUTION;
    int lastScale = RESOLUTION;

    // avoid drawing null because of multithreading
    boolean rendering = false;

    public MapContinent() {
        super(800, 600, 60, 60);

        frameTitle = "map";

        // randomize noise
        if (RANDOMIZE) {
            for (int i = 0; i < SimplexNoise.p.length; i++) {
                SimplexNoise.p[i] = (short) Utils.rand(0, 255);
            }
            for (int i = 0; i < 512; i++) {
                SimplexNoise.perm[i] = SimplexNoise.p[i & 255];
                SimplexNoise.permMod12[i] = (short) (SimplexNoise.perm[i] % 12);
            }
        }

        // go through and create each tile
        map = new Tiles[HEIGHT][WIDTH];
        for (double y = 0; y < HEIGHT; y++) {
            for (double x = 0; x < WIDTH; x++) {
                Tiles tile = Tiles.EMPTY;

                // find height
                double noise = SimplexNoise.noise(x / 500.0, y / 500.0) + SimplexNoise.noise(x / 100.0, y / 100.0) / 2 + SimplexNoise.noise(x / 10.0, y / 10.0) / 4;

                // set correct tile
                if (noise < -0.5 && FILLWATER) {
                    tile = Tiles.DEEPOCEAN;
                } else if (noise < 0.3) {
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

        // fill ocean and lakes
        if (FILLWATER) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (map[y][x] == Tiles.EMPTY) {
                        floodFill(x, y);
                    }
                }
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
        // panning movement
        int move = 10 / (int) Camera.zoom;
        move = move < 1 ? 1 : move;
        if (Input.keyDown(KeyCodes.W)) {
            Camera.y += move;
        }
        if (Input.keyDown(KeyCodes.S)) {
            Camera.y -= move;
        }
        if (Input.keyDown(KeyCodes.A)) {
            Camera.x += move;
        }
        if (Input.keyDown(KeyCodes.D)) {
            Camera.x -= move;
        }

        // zooming
        if (Input.scroll != 0) {
            if (Input.scroll < 0) {
                Camera.zoom *= 1.25;
            }
            if (Input.scroll > 0) {
                Camera.zoom *= 0.75;
            }
            Camera.zoom = (float) Utils.limit(Camera.zoom, 48, 1);

            // render low resolution view if needed
            scale = RESOLUTION / (int) Camera.zoom;
            scale = scale < 1 ? 1 : scale;
            if (scale != lastScale) {
                lastScale = scale;
                renderLowResView();
            }
        }

        // fill water on left click
        if (Input.mouseClick(0)) {
            floodFill((int) Input.mousePos.x - gw / 2, (int) Input.mousePos.y - gh / 2);
            renderLowResView();
        }

        // toggle tile on right click
        if (Input.mouseClick(2)) {
            // contain position in map
            int posX = (int) Input.mousePos.x - gw / 2;
            posX = posX < 0 ? 0 : posX;
            posX = posX > WIDTH - 1 ? WIDTH - 1 : posX;
            int posY = (int) Input.mousePos.y - gh / 2;
            posY = posY < 0 ? 0 : posY;
            posY = posY > HEIGHT - 1 ? HEIGHT - 1 : posY;

            // toggle
            if (map[posY][posX] == Tiles.EMPTY) {
                map[posY][posX] = Tiles.LAND;
            } else {
                map[posY][posX] = Tiles.EMPTY;
            }

            renderLowResView();
        }
    }

    @Override
    public void draw() {
        Draw.setColor(new Color(30, 30, 30));
        Draw.rect(-Camera.x + gw / 2, -Camera.y + gh / 2, gw / (int) Camera.zoom, gh / (int) Camera.zoom);

        // find bounds to draw within so every tile is not drawn always
        int startX = (-Camera.x - (int) (gw / 2 / Camera.zoom)) / scale - 1;
        int startY = (-Camera.y - (int) (gh / 2 / Camera.zoom)) / scale - 1;
        int endX = (-Camera.x + (int) (gw / 2 / Camera.zoom)) / scale + 1;
        int endY = (-Camera.y + (int) (gh / 2 / Camera.zoom)) / scale + 1;

        startX = startX < 0 ? 0 : startX;
        startY = startY < 0 ? 0 : startY;
        endX = endX > lowRes[0].length ? lowRes[0].length : endX;
        endY = endY > lowRes.length ? lowRes.length : endY;

        // draw tiles
        outer: for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (rendering) {
                    break outer;
                }
                Draw.setColor(COLORS[lowRes[y][x].ordinal()]);
                Draw.rect(x * scale + gw / 2, y * scale + gh / 2, scale, scale);
            }
        }

        // draw cursor
        Draw.setColor(new Color(200, 200, 0, 100));
        Draw.rect((int) Input.mousePos.x, (int) Input.mousePos.y, 1, 1);
    }

    @Override
    public void absoluteDraw() {
    }

    void renderLowResView() {
        int lowResWidth = (int) Math.ceil(WIDTH / scale);
        int lowResHeight = (int) Math.ceil(HEIGHT / scale);

        // make sure draw does not try to draw null
        rendering = true;

        // resize low res
        lowRes = new Tiles[lowResHeight][lowResWidth];

        // find average for each point
        for (int y = 0; y < lowRes.length; y++) {
            for (int x = 0; x < lowRes[0].length; x++) {
                lowRes[y][x] = averageOfMapPoint(x * scale, y * scale);
            }
        }

        rendering = false;
    }

    Tiles averageOfMapPoint(int x, int y) {
        // if its 1:1 return the tile
        if (scale == 1) {
            return map[y][x];
        }

        // find bounds to average within
        int halfScale = scale / 2;
        int startX = x - halfScale;
        int startY = y - halfScale;
        int endX = x + halfScale;
        int endY = y + halfScale;

        startX = startX < 0 ? 0 : startX;
        startY = startY < 0 ? 0 : startY;
        endX = endX > map[0].length ? map[0].length : endX;
        endY = endY > map.length ? map.length : endY;

        // create list for counting tiles
        int[] counts = new int[Tiles.values().length];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 0;
        }

        // count all tiles
        for (int y2 = startY; y2 < endY; y2++) {
            for (int x2 = startX; x2 < endX; x2++) {
                counts[map[y2][x2].ordinal()]++;
            }
        }

        // find what tile occurs most
        int biggest = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] >= counts[biggest]) {
                biggest = i;
            }
        }

        return Tiles.values()[biggest];
    }

    // copied from my art editing program
    // https://github.com/rsninja722/rsninja722.github.io/blob/master/notGames/pixelJS/scripts/tools.js
    void floodFill(int x, int y) {
        int posX = x;
        posX = posX < 0 ? 0 : posX;
        posX = posX > WIDTH - 1 ? WIDTH - 1 : posX;
        int posY = y;
        posY = posY < 0 ? 0 : posY;
        posY = posY > HEIGHT - 1 ? HEIGHT - 1 : posY;

        if (map[posY][posX] != Tiles.EMPTY) {
            return;
        }
        Tiles replace = Tiles.LAKE;

        ArrayList<int[]> q = new ArrayList<int[]>();
        int[] start = { posX, posY };
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

            if (n[1] < HEIGHT - 1) {
                if (tilesToReplace[n[1] + 1][n[0]] == 0) {
                    q.add(new int[] { n[0], n[1] + 1 });
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[0] < WIDTH - 1) {
                if (tilesToReplace[n[1]][n[0] + 1] == 0) {
                    q.add(new int[] { n[0] + 1, n[1] });
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[1] > 0) {
                if (tilesToReplace[n[1] - 1][n[0]] == 0) {
                    q.add(new int[] { n[0], n[1] - 1 });
                }
            } else {
                replace = Tiles.OCEAN;
            }
            if (n[0] > 0) {
                if (tilesToReplace[n[1]][n[0] - 1] == 0) {
                    q.add(new int[] { n[0] - 1, n[1] });
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
}