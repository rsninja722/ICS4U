package game.drawing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.FontFormatException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.GameJava;
import game.Input;
import game.physics.Circle;
import game.physics.Point;
import game.physics.Rect;

/** contains methods for drawing to the screen */
public class Draw extends JPanel {

    private static final long serialVersionUID = 1L;

    public static JPanel panel;

    public static JFrame frame;

    private static Font drawFont; // font used for all text drawing

    public static boolean fullScreen = false; // if in fullscreen mode

    public static int windowedWidth; // width to return to when exiting full screen
    public static int windowedHeight; // width to return to when exiting full screen

    private static VolatileImage buffer; // final buffer
    private static VolatileImage buffer1; // buffer used for scaling
    private static VolatileImage buffer2; // buffer used for rotation

    private static Graphics2D bufferGraphics; // final buffer
    private static Graphics2D buffer1Graphics; // buffer used for scaling
    private static Graphics2D buffer2Graphics; // buffer used for rotation
    
    private static int lastScreenIndex = -1;
    private static int lastW = 0;
    private static int lastH = 0;    

    public static Graphics2D canvas; // graphics2D of the current buffer being used to draw

    private static int desiredWidth = 0;
    private static int desiredHeight = 0;

    // drawing offset
    private static int difx = 0;
    private static int dify = 0;

    public static int drawingMode = 0; // 0 = translations only, 1 = adding scaling, 2 = adding rotations

    // limit for when to stop drawing
    public static int drawLimitLeft;
    public static int drawLimitRight;
    public static int drawLimitTop;
    public static int drawLimitBottom;

    public static boolean absoluteDraw = false; // whether camera should be ignored
    
    public static float alphaBetweenFrames = 1.0f;
    public static float lastAlpha = alphaBetweenFrames;
    
    public static boolean antialiasing = false;

    public static GraphicsEnvironment graphicsEnviro = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static GraphicsConfiguration graphicsConfig = graphicsEnviro.getDefaultScreenDevice().getDefaultConfiguration();
    
    public static int drawCalls = 0;

    // __________________________________________ Drawing methods __________________________________________

    /**
     * sets the color to use for drawing
     * @param c Color
     */
    public static void setColor(Color c) {
        canvas.setColor(c);
    }

    /**
     * draws a rectangle centered on x,y
     * @param x x position
     * @param y y position
     * @param w width
     * @param h height
     */
    public static void rect(int x, int y, int w, int h) {
        canvas.fillRect(x - (w / 2) + difx, y - (h / 2) + dify, w, h);
    }
    
    /**
     * draws a rectangle outline centered on x,y
     * @param x x position
     * @param y y position
     * @param w width
     * @param h height
     */
    public static void rectOutline(int x, int y, int w, int h) {
        canvas.drawRect(x - (w / 2) + difx, y - (h / 2) + dify, w, h);
    }

    /**
     * draws a rectangle centered on x,y
     * @param rectangle {@link game.physics.Rect#Rect rectangle}
     */
    public static void rect(Rect rectangle) {
        canvas.fillRect((int) rectangle.x - (rectangle.w / 2) + difx, (int) rectangle.y - (rectangle.h / 2) + dify, rectangle.w, rectangle.h);
    }
    
    /**
     * draws a rectangle outline centered on x,y
     * @param rectangle {@link game.physics.Rect#Rect rectangle}
     */
    public static void rectOutline(Rect rectangle) {
        canvas.drawRect((int) rectangle.x - (rectangle.w / 2) + difx, (int) rectangle.y - (rectangle.h / 2) + dify, rectangle.w, rectangle.h);
    }

    /**
     * draws circle centered on x,y
     * @param x x position
     * @param y y position
     * @param r radius
     */
    public static void circle(int x, int y, int r) {
        canvas.fillOval(x - r + difx, y - r + dify, r * 2, r * 2);
    }
    
