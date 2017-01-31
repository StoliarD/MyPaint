package edu.my_paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by Dmitry on 02.12.2016.
 */
class MyFrame extends JFrame {
    private Drawing drawing;
    Drawing getDrawing() {
        return drawing;

    }

    private MyFrame myFrame; // because 'this' cannot be applied in method DialogMenu.newButtonMenu

    MyFrame() throws HeadlessException {
        super("MyPaint");
        myFrame = this;
        setSize(513, 513);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-400,
                Toolkit.getDefaultToolkit().getScreenSize().height/2-350);
        setResizable(true);
        setVisible(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    void newDrawing(int x, int y) {
        System.out.println("new drawing");
        if (drawing == null) {
            System.out.println("null");
            drawing = new Drawing(x,y);
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(drawing);
            this.add(panel, BorderLayout.CENTER);
            revalidate();
        } else {
            drawing.initialize(x,y);
            revalidate();

        }
    }

    void createGUI() {
        System.out.println("starting...    ");
        prepareMenu();
        prepareToolPanel();
        System.out.println("done!");
    }

    private void prepareMenu() {
        JButton newButton = new JButton("new");
        JButton openButton = new JButton("open");
        JButton saveButton = new JButton("save");
        JButton clearButton = new JButton("clear");
        JButton undoButton = new JButton("undo(deprecated)");

        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button == newButton){
                    DialogMenu.newButtonMenu(myFrame);
//                    newDrawing(300,300);
                } else if (button == openButton) {
                    DialogMenu.openButtonMenu(myFrame);
                } else if (button == saveButton) {
                    DialogMenu.saveButtonMenu(myFrame);
                } else if (button == clearButton) {
                    if (drawing != null)
                        drawing.clear();
                } else if (button == undoButton) {
                    if (drawing != null)
                        drawing.undo();
                }
                if (drawing != null){
                    drawing.drawObj.shapeAndStyleHighlighted = null;
                    drawing.repaint();
                }
            }
        };

        newButton.addActionListener(menuListener);
        openButton.addActionListener(menuListener);
        saveButton.addActionListener(menuListener);
        clearButton.addActionListener(menuListener);
        undoButton.addActionListener(menuListener);
        JToolBar menuPanel = new JToolBar(SwingConstants.HORIZONTAL);//(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setFloatable(false);
        menuPanel.addSeparator();
        menuPanel.add(newButton);
        menuPanel.add(openButton);
        menuPanel.add(saveButton);
        menuPanel.add(clearButton);
//        menuPanel.add(Box.createHorizontalStrut(50));
        menuPanel.addSeparator();
        menuPanel.add(undoButton);

        this.add(menuPanel, BorderLayout.NORTH);
        revalidate();
    }

    private void prepareToolPanel() {
        Dimension buttonsSize = new Dimension(75,30);
        //create buttons
        JButton brushButton = new JButton("Brush");
        JButton fillRectButton = new JButton("fillRect");
        JButton refactorButton = new JButton("color");
        JButton moveButton = new JButton("move");
        JButton rotateButton = new JButton("rot");
        JButton deleteButton = new JButton("delete");


        // create color buttons
        HashMap<Color,JButton> colorMap = new HashMap<>();
        JButton colorButton;
        for (Color color : Drawing.colors){
            colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(25,25));
            colorMap.put(color, colorButton);
        }
        JLabel colorLabel = new JLabel(Drawing.color.toString().substring(14));

        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                if (button == brushButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.BRUSH);
                    stopHighlight();
                } else if (button == fillRectButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.FILL_RECT);
                    stopHighlight();
                } else if (button == refactorButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.REFACTOR);
                    if (drawing.drawObj.shapeAndStyleHighlighted != null) {
                        drawing.acceptRefactor();
                    }
                } else if (button == moveButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.MOVE);
                } else if (button == rotateButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.ROTATE);
                } else if (button == deleteButton) {
                    Drawing.setDrawMode(Drawing.DrawingTools.DELETE);
                } else if (colorMap.values().contains(button)) {
                    for (Color color : Drawing.colors) {
                        if (colorMap.get(color) == e.getSource()){
                            if (drawing != null) {
                                if (drawing.drawObj.shapeAndStyleHighlighted != null) {
                                    drawing.drawObj.shapeAndStyleHighlighted.setColor(color);
                                    drawing.acceptRefactor();
                                    drawing.repaint();
                                } else
                                    drawing.setColor(color);
                            } else {
                                Drawing.color = color;
                            }
                            colorLabel.setText(Drawing.color.toString().substring(14));
                        }
                    }
                }
//                drawing.repaint();
            }
        };


        Box toolBox = Box.createVerticalBox();
        toolBox.add(Box.createVerticalStrut(20));
        int n=0;

        // init Buttons
        brushButton.setPreferredSize(buttonsSize);
        fillRectButton.setPreferredSize(buttonsSize);
        refactorButton.setPreferredSize(buttonsSize);
        deleteButton.setPreferredSize(buttonsSize);
        moveButton.setPreferredSize(buttonsSize);
        rotateButton.setPreferredSize(buttonsSize);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brushButton.addActionListener(menuListener);
        panel.add(brushButton);
        fillRectButton.addActionListener(menuListener);
        panel.add(fillRectButton);
        toolBox.add(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refactorButton.addActionListener(menuListener);
        panel.add(refactorButton);
        deleteButton.addActionListener(menuListener);
        panel.add(deleteButton);
        toolBox.add(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        moveButton.addActionListener(menuListener);
        rotateButton.addActionListener(menuListener);
        panel.add(moveButton);
        panel.add(rotateButton);
        toolBox.add(panel);



        //init and add colorbuttons to panel
        for (JButton colorMapValue : colorMap.values()) {
            colorMapValue.addActionListener(menuListener);
        }
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        for (Color color : Drawing.colors) {
            colorButton = colorMap.get(color);
            colorButton.addActionListener(menuListener);
            panel.add(colorButton);
            n++;
            if (n>=3) {
                n = 0;
                toolBox.add(panel);
                panel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
            }
        }
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        panel.add(colorLabel);
        toolBox.add(panel);


        panel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(toolBox);
//        toolPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(panel, BorderLayout.WEST);
        revalidate();

    }


    private void stopHighlight(){
        if (drawing!=null) {
            drawing.drawObj.shapeAndStyleHighlighted = null;
            drawing.repaint();
        }
    }





}
