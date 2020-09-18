package smallPrograms.day3;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class generativeArt extends JPanel {
    static final int W = 512;
    static final int H = 512;
    static final float STEP = 4;

    static BufferedImage img;

    static ArrayList<Color> colors = new ArrayList<>();;

    static final long serialVersionUID = 1L;

    static JPanel panel;

    static JFrame frame;

    public static void main(String[] Args) {

        panel = new JPanel(new BorderLayout());

        frame = new JFrame("art");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setUndecorated(false);
        frame.setVisible(true);
        frame.pack();

        frame.setSize(W, H);

        frame.setLocationRelativeTo(null);

        img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

        int r = 0;
        int b = 0;
        int g = 0;

        for (int i = 0; i < W * H; i++) {
            colors.add(new Color(r,g,b));
            r += STEP;
            if (r > 255) {
                r = 0;
                b += STEP;
                if (b > 255) {
                    b = 0;
                    g += STEP;
                }
            }
        }

        float tolerance = 5;
        Color lastColor = colors.get(0);
        int i=0;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {

                int size = colors.size() - 1;
                i = (int) Math.min(Math.max(0, x-y), size);
                Color c = colors.get(i);

                int lastColoVal = lastColor.getRed() + lastColor.getGreen() + lastColor.getBlue();
                while (Math.abs(c.getRed() + c.getGreen() + c.getBlue() - lastColoVal) > tolerance) {
                    i = Math.min(Math.max(0, --i), size);
                    if(i==0) {
                        i = size;
                    }
                    c = colors.get(i);
                    tolerance += 0.1;
                }

                lastColor = colors.get(i);
                img.setRGB(x, y, colors.get(i).getRGB());
                
                colors.remove(i);
                tolerance = 1;
            }

            if (y % 4 == 0) {
                System.out.println(y + "/512");
            }
        }

        Graphics2D g2 = (Graphics2D) panel.getGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream("output.png"))) {
            ImageIO.write(img, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double mapRange(double value, double valueLow, double valueHigh, double remappedLow,
            double remappedHigh) {
        return remappedLow + (remappedHigh - remappedLow) * (value - valueLow) / (valueHigh - valueLow);
    }
}