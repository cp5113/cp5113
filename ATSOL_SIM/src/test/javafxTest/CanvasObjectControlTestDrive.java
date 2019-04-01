package test.javafxTest;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 
 * Dtails....
 * 
 * 
 * <p>
 * The naming convention
 *  <li>I...... : Interface class</li>
 *  <li>A...... : Abstract class</li>
 *  <li>C...... : Concrete class</li>
 *  
 *  <li>doSomething  : Do it method </li>
 *  <li>runSomething : Run it method </li>
 *  <li>getSomething : Getter </li>
 *  <li>setSomething : Set attribute</li>
 *
 *  <li>i...... : Instance variable </li>
 *  <li>l...... : Local variable </li>
 *  <li>s...... : Static variable </li>
 *  <li>a...... : Argument </li>
 *  <li>n...... : ENUM </li>
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : Apr 1, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 1, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CanvasObjectControlTestDrive {
	@FXML
	private Canvas   SimCanvas;
	
	@FXML
	private SubScene InnerScene;
	
	private CanvasObjectTestDrive CanvasObjectTestDrive;
	Circle circle;
	public void addview(CanvasObjectTestDrive aCanvasObjectTestDrive) {
		// TODO Auto-generated method stub
		CanvasObjectTestDrive = aCanvasObjectTestDrive;
	}

	public void initialize() {
		// TODO Auto-generated method stub
		System.out.println(2);
		circle = new Circle();
		circle.setCenterX(50);
		circle.setCenterY(50);
		circle.setRadius(25);
		circle.setFill(Color.RED);
		
		Group a = new Group(circle);
		InnerScene.setRoot(a);
		
		System.out.println(2);
		
//		GraphicsContext gc = SimCanvas.getGraphicsContext2D();
		InnerScene.setPickOnBounds(true);
		InnerScene.setOnMouseClicked(new Testclick());
		
	}
	
	public class Testclick implements EventHandler<Event> {

		@Override
		public void handle(Event aEvent) {
			// TODO Auto-generated method stub
			System.out.println("TTT");
			circle.setCenterX(circle.getCenterX()+1);
			circle.setCenterY(50);
		}
		
	}
	
	
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */

	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