    /**
     * draws circle outline centered on x,y
     * @param x x position
     * @param y y position
     * @param r radius
     */
    public static void circleOutline(int x, int y, int r) {
        canvas.drawOval(x - r + difx, y - r + dify, r * 2, r * 2);
    }

    /**
     * draws circle centered on x,y
     * @param circle {@link game.physics.Circle#Circle circle}
     */
    public static void circle(Circle circle) {
        canvas.fillOval((int) circle.x - circle.r + difx, (int) circle.y - circle.r + dify, circle.r * 2, circle.r * 2);
    }
    
    /**
     * draws circle outline centered on x,y
     * @param circle {@link game.physics.Circle#Circle circle}
     */
    public static void circleOutline(Circle circle) {
        canvas.drawOval((int) circle.x - circle.r + difx, (int) circle.y - circle.r + dify, circle.r * 2, circle.r * 2);
    }

    /**
     * draws and arc from startAngle to arcAngle
     * @param x
     * @param y
     * @param size
     * @param startAngle in degrees
     * @param arcAngle in degrees
     */
    public static void arc(int x, int y, int size, int startAngle, int arcAngle) {
    	canvas.drawArc(x + difx - size/2, y + dify - size/2, size, size, startAngle, arcAngle);
    }

    /**
     * sets the width lines are drawn at
     * @param width
     */
    public static void setLineWidth(float width) {
        canvas.setStroke(new BasicStroke(width));
    }

    /**
     * draws a line from x1,y1 to x2,y2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public static void line(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1 + difx, y1 + dify, x2 + difx, y2 + dify);
    }

    /**
     * draws a line from start to end
     * @param start {@link game.physics.Point#Point point}
     * @param end   {@link game.physics.Point#Point point}
     */
    public static void line(Point start, Point end) {
        canvas.drawLine((int)start.x + difx, (int)start.y + dify, (int)end.x + difx, (int)end.y + dify);
    }

    /**
     * sets the font size to 8 x size (pixelmix font will only be pixelated at sizes that are factors of 8)
     * @param size
     */
    public static void setFontSize(int size) {
        canvas.setFont(drawFont.deriveFont(size * 8.0f));
    }

    /**
     * draws a string using x,y as the bottom left
     * @param text
     * @param x    left position of the text
     * @param y    bottom position of the text
     */
    public static void text(String text, int x, int y) {
        canvas.drawString(text, x + difx, y + dify);
    }

    /**
     * @param text string to measure width of
     * @return width of text in pixels
     */
    public static int getWidthOfText(String text) {
        return canvas.getFontMetrics().stringWidth(text);
    }

    /**
     * draws an image centered on x,y with an angle, scaled on the x and y axis.
     * will not prevent drawing if sprite is off screen.
     * @param spr   {@link game.drawing.Sprites#get(String) Sprites.get(sprite name)}
     * @param x
     * @param y
     * @param angle radians
     * @param scale pixels per pixel
     */
    public static void imageIgnoreCutoff(String imageName, int x, int y, double angle, double scale) {
        AffineTransform t = canvas.getTransform();

        canvas.translate(Math.round(x + difx), Math.round(y + dify));
        canvas.scale(scale, scale);
        canvas.rotate(angle);

        Sprite spr = Sprites.get(imageName);
        canvas.drawImage(spr.img, Math.round(-spr.width / 2), Math.round(-spr.height / 2), null);

        canvas.setTransform(t);drawCalls++;
    }

    /**
     * draws an image centered on x,y. will not prevent drawing if off screen.
     * @param spr {@link game.drawing.Sprites#get(String) Sprites.get(sprite name)}
     * @param x
     * @param y
     */
    public static void imageIgnoreCutoff(String imageName, int x, int y) {
    	Sprite spr = Sprites.get(imageName);
        canvas.drawImage(spr.img, Math.round(x + difx - (spr.width / 2)), Math.round(y + dify - (spr.height / 2)), null);drawCalls++;
    }

