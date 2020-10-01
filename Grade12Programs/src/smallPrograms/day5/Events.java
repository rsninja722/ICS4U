package smallPrograms.day5;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * James N
 * 2020.09.30
 * Events 
 *
 */


public class Events extends JFrame {

    JPanel panel;

    public static void main(String[] args) {
        new Events();
    }

    Events() {
        this.setSize( 500,500);
        this.setTitle("events");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        
        JButton btn1 = new JButton("Go");
        JButton btn2 = new JButton("Stop");
        
        Listener listner = new Listener();

        btn1.addActionListener(listner);
        btn2.addActionListener(listner);

        panel.add(btn1);
        panel.add(btn2);

        this.add(panel);

        this.setVisible(true);
    }

    class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Go")) {
                panel.setBackground(Color.BLUE);
            } else {
                panel.setBackground(Color.YELLOW);
            }
            panel.repaint();

        }
    }
}
