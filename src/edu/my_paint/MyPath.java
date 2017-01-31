package edu.my_paint;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.HashSet;

class MyPath extends Path2D.Float{
    private HashMap<Integer, HashSet<Integer>> points;

    private AffineTransform affineTransform = new AffineTransform();

    MyPath() {
        super();
        points = new HashMap<>();
    }
    MyPath(Path2D path){
    }

    void appendLine(int x1, int y1, int x2, int y2) {
        Line2D line = new Line2D.Float(x1,y1,x2,y2);
        append(line,false);
        HashSet<Integer> ySet = new HashSet<>();
        if (points.containsKey(x1))
            points.get(x1).add(y1);
        else {
            ySet.add(y1);
            points.put(x1, ySet);
        }

        if (points.containsKey(x2))
            points.get(x2).add(y2);
        else {
            ySet.add(y2);
            points.put(x2, ySet);
        }
    }

    boolean containsPoint(int x, int y){
        Point p0 = new Point(x,y);
        Point p1 = new Point();
        affineTransform.transform(p0,p1);
        x = (int)(p1.getX());
        y = (int)(p1.getY());
        int tolerance = 5;
        System.out.println("finding   " + x + " " + y);
        for (int i = x-tolerance; i <= x+tolerance ; i++) {
            if (points.containsKey(i))
                for (int j = y-tolerance; j <= y+tolerance; j++) {
                    if (points.get(i).contains(j)) {
                        return true;
                    }
                }

        }
        return false;
    }

    public void affineTrans(AffineTransform affineTransform){
        Shape shape = affineTransform.createTransformedShape(this);
        this.reset();
        this.append(shape,false);
        try {
            affineTransform.invert();
        } catch (NoninvertibleTransformException e) {}
        this.affineTransform.concatenate(affineTransform);
    }
}
