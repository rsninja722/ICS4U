package GUIPrograms;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.Timer;

import game.*;
import game.drawing.Draw;

public class Timers extends GameJava {

    Timer timer;

    int yPos = 100;

    public Timers() {
        super(640, 480, 60, 60);

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("timer ran");
                yPos = Utils.rand(0, gh);
            }
        };
        timer = new Timer(1000, action);
        timer.start();

        LoopManager.startLoops(this);
    }

    public static void main(String[] args) {
        new Timers();
    }

    @Override
    public void update() {
        // called at the set frame rate
    }

    @Override
    public void draw() {
        Draw.setColor(Color.BLUE);
        Draw.rect(100, yPos, 50, 75);
    }

    @Override
    public void absoluteDraw() {
        // called immediately after draw, all drawing is the same but without the camera
        // affecting anything
    }
}