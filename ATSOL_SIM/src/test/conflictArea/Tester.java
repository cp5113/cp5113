package test.conflictArea;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Tester extends Application {

    Shape inter;
    Point2D pc;
    double initX, initY, mx, my;
    
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        root.setStyle("-fx-background-color:cyan;");
//        final Polygon p2 = new Polygon();
        final Polygon p1 = new Polygon();
        p1.getPoints().addAll(new Double[]{
            50., 50.,
            50., 100.,
            60., 100.,
            60., 55.,
            80., 70.,
            80., 100.,
            100., 100.,
            100., 50.
        });
        p1.setFill(Color.GREEN);
//        p2.getPoints().addAll(new Double[]{
//            65., 100.,
//            65., 45.,
//            75., 80.,
//            100., 100.
//        });
        
        Arc a1 = new Arc(150, 550, 100, 100, 90, 90);
        a1.setType(ArcType.OPEN);
        a1.setStroke(Color.BLACK);
        a1.setFill(null);
        a1.setStrokeWidth(3);

        Arc a2 = new Arc(300, 550, 100, 100, 45, 90);
        a2.setType(ArcType.CHORD);
        a2.setStroke(Color.BLACK);
        a2.setFill(null);
        a2.setStrokeWidth(3);

        Arc a3 = new Arc(500, 550, 100, 100, 45, 90);
        a3.setType(ArcType.ROUND);
        a3.setStroke(Color.BLACK);
        a3.setFill(null);
        a3.setStrokeWidth(3);
        Rectangle r3 = new Rectangle(450,25,100,200);
        r3.setStroke(Color.BLACK);
        r3.setFill(null);
        r3.setStrokeWidth(3);
        r3.setArcWidth(30);
        r3.setArcHeight(300);
        r3.setRotate(0);
        
        final Circle p2 = new Circle(50, 50, 100);

        p1.setTranslateX(400);
        p1.setTranslateY(250);

        p2.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
                initX = p2.getTranslateX();
                initY = p2.getTranslateX();
                pc = new Point2D(me.getSceneX(), me.getSceneY());
                
            }
            
        });

        p2.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                double dragX = me.getSceneX() - pc.getX();
                double dragY = me.getSceneY() - pc.getY();
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                p2.setTranslateX(newXPosition);
                p2.setTranslateY(newYPosition);
                System.out.println("no intersection");
                if (Shape.intersect(p2, p1).getBoundsInLocal().isEmpty() == false) {
                    p1.setTranslateX(p2.getTranslateX() + mx);
                    p1.setTranslateY(p2.getTranslateY() + my);
                    System.out.println("colision");
                    
                } else {
                    mx = p1.getTranslateX() - p2.getTranslateX();
                    my = p1.getTranslateY() - p2.getTranslateY();
                }

            }
        });
        
        
        
        
        root.getChildren().addAll(p1, p2,a1,a2,a3,r3);

        final Scene scene = new Scene(root, 1200, 850);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}