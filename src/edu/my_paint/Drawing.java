package edu.my_paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Dmitry on 01.12.2016.
 */

class Drawing extends JPanel {
    static final Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.ORANGE, Color.WHITE};
    static Color color = Color.BLACK;
    DrawObj drawObj;
    private MyPath path = new MyPath();
    //    private Graphics2D graphics; // this will be used to buffer into image(while plotting into graph).
    private static BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,2.0f);

//    boolean changing = false;

    enum DrawingTools {NONE, FILL_RECT, BRUSH, REFACTOR, MOVE, ROTATE, DELETE}
    private static DrawingTools drawingTool = DrawingTools.NONE;

    private int XCoord, YCoord;
    private int rotX, rotY;

    /// CONSTRUCTOR
    private Drawing() {System.out.println("don't use");}
    Drawing(int width, int height) {
        super();
        this.setLocation(0,0);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.WHITE);
        initialize(width,height);
        this.setVisible(true);
//        repaint();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                XCoord = e.getX();
                YCoord = e.getY();
                drawMousePressed(XCoord, YCoord);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                XCoord = e.getX();
                YCoord = e.getY();
                drawMouseReleased(XCoord, YCoord);
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                drawMouseDragged(XCoord,YCoord, e.getX(), e.getY());
                XCoord = e.getX();
                YCoord = e.getY();
                repaint();
            }
        });
    }

    void initialize(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        drawObj = new DrawObj(width, height);
        revalidate();
        repaint();
    }

    void openFile(String filePath) throws IOException {
        drawObj = new DrawObj(filePath);
        this.setPreferredSize(drawObj.getDimensions());
        revalidate();
        repaint();
    }

    void saveFile(String filePath) throws IOException {
        if (drawObj != null) {
            drawObj.save(filePath);
        }
    }

    void clear() {
        drawObj.clear();
        repaint();
    }

    void undo() {
        drawObj.undo();
        repaint();
    }

    void setColor(Color color) {
        Drawing.color = color;
    }

    static void setDrawMode(DrawingTools drawingTool) {
        Drawing.drawingTool = drawingTool;
    }

    private void drawMousePressed(int x, int y) {
        switch (drawingTool) {
            case FILL_RECT :
                drawObj.add(new Rectangle(x-10,y-10,20,20),stroke,color,true);
                break;
            case BRUSH :
                path.appendLine(x,y,x,y);
                break;
            case REFACTOR :
                System.out.println("refact");
                drawObj.highlight(x,y);
                break;
            case MOVE :
                System.out.println("move");
                drawObj.highlight(x,y);
                break;
            case ROTATE:
                System.out.println("rotate");
                drawObj.highlight(x,y);
                rotX = x;
                rotY = y;
                break;
            case DELETE:
                System.out.println("delete");
                drawObj.highlight(x,y);
                break;
        }
        repaint();
    }

    private void drawMouseDragged(int x1, int y1, int x2, int y2){
        switch (drawingTool) {
            case BRUSH:
                path.appendLine(x1,y1,x2,y2);
                repaint();
                break;
            case MOVE:
                if (drawObj.shapeAndStyleHighlighted != null) {
                    System.out.println("moving");
                    drawObj.shapeAndStyleHighlighted.move(x2-x1,y2-y1);
                    repaint();
                }
                break;
            case ROTATE:
                if (drawObj.shapeAndStyleHighlighted != null) {
                    System.out.println("rotating");
                    drawObj.shapeAndStyleHighlighted.rotate(y2-y1,rotX,rotY);
                    repaint();
                }
                break;

        }
    }

    private void drawMouseReleased(int x, int y) {
        switch (drawingTool) {
            case BRUSH:
                drawObj.add(path,stroke,color,false);
                path = new MyPath();
                repaint();
                break;
            case MOVE:
                acceptRefactor();
                repaint();
                break;
            case ROTATE:
                acceptRefactor();
                repaint();
                break;
            case DELETE:
                drawObj.delete();
//                acceptRefactor();
                repaint();
                break;
        }
    }

    void acceptRefactor() {
        drawObj.acceptChanges();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
//        try {
//            image = ImageIO.read(new File("e:\\1.jpg"));
//        } catch (IOException e) {
//        }
//        g.drawImage(image,0,0,null);
        drawObj.toPaintComponent((Graphics2D) g);
        g.setColor(color);
        ((Graphics2D) g).setStroke(stroke);
        ((Graphics2D) g).draw(path);
    }

}
