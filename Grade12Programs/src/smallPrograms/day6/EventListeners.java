package smallPrograms.day6;

/**
 * James N 
 * 2020.10.01 
 * EventListeners
 * add various event listeners to buttons
 */

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import javax.swing.JButton;

public class EventListeners  {

    JFrame frame;
    JPanel panel;
    JButton btn1;
    JButton btn2;
    JButton btn3;

    int counter = 0;

    public static void main(String[] args) {
        new EventListeners();
    }

    EventListeners() {
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setTitle("count: 0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel();
        
        panel.setBackground(Color.YELLOW);

        JButton btn1 = new JButton("Increment");
        JButton btn2 = new JButton("Change Background");
        JButton btn3 = new JButton("Move");

        // title changing button
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                counter++;
                frame.setTitle("counter : " + counter);
            }
        });

        // background changing button
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(panel.getBackground().getRGB() == Color.BLUE.getRGB()) {
                    panel.setBackground(Color.YELLOW);
                } else {
                    panel.setBackground(Color.BLUE);
                }
                panel.repaint();
               
            }
        });

        // moving down listener
        ActionListener moveAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JButton moveButton = arg0.getActionCommand() == "Move" ? btn3 : btn2;
                moveButton.setLocation(moveButton.getLocation().x, moveButton.getLocation().y + 100);
                if(moveButton.getLocation().y > panel.getHeight()) {
                    moveButton.setLocation(moveButton.getLocation().x, 0);
                }
            }
        };

        btn2.addActionListener(moveAction);
        btn3.addActionListener(moveAction);
        
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);

        frame.add(panel);

        frame.setVisible(true);
    }
}