    /**
     * draws an image centered on x,y with an angle, scaled on the x and y axis.
     * @param spr   {@link game.drawing.Sprites#get(String) Sprites.get(spritename)}
     * @param x
     * @param y
     * @param angle radians
     * @param scale pixels per pixel
     */
    public static void image(String imageName, int x, int y, double angle, double scale) {
    	Sprite spr = Sprites.get(imageName);
    	
        int half = (int) (spr.drawLimit * scale);
        if ((x + half > drawLimitLeft && x - half < drawLimitRight && y + half > drawLimitTop && y - half < drawLimitBottom) || absoluteDraw) {
            AffineTransform t = canvas.getTransform();

            canvas.translate(Math.round(x + difx), Math.round(y + dify));
            canvas.scale(scale, scale);
            canvas.rotate(angle);

            canvas.drawImage(spr.img, Math.round(-spr.width / 2), Math.round(-spr.height / 2), null);

            canvas.setTransform(t);
        drawCalls++;}
    }

    /**
     * draws an image centered on x,y
     * @param spr {@link game.drawing.Sprites#get(String) Sprites.get(sprite name)}
     * @param x
     * @param y
     */
    public static void image(String imageName, int x, int y) {
    	Sprite spr = Sprites.get(imageName);
    	
        int half = spr.drawLimit;
        if ((x + half > drawLimitLeft && x - half < drawLimitRight && y + half > drawLimitTop && y - half < drawLimitBottom) || absoluteDraw) {
            canvas.drawImage(spr.img, Math.round(x + difx - (spr.width / 2)), Math.round(y + dify - (spr.height / 2)), null);
        drawCalls++;}
    }

    /**
     * Sets the size of the window
     * @param w
     * @param h
     */
    public static void setWindowSize(int w, int h) {
        desiredWidth = w;
        desiredHeight = h;
    }

    // __________________________________________ methods that should only be used by gamej __________________________________________

