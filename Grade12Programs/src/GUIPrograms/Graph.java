package GUIPrograms;

import java.awt.Color;

import game.*;
import game.drawing.Draw;

public class Graph extends GameJava {

    double multiX = 1;
    double multiY = 1;
    int step = 5;

    public Graph() {
        super(640, 480, 60, 60);

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new Graph();
    }

    @Override
    public void update() {
        if(Input.keyDown(KeyCodes.LEFT)) {
            multiX *= 1.25;
        }
        if(Input.keyDown(KeyCodes.RIGHT)) {
            multiX *= 0.75;
        }
        if(Input.keyDown(KeyCodes.UP)) {
            multiY *= 1.25;
        }
        if(Input.keyDown(KeyCodes.DOWN)) {
            multiY *= 0.75;
        }
        if(Input.keyClick(KeyCodes.EQUALS)) {
            step -= 1;
        }
        if(Input.keyClick(KeyCodes.MINUS)) {
            step += 1;
        }
        Utils.putInDebugMenu("multi x", multiX);
        Utils.putInDebugMenu("multi y", multiY);
        Utils.putInDebugMenu("step", step);
    }

    @Override
    public void draw() {
        int halfW = gw / 2;
        int halfH = gh / 2;
        Draw.setColor(new Color(30, 30, 30));
        Draw.rect(halfW, halfH, gw, gh);
        Draw.setColor(Color.WHITE);
        Draw.setLineWidth(2);
        Draw.line(halfW, 0, halfW, gh);
        Draw.line(0, halfH, gw, halfH);

        Draw.setFontSize(1);
        for (int i = 0; i < halfW; i += 50) {
            Draw.text(Integer.toString(i), halfW + i, halfH + 10);
            Draw.text(Integer.toString(-i), halfW - i, halfH + 10);
        }
        for (int i = 50; i < halfH; i += 50) {
            Draw.text(Integer.toString(i), halfW + 10, halfH + i);
            Draw.text(Integer.toString(-i), halfW + 10, halfH - i);
        }

        Draw.setLineWidth(1);
        Draw.setColor(new Color(61, 197, 227));
        int lastX = -halfW;
        int lastY = halfH;
        for (int x = lastX; x < halfW; x += step) {
            int y = (int)(y(x*multiX) * multiY) + halfH;
            Draw.line(lastX + halfW, lastY, x + halfW, y);
            lastY = y;
            lastX = x;
        }
    }

    double y(double x) {
        // return (int)(Math.sin(x/20.0)*200.0);
        return (8 * Math.sqrt(x) - 10 * Math.sin(x) - 0.03 * x * x * x - 5);
    }

    @Override
    public void absoluteDraw() {
        // called immediately after draw, all drawing is the same but without the camera
        // affecting anything
    }
}