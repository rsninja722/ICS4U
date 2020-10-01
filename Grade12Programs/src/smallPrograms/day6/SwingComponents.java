package smallPrograms.day6;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 * James N 
 * 2020.10.01 
 * SwingComponents
 * demonstrates various swing components
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SwingComponents {

    int lastSliderVal;

    public static void main(String[] args) {
        new SwingComponents();
    }

    SwingComponents() {
        JFrame frame = new JFrame();
        frame.setSize(500, 150);
        frame.setTitle("components");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // slider
        JSlider slider = new JSlider();
        lastSliderVal = slider.getValue();
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                frame.setLocation(frame.getX() + (lastSliderVal - slider.getValue()) * 3, frame.getY());
                lastSliderVal = slider.getValue();
            }
        });
        frame.add(slider, BorderLayout.NORTH);

        // text field that changes title
        JTextField text = new JTextField();
        text.setSize(100, 30);
        text.setText("Components");
        text.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changeTitle();
            }

            public void removeUpdate(DocumentEvent e) {
                changeTitle();
            }

            public void insertUpdate(DocumentEvent e) {
                changeTitle();
            }

            public void changeTitle() {
                frame.setTitle(text.getText());
            }
        });
        frame.add(text, BorderLayout.SOUTH);

        // panel for center
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);

        // radio buttons
        ButtonGroup group = new ButtonGroup();
        JRadioButton radio1 = new JRadioButton();
        JRadioButton radio2 = new JRadioButton();
        ChangeListener change = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (radio1.isSelected()) {
                    panel.setBackground(Color.ORANGE);
                } else {
                    panel.setBackground(Color.MAGENTA);
                }
            }
        };
        radio1.addChangeListener(change);
        radio2.addChangeListener(change);
        group.add(radio1);
        group.add(radio2);

        panel.add(radio1);
        panel.add(radio2);
        
        // check boxes
        JCheckBox box1 = new JCheckBox();
        JCheckBox box2 = new JCheckBox();
        ActionListener boxChange = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = panel.getGraphics();
                if (box1.isSelected()) {
                    g.setColor(Color.BLUE);
                    g.fillRect(20, 20, 20, 20);
                } else {
                    g.setColor(panel.getBackground());
                    g.fillRect(20, 20, 20, 20);
                }
                if (box2.isSelected()) {
                    g.setColor(Color.GREEN);
                    g.fillRect(50, 20, 20, 20);
                } else {
                    g.setColor(panel.getBackground());
                    g.fillRect(50, 20, 20, 20);
                }
            }
        };
        box1.addActionListener(boxChange);
        box2.addActionListener(boxChange);

        panel.add(box1);
        panel.add(box2);

        // label adding button with option pane
        JButton labelButton = new JButton("add label");
        labelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String labelText = JOptionPane.showInputDialog(null, "enter text to add as label", "add label", JOptionPane.PLAIN_MESSAGE);
                if (labelText != null) {
                    panel.add(new JLabel(labelText));
                    frame.setSize(frame.getWidth()+1, frame.getHeight()+1);
                }
            }
        });
        frame.add(labelButton, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
