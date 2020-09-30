package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import game.drawing.Camera;
import game.drawing.Draw;
import game.physics.Physics;
import game.physics.Point;


/** listens to keyboard and mouse input */
public class Input {

    /** position of mouse relitive to the top left of the JPanel */
    public static Point rawMousePos = new Point(0,0);
    /** position of mouse in the world (takes into account camera movement)*/
    public static Point mousePos = new Point(0,0);

    // keep track of pressed buttons/keys
    private static byte[] mouseInputs = new byte[3]; 
    private static byte[] keyInputs = new byte[256];

    // adds input listeners to panel
    public static void init() {
        System.out.println("[Input] initializing");

        initKeys();
        
        // mouse presses
        Draw.panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(e.getButton()<4) {
                    mouseInputs[e.getButton()-1] = 2;
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(e.getButton()<4) {
                    mouseInputs[e.getButton()-1] = 0;
                }
            }
        });
        
        // mouse movement
        Draw.panel.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
            public void mouseMoved(MouseEvent e) {
                rawMousePos.x = e.getX();
                rawMousePos.y = e.getY();
            }
        	@Override
            public void mouseDragged(MouseEvent e) {
                rawMousePos.x = e.getX();
                rawMousePos.y = e.getY();
            }
        });

        // Initialize input array 
        for(int i=0;i<mouseInputs.length;i++) {
            mouseInputs[i] = 0;
        }
    }

    // adds key input listeners to panel
    public static void initKeys() {
        Draw.frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()<=256) {
                    keyInputs[e.getKeyCode()] = 2;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { 
                if(e.getKeyCode()<=256) {
                    keyInputs[e.getKeyCode()] = 0;
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
        });

        // Initialize input array 
        for(int i=0;i<keyInputs.length;i++) {
            keyInputs[i] = 0;
        }
    }

    // set the mouse position in the world based on the camera. This math was done 6 months ago in javascript late at night, I have no clue how it works anymore 
    public static void setMousePosition(int x,int y) {
        if(Draw.drawingMode==0) {
            mousePos.x = x-Camera.x;
            mousePos.y = y-Camera.y;
        } else if(Draw.drawingMode==1) {
            double xoff = GameJava.gw/2;
            double yoff = GameJava.gh/2;
            mousePos.x = (int)((x-xoff)/Camera.zoom+xoff)-Camera.x;
            mousePos.y = (int)((y-yoff)/Camera.zoom+yoff)-Camera.y;
        } else {
            double xoff = GameJava.gw/2;
            double yoff = GameJava.gh/2;
            Point tempPos = new Point((int)((x-xoff)/Camera.zoom+xoff)-Camera.x,(int)((y-yoff)/Camera.zoom+yoff)-Camera.y);
    
            Point center = new Point(-Camera.x + GameJava.gw/2, -Camera.y + GameJava.gh/2);
            double tempAngle = Utils.pointTo(center,tempPos) - Camera.angle; 
            double tempDist = Physics.dist(center,tempPos);
    
            mousePos.x = center.x + (int)(Math.cos(tempAngle) * tempDist);
            mousePos.y = center.y + (int)(Math.sin(tempAngle) * tempDist);
        }
    }

    // needs to be called every cycle to make sure clicks are registered properly 
    public static void handleHolding() {
        for(int i=0;i<mouseInputs.length;i++) {
            if(mouseInputs[i] == 2) {
                mouseInputs[i] = 1;
            }
        }
        for(int i=0;i<keyInputs.length;i++) {
            if(keyInputs[i] == 2) {
            	keyInputs[i] = 1;
            }
        }
    }

    /** 
     * @param buttonID 0 = lmb, 1 = middle click, 2 = rmb 
     * @return if that mouse button is held down  
     */
    public static boolean mouseDown(int buttonID) {
        return mouseInputs[buttonID] > 0;
    }
    
    /** 
     * @param buttonID 0 = lmb, 1 = middle click, 2 = rmb 
     * @return only true the first cycle the button is held down 
     */
    public static boolean mouseClick(int buttonID) {
        return mouseInputs[buttonID] == 2;
    }
    
    /** 
     * @param id of a key
     * @return only true the first cycle the button is held down 
     */
    public static boolean keyClick(int keyID) {
        return keyInputs[keyID] == 2;
    }
    /** 
     * @param keyCode {@link game.KeyCodes KeyCodes.keyCodeName}
     * @return only true the first cycle the key is held down 
     */
    public static boolean keyClick(KeyCodes keyCode) {
        return keyInputs[keyCode.getCode()] == 2;
    }
    
    /** 
     * @param id of a key 
     * @return if key is held 
     */
    public static boolean keyDown(int keyID) {
        return keyInputs[keyID] > 0;
    }
    /** 
     * @param keyCode {@link game.KeyCodes KeyCodes.keyCodeName}
     * @return if key is held 
     */
    public static boolean keyDown(KeyCodes keyCode) {
        return keyInputs[keyCode.getCode()] > 0;
    }
}