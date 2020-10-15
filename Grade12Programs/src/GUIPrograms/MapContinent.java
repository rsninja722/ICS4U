package GUIPrograms;

/**
 * James N 
 * 2020.10.14
 * MapContinent
 */

import game.*;
import game.drawing.Camera;
import game.drawing.Draw;

import java.awt.Color;

public class MapContinent extends GameJava {

    public enum Tiles {
        EMPTY, OCEAN, LAKE, LAND;
    }

    final Color[] COLORS = { new Color(60, 60, 60), new Color(53, 138, 166), new Color(58, 78, 158),
            new Color(61, 145, 65) };

    // just map size, screen can be resized
    final int WIDTH = 800;
    final int HEIGHT = 600;

    // smallest size a tile can be at
    final int RESOLUTION = 16;

    Tiles[][] map;
    Tiles[][] lowRes;

    public MapContinent() {
        super(800, 600, 60, 60);

        Tiles[] t = Tiles.values();
        int l = t.length-1;
        map = new Tiles[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                map[y][x] = t[Utils.rand(0, l)];
            }
        }

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new MapContinent();
    }

    @Override
    public void update() {
        if (Input.keyDown(KeyCodes.W)) {
            Camera.move(0, -10 / (int) Camera.zoom);
        }
        if (Input.keyDown(KeyCodes.S)) {
            Camera.move(0, 10 / (int) Camera.zoom);
        }
        if (Input.keyDown(KeyCodes.A)) {
            Camera.move(-10 / (int) Camera.zoom, 0);
        }
        if (Input.keyDown(KeyCodes.D)) {
            Camera.move(10 / (int) Camera.zoom, 0);
        }
        if (Input.scroll != 0) {
            Camera.zoom -= Input.scroll / 5.0;
            if(Camera.zoom < 1) {
                Camera.zoom = 1;
            }
        }

        Utils.putInDebugMenu("sx", -Camera.x - (int)(gw/2/Camera.zoom));
        Utils.putInDebugMenu("ex", -Camera.x + (int)(gw/2/Camera.zoom));
    }

    @Override
    public void draw() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                Draw.setColor(COLORS[map[y][x].ordinal()]);
                Draw.rect(x,y,1,1);
            }
        }
    }

    @Override
    public void absoluteDraw() {}

    void renderLowResView() {
        int scale = RESOLUTION / (int)Camera.zoom;
        scale = scale < 1 ? 1 : scale;

        int startX = -Camera.x - (int)(gw/Camera.zoom);

        // for(int y = )
    }
}