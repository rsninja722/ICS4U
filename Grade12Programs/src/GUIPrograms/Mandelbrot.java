package GUIPrograms;


/**
 * James N 
 * arrow keys to pan
 * w/s to zoom
 * a/d to change steps
 * f3 for debug
 */

import game.*;
import game.drawing.Draw;

import java.awt.image.BufferedImage;
import java.util.stream.StreamSupport;
import java.awt.Color;

public class Mandelbrot extends GameJava {

    BufferedImage img;

    int steps = 100;

    double realMin = -2;
    double realMax = 1;
    double imgnryMin = -1.5;
    double imgnryMax;

    double realFactor;
    double imgnryFactor;

    double zoom = 0.5;

    public Mandelbrot() {
        super(600, 600, 60, 60);

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new Mandelbrot();
    }

    @Override
    public void update() {
        zoom = -(realMin - realMax) / 50;

        float speed = 0.4f;
        if (Input.keyDown(KeyCodes.LEFT)) {
            realMax -= zoom * speed;
            realMin -= zoom * speed;
        }
        if (Input.keyDown(KeyCodes.RIGHT)) {
            realMax += zoom * speed;
            realMin += zoom * speed;
        }
        if (Input.keyDown(KeyCodes.DOWN)) {
            imgnryMax -= zoom * speed;
            imgnryMin -= zoom * speed;
        }
        if (Input.keyDown(KeyCodes.UP)) {
            imgnryMax += zoom * speed;
            imgnryMin += zoom * speed;
        }
        if (Input.keyDown(KeyCodes.W)) {
            realMin += zoom * speed;
            realMax -= zoom * speed;
            imgnryMin += zoom * (speed / 2.0);
        }
        if (Input.keyDown(KeyCodes.S)) {
            realMin -= zoom * speed;
            realMax += zoom * speed;
            imgnryMin -= zoom * (speed / 2.0);
        }
        
        if (Input.keyClick(KeyCodes.D)) {
            steps *= 2;
        }
        if (Input.keyClick(KeyCodes.A)) {
            steps /= 2;
            if (steps < 10) {
                steps = 10;
            }
        }

        imgnryMax = imgnryMin + (realMax - realMin) * gh / gw;

        realFactor = (realMax - realMin) / (gw - 1);
        imgnryFactor = (imgnryMax - imgnryMin) / (gh - 1);

        Utils.putInDebugMenu("steps", steps);
    }

    @Override
    public void draw() {
        img = new BufferedImage(gw, gh, BufferedImage.TYPE_INT_RGB);

        int black = Color.BLACK.getRGB();
        int white = Color.WHITE.getRGB();

        for (int y = 0; y < gh; y++) {
            double imgnryC = imgnryMax - y * imgnryFactor;
            for (int x = 0; x < gw; x++) {
                double realC = realMin + x * realFactor;

                double Z_re = realC, Z_im = imgnryC;
                boolean isInside = true;
                int n;
                for (n = 0; n < steps; ++n) {
                    double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
                    if (Z_re2 + Z_im2 > 4) {
                        isInside = false;
                        break;
                    }
                    Z_im = 2 * Z_re * Z_im + imgnryC;
                    Z_re = Z_re2 - Z_im2 + realC;
                }

                int c;
                if (isInside) {
                    c = black;
                } else {
                    c = Color.getHSBColor((n%255) / 256.0f, 1.0f, 0.5f).getRGB();
                }

                // set pixel
                img.setRGB(x, y, c);
            }
        }

        Draw.canvas.drawImage(img, 0, 0, null);
    }

    @Override
    public void absoluteDraw() {
        // called immediately after draw, all drawing is the same but without the camera
        // affecting anything
    }
}