    // creates window and sets up buffers
    public static void init() {
        System.out.println("[Draw] initizlizing");

        // Andrew Tyler www.AndrewTyler.net and font@andrewtyler.net
        try {
            graphicsEnviro.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(GameJava.baseDirectory + GameJava.directoryChar + "pixelmix.ttf")));
            drawFont = new Font("pixelmix", Font.PLAIN, 8);
        } catch (IOException | FontFormatException e) {
            System.err.println("[Draw] cannot find pixelmix.ttf in assets, using default font instead");
            drawFont = new Font("Dialog", Font.PLAIN, 8);
        }

        panel = new JPanel(new BorderLayout());

        buffer = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
        buffer1 = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
        buffer2 = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);

        canvas = buffer.createGraphics();

        panel.setBackground(Color.WHITE);

        frame = new JFrame(GameJava.frameTitle);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setUndecorated(false);
        frame.setVisible(true);
        frame.pack();

        frame.setSize(GameJava.gw, GameJava.gh);

        frame.setLocationRelativeTo(null);

        windowedWidth = GameJava.gw;
        windowedHeight = GameJava.gh;
        
        int w = frame.getWidth() - frame.getInsets().right - frame.getInsets().left;
        int h = frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom;
        panel.setSize(w, h);
        GameJava.gw = panel.getWidth();
        GameJava.gh = panel.getHeight();
    }

    public static void toggleFullSreen() {
        // toggle full screen
        fullScreen = !fullScreen;
        // destroy and remake jframe
        frame.dispose();
        frame = new JFrame(GameJava.frameTitle);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setUndecorated(fullScreen);
        frame.setVisible(true);
        frame.pack();

        // if entering windowed mode, set size to size set in GameJava init
        if (!fullScreen) {
            frame.setSize(windowedWidth, windowedHeight);
        }

        frame.setLocationRelativeTo(null);

        // if resizable has been turned off
        if(GameJava.resizable == false) {
            frame.setResizable(false);
        }

        // set or unset frame to be the fullscreen window
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        System.out.println(device);
        if (fullScreen) {
            device.setFullScreenWindow(Draw.frame);
            graphicsEnviro = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsConfig = graphicsEnviro.getDefaultScreenDevice().getDefaultConfiguration();
	        
	        buffer = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
	        bufferGraphics = buffer.createGraphics();
        } else {
            device.setFullScreenWindow(null);
        }

        // add the key listeners back to the frame
        Input.initKeys();
    }

    // sets up buffers
    public static void preRender() {drawCalls=0;absoluteDraw=false;
        // change what buffer is used depending in the camera
        if (Camera.zoom < 1.0f) {
            Camera.zoom = 1.0f;
        }
        if (Camera.angle != 0) {
            drawingMode = 2;
        } else if (Camera.zoom != 1) {
            drawingMode = 1;
        } else {
            drawingMode = 0;
        }

        // set size of panel to fit frame
        if(desiredWidth != 0 || desiredHeight != 0) {
            windowedWidth = desiredWidth < 10 ? 640 : desiredWidth;
            windowedHeight = desiredHeight < 10 ? 480 : desiredHeight;
            frame.setSize(windowedWidth, windowedHeight);
            desiredWidth = 0;
            desiredHeight = 0;
        } 
        int w = frame.getWidth() - frame.getInsets().right - frame.getInsets().left;
        int h = frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom;
        w = w < 10 ? windowedWidth : w;
        h = h < 10 ? windowedHeight : h;
        panel.setSize(w, h);
        GameJava.gw = panel.getWidth();
        GameJava.gh = panel.getHeight();

        // recalculate the biggest of width and height, and get the difference (this is
        // used for the buffer sizes)
        int maxCvsSize = Math.max(GameJava.gw, GameJava.gh);
        int sizeDif = maxCvsSize - Math.min(GameJava.gw, GameJava.gh);

        // calculate the size buffer 2 should be, so it can be rotated and not miss
        // drawing in corners
        double tempSize = maxCvsSize / Camera.zoom;
        double tempSizeAndPadding = tempSize + (tempSize / 2);
        
        // account for window being moved to different screen https://stackoverflow.com/questions/2234476/how-to-detect-the-current-display-with-java
        GraphicsDevice currentScreen = frame.getGraphicsConfiguration().getDevice();
        GraphicsDevice[] allScreens = graphicsEnviro.getScreenDevices();
        int screenIndex = -1;
        for (int i = 0; i < allScreens.length; i++) {
            if (allScreens[i].equals(currentScreen))
            {
                screenIndex = i;
                break;
            }
        }
        if(lastScreenIndex != screenIndex) {
        	lastScreenIndex = screenIndex; 
        	
	        graphicsConfig = allScreens[screenIndex].getDefaultConfiguration();
	        
	        buffer = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
	        buffer1 = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
	        buffer2 = graphicsConfig.createCompatibleVolatileImage((int) tempSizeAndPadding, (int) tempSizeAndPadding);
	        bufferGraphics = buffer.createGraphics();
	        buffer1Graphics = buffer1.createGraphics();
	        buffer2Graphics = buffer2.createGraphics();
        }
        

        // resize buffers
        boolean sizeChange = GameJava.gw != lastW || GameJava.gh != lastH;
        lastW = GameJava.gw;
        lastH = GameJava.gh;
        if (drawingMode == 2) {
        	if(buffer2Graphics == null || sizeChange) {
	            buffer2 = graphicsConfig.createCompatibleVolatileImage((int) tempSizeAndPadding, (int) tempSizeAndPadding);
	            buffer2Graphics = buffer2.createGraphics();
        	}
        }
        if (drawingMode > 0) {
        	if(buffer1Graphics == null || sizeChange) {
	            buffer1 = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
	            buffer1Graphics = buffer1.createGraphics();
        	}
        }
        if(bufferGraphics == null || sizeChange) {
        	buffer = graphicsConfig.createCompatibleVolatileImage(GameJava.gw, GameJava.gh);
	        bufferGraphics = buffer.createGraphics();
        }
        
        // set translations for drawing
        if (drawingMode == 2) {
            // account for buffer2 being a square
            difx = Camera.x + (int) (tempSizeAndPadding - GameJava.gw) / 2;
            dify = Camera.y + (int) (tempSizeAndPadding - GameJava.gh) / 2;
        } else {
            difx = Camera.x;
            dify = Camera.y;
        }

        // calculate limits, if image is outside these bounds, it will not draw
        int limitModifyer = 0;
        if(drawingMode==2) {limitModifyer=Math.max(buffer2.getHeight(), buffer2.getWidth())-maxCvsSize;}
        drawLimitLeft = -Camera.x - (drawingMode == 2 ? sizeDif : 0) - limitModifyer;
        drawLimitRight = -Camera.x + maxCvsSize + (drawingMode == 2 ? sizeDif : 0) + limitModifyer;
        drawLimitTop = -Camera.y - (drawingMode == 2 ? sizeDif : 0) - limitModifyer;
        drawLimitBottom = -Camera.y + maxCvsSize + (drawingMode == 2 ? sizeDif : 0) + limitModifyer;

        // set the drawing target
        switch (drawingMode) {
            case 0:
                canvas = bufferGraphics;
                break;
            case 1:
                canvas = buffer1Graphics;
                break;
            case 2:
                canvas = buffer2Graphics;
                break;
        }
        
        // turn on anti alliasing
 		if(antialiasing) {
 			Draw.canvas.setRenderingHint(
                 RenderingHints.KEY_ANTIALIASING, 
                 RenderingHints.VALUE_ANTIALIAS_ON);
 		}

        // set font
        canvas.setFont(drawFont);
        
        bufferGraphics.clearRect(0, 0, GameJava.gw, GameJava.gh);
        buffer1Graphics.clearRect(0, 0, GameJava.gw, GameJava.gh);
        buffer2Graphics.clearRect(0, 0, (int) tempSizeAndPadding, (int) tempSizeAndPadding);
    }

    // draws buffers onto main buffer to account for camera zooming and rotating
    public static void renderCameraMovement() {
        switch (drawingMode) {
            case 1:
                imgRotScale(GameJava.gw / 2, GameJava.gh / 2, 0, Camera.zoom, buffer1, bufferGraphics);
                break;
            case 2:
                imgRotScale(GameJava.gw / 2, GameJava.gh / 2, Camera.angle, 1, buffer2, buffer1Graphics);
                imgRotScale(GameJava.gw / 2, GameJava.gh / 2, 0, Camera.zoom, buffer1, bufferGraphics);
                break;
        }
    }

    private static void imgRotScale(double x, double y, double angle, double scale, VolatileImage pic, Graphics2D ctx) {
        AffineTransform t = ctx.getTransform();
        ctx.translate(x, y);
        ctx.scale(scale, scale);
        ctx.rotate(angle);
        ctx.drawImage(pic, -pic.getWidth() / 2, -pic.getHeight() / 2, null);
        ctx.setTransform(t);
    }

    // removes all offsets and sets drawing target to buffer
    public static void preAbsoluteRender() {
        difx = 0;
        dify = 0;
        absoluteDraw = true;
        canvas = bufferGraphics;
    }

    // draws the buffer to the jpanel
    public static void renderToScreen() {
        Graphics2D g2 = (Graphics2D) panel.getGraphics();
        if(g2 != null) {
            if(lastAlpha != 1.0f || alphaBetweenFrames != 1.0f) {
                int rule = AlphaComposite.SRC_OVER;
                Composite comp = AlphaComposite.getInstance(rule , (float) alphaBetweenFrames );
                g2.setComposite(comp);
                lastAlpha = alphaBetweenFrames;
            }
            g2.drawImage(buffer, 0, 0, null);
            g2.dispose();
        }
    }
}
