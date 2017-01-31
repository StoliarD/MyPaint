package edu.my_paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Dmitry on 02.12.2016.
 */
class DialogMenu {
    static void newButtonMenu(MyFrame myFrame) {
        JFrame frame = new JFrame("'new' menu");
        frame.setLocationRelativeTo(myFrame);
        frame.setSize(200,200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4,1));
        frame.add(new JLabel("       input painting resolution"));

        JPanel panel = new JPanel(new FlowLayout());
        JTextField xField = new JTextField(3);
        JTextField yField = new JTextField(3);
        panel.add(xField); panel.add(yField);
        frame.add(panel);

        JLabel label = new JLabel("");
        frame.add(label);

        JPanel buttons = new JPanel();
        JButton inputButton = new JButton("input");
        JButton defaultButton = new JButton("default");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == inputButton) {
                    try {
                        int x = Integer.parseInt(xField.getText());
                        int y = Integer.parseInt(yField.getText());
                        if (x<1 || y<1 || x>1200 || y>800)
                            throw new NumberFormatException();
                        myFrame.newDrawing(x, y);
                        frame.dispose();
                    } catch (NumberFormatException err) {
                        label.setText("            цыфры блядь!!!!");
                        xField.setText("");
                        yField.setText("");
                        frame.revalidate();
                    }
                } else if (e.getSource() == defaultButton) {
                    myFrame.newDrawing(300,300);
                    frame.dispose();
                }
            }
        };
        inputButton.addActionListener(listener);
        defaultButton.addActionListener(listener);
        buttons.add(inputButton);
        buttons.add(defaultButton);
        frame.add(buttons);

        frame.setVisible(true);
    }

    static void openButtonMenu(MyFrame myFrame) {
        JFrame frame = new JFrame("'open' menu");
        frame.setLocationRelativeTo(myFrame);
        frame.setSize(400,200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4,1));
        frame.add(new JLabel("       input filename"));

        JPanel panel = new JPanel(new FlowLayout());
        JTextField textField = new JTextField(30);
        panel.add(textField);
        frame.add(panel);
        textField.setText("e:\\1.jpg");

        JLabel label = new JLabel("");
        frame.add(label);

        JPanel buttons = new JPanel();
        JButton inputButton = new JButton("input");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == inputButton) {
                    try {
                        if (myFrame.getDrawing() == null)
                            myFrame.newDrawing(1,1);
                        myFrame.getDrawing().openFile(textField.getText());
                        frame.dispose();
                    } catch (IOException err) {
                        System.out.println("1111");
                        label.setText("       wrong filename!!!!");
                        frame.revalidate();
                    }
                }
            }
        };
        inputButton.addActionListener(listener);
        buttons.add(inputButton);
        frame.add(buttons);

        frame.setVisible(true);
    }

    static void saveButtonMenu(MyFrame myFrame) {
        JFrame frame = new JFrame("'save' menu");
        frame.setLocationRelativeTo(myFrame);
        frame.setSize(400,200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4,1));
        frame.add(new JLabel("       input filename"));

        JPanel panel = new JPanel(new FlowLayout());
        JTextField textField = new JTextField(30);
        panel.add(textField);
        frame.add(panel);
        textField.setText("e:\\2.jpg");

        JLabel label = new JLabel("");
        frame.add(label);

        JPanel buttons = new JPanel();
        JButton inputButton = new JButton("input");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == inputButton) {
                    try {
                        if (myFrame.getDrawing() == null)
                            myFrame.newDrawing(1,1);
                        myFrame.getDrawing().saveFile(textField.getText());
                        frame.dispose();
                    } catch (IOException err) {
                        System.out.println("1111");
                        label.setText("       wrong filename!!!!");
                        frame.revalidate();
                    }
                }
            }
        };
        inputButton.addActionListener(listener);
        buttons.add(inputButton);
        frame.add(buttons);

        frame.setVisible(true);
    }

}
