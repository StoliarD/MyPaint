package edu.my_paint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Dmitry on 03.12.2016.
 */
class DrawObj {
    private int width;
    private int height;
    private BufferedImage image;
    private Graphics2D graphics;
    private LinkedList<ShapeAndStyle> shapesList;
    ShapeAndStyleHighlighted shapeAndStyleHighlighted = null;

    DrawObj(int width, int height) {
        this.width = width;
        this.height = height;
        shapesList = new LinkedList<>();
        this.image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) image.getGraphics();
        shapesList.clear();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,width,height);
    }

    DrawObj(String filePath) throws IOException {
        this.image = ImageIO.read(new File(filePath));
        graphics = (Graphics2D) image.getGraphics();
        graphics = (Graphics2D) image.getGraphics();
        width = image.getWidth();
        height = image.getHeight();
        shapesList = new LinkedList<>();
    }

    void loadFromXML() {

    }

    void save(String fileName) throws IOException{
        BufferedImage image = this.image;
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        toGraphics(graphics2D);
        ImageIO.write(image, "jpg", new File(fileName));
    }

    void undo() {
        if (shapesList.size()>0)
            shapesList.remove(shapesList.size()-1);
    }

    private void toGraphics(Graphics2D g) {
        System.out.println(shapesList.size());
        if (shapesList != null) {
            for (ShapeAndStyle shapeAndStyle : shapesList) {
                shapeAndStyle.print(g);
            }
        }
    }


    void toPaintComponent(Graphics2D g) {
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
        toGraphics(g);
        if (shapeAndStyleHighlighted != null) {
            shapeAndStyleHighlighted.printHighlighted(g);
//            shapesList.get(shapesList.size()-1).printHighlighted(g);
        }
    }

    void add(Shape shape, Stroke stroke, Color color, boolean fill) {
        ShapeAndStyle shapeAndStyle = new ShapeAndStyle(shape, color, stroke, fill);
        if (true){//(shapesList.size() < 10) {
            shapesList.add(shapeAndStyle);
        } else {
//            toImage = shapesList.pop();
//            shapesList.add(shapeAndStyle);
        }
        System.out.println(shapesList.size());
    }

    void clear() {
////        shapesList.clear();
////        graphics.setColor(Color.WHITE);
////        graphics.fillRect(0,0,width,height);
//        shapesList.add(new ShapeAndStyle(new Rectangle(0,0,width,height), Color.WHITE, new BasicStroke(1.0f), true));
        shapesList.add((ClearShape.getSingleton(this)));
    }

    Dimension getDimensions() {
        return new Dimension(width, height);
    }

    static class ShapeAndStyle {
        Shape shape;
        Color color;
        Stroke stroke;
        boolean fill;

        ShapeAndStyle(Shape shape, Color color, Stroke stroke, boolean fill) {
            this.shape = shape;
            this.color = color;
            this.stroke = stroke;
            this.fill = fill;
        }

        void print(Graphics2D g) {
            g.setColor(color);
            g.setStroke(stroke);
            if (fill) {
                g.fill(shape);
            } else {
                g.draw(shape);
            }
        }
    }

    private static class ClearShape extends ShapeAndStyle{
        private static ClearShape singleton;

        private ClearShape(Shape shape, Color color, Stroke stroke, boolean fill) {
            super(shape, color, stroke, fill);
        }

        static ClearShape getSingleton(DrawObj drawObj){
            if (singleton == null) {
                singleton = new ClearShape(new Rectangle(0,0,drawObj.width,drawObj.height),
                        Color.WHITE, new BasicStroke(1.0f), true);
            }
            return singleton;
        }
    }

    static class ShapeAndStyleHighlighted extends ShapeAndStyle {
        static Color highLightColor = Color.CYAN;
        int i;

        private ShapeAndStyleHighlighted(Shape shape, Color color, Stroke stroke, boolean fill) {
            super(shape, color, stroke, fill);
        }

        static ShapeAndStyleHighlighted chooseShapeAndStyle(LinkedList<ShapeAndStyle> shapesList, int x, int y) {
            ListIterator iterator = shapesList.listIterator(shapesList.size()); //use this coz LinkedList.get is slower
            int i = shapesList.size();
            while (iterator.hasPrevious()) {
                ShapeAndStyle shapeAndStyle = (ShapeAndStyle) (iterator.previous());
                i--;
//                if (shapeAndStyle.shape.contains(x, y) ||
//                        (shapeAndStyle.shape instanceof MyPath) && shapeAndStyle.shape.intersects(x-0.1f, y-0.f,x+0.1f,y+0.1f)) {
                if (shapeAndStyle.shape.contains(x, y) ||
                        ((shapeAndStyle.shape instanceof MyPath) && ((MyPath)(shapeAndStyle.shape)).containsPoint(x,y))) {
                    System.out.println("choose shape");
                    if (shapeAndStyle instanceof ClearShape) {
                        return null;
                    } else {
                        ShapeAndStyleHighlighted res = new ShapeAndStyleHighlighted(shapeAndStyle.shape,
                                shapeAndStyle.color, shapeAndStyle.stroke, shapeAndStyle.fill);
                        res.i = i;
                        return res;
                    }
                }
            }
            return null;
        }

        void printHighlighted(Graphics2D g) {
            g.setColor(highLightColor);
            g.setStroke(stroke);
            if (fill) {
                g.fill(shape);
            } else {
                g.draw(shape);
            }
        }

        void setColor(Color color) {
            this.color = color;
        }

        void move(int dx, int dy){
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(dx*1.0d,dy*1.0d);
            if (this.shape instanceof MyPath){
                ((MyPath) this.shape).affineTrans(affineTransform);
                System.out.println("add transform");
                System.out.println(this.shape instanceof MyPath);
            } else {
                this.shape = new Path2D.Float(this.shape, affineTransform);
            }
        }

        void rotate(int dy, int x,int y) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(dy*0.05d,x,y);
            if (this.shape instanceof MyPath){
                ((MyPath) this.shape).affineTrans(affineTransform);
                System.out.println("add transform");
                System.out.println(this.shape instanceof MyPath);
            } else {
                this.shape = new Path2D.Float(this.shape, affineTransform);
            }
        }
    }

    void highlight(int x, int y) {
        if (shapeAndStyleHighlighted == null) {
            System.out.println("highlight");
            this.shapeAndStyleHighlighted = ShapeAndStyleHighlighted.chooseShapeAndStyle(shapesList,x,y);
        }
//        else {
//            System.out.println("!!!!!!!!!already highlighted");
//            shapeAndStyleHighlighted = null;
//        }
    }

    void delete(){
        if (shapeAndStyleHighlighted != null) {
            shapesList.remove(shapeAndStyleHighlighted.i);
            shapeAndStyleHighlighted = null;
        }
    }

    void acceptChanges() {
        if (shapeAndStyleHighlighted != null) {
            shapesList.remove(shapeAndStyleHighlighted.i);
            add(shapeAndStyleHighlighted.shape, shapeAndStyleHighlighted.stroke,
                    shapeAndStyleHighlighted.color, shapeAndStyleHighlighted.fill);
            shapeAndStyleHighlighted = null;
        }
    }
}
