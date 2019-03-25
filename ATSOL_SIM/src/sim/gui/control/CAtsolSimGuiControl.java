package sim.gui.control;
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
 * @date : Mar 21, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 21, 2019 : Coded by S. J. Yun.
 *
 *
 */

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import elements.util.geo.CCoordination;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.gui.CDrawingInform;
import sim.gui.IDrawingObject;
import sim.gui.view.CAtsolSimGuiView;

public class CAtsolSimGuiControl {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private static double TestingCount = 0;
	private CAtsolSimGuiView fGuiview;
	@FXML
	private MenuItem MenuRunProject;
	@FXML
	private MenuItem MenuLoadProject;
	@FXML
	private MenuItem MenuSaveProject;
	@FXML
	private MenuItem MenuClose;
	
	@FXML
	private Pane	SimCanvasPane;
	@FXML
	private Canvas   SimCanvas;
	@FXML
	private BorderPane RootBoderPane;
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public void MenuCloseAction() {
		System.exit(0);
	}
	
	public void MenuRunProjectAction() {		
		if(!CAtsolSimMain.getInstance().getSimClock().isRunning()) {
			CAtsolSimMain.getInstance().getSimClock().setStartTimeInMilliSeconds(0);
			CAtsolSimMain.getInstance().getSimClock().setEndTimeInMilliSeconds(3600000);			
			Thread myThread = new Thread(CAtsolSimMain.getInstance().getSimClock());
			myThread.start();
			
		}else {
			CAtsolSimMain.getInstance().getSimClock().stop();
		}
			
	}
	
	public void drawDrawingObjectList() {		
		// Cleaning Canvas
		GraphicsContext gc = SimCanvas.getGraphicsContext2D();
		gc.setFill(Color.LIGHTGRAY);
		gc.fillRect(0, 0, SimCanvas.getWidth(), SimCanvas.getHeight());
		
		
		Iterator<IDrawingObject> iter = CAtsolSimMain.getInstance().getiDrawingObjectList().iterator();
		while(iter.hasNext()) {
			CDrawingInform lDrawingInform = iter.next().getDrawingInform();
			if(!lDrawingInform.isVisible()) continue; 
			
			// Drawing			
			switch(lDrawingInform.getShape()) {
			case LINE:
				List<CCoordination> lCoordList = lDrawingInform.getCoordinationList();
				for(int loopList = 0; loopList < lCoordList.size()-1; loopList++) {
					double p1X = lCoordList.get(loopList).getXCoordination();
					double p1Y = lCoordList.get(loopList).getYCoordination();
					double p2X = lCoordList.get(loopList+1).getXCoordination();
					double p2Y = lCoordList.get(loopList+1).getYCoordination();
					
					CCoordination p1 = changeCoordinatesInCanvas(p1X, p1Y);
					CCoordination p2 = changeCoordinatesInCanvas(p2X, p2Y);
					
					gc.setStroke(lDrawingInform.getColor());
					gc.beginPath();
					gc.moveTo(p1.getXCoordination(), p1.getYCoordination());
					gc.lineTo(p2.getXCoordination(), p2.getYCoordination());
					gc.closePath();
					gc.stroke();					
				}
				break;
			case DOT:
				CCoordination lCoordination = lDrawingInform.getCoordinationList().get(0);
				double p1X = lCoordination.getXCoordination();
				double p1Y = lCoordination.getYCoordination();
				CCoordination p1 = changeCoordinatesInCanvas(p1X, p1Y);
				gc.setStroke(lDrawingInform.getColor());
				gc.fillRect(p1.getXCoordination(), p1.getYCoordination(), 50*CAtsolSimMain.getInstance().getiViewPointR(), 50*CAtsolSimMain.getInstance().getiViewPointR());
				
				break;
			} //switch(lDrawingInform.getShape()) {
			
		}//while(iter.hasNext()) {
		
//		System.out.println(CAtsolSimMain.getInstance().getiViewPoint());
//		System.out.println(CAtsolSimMain.getInstance().getiViewPointR());
		
	}
	
	private CCoordination changeCoordinatesInCanvas(double aXCoord, double aYCoord) {
		// Initialize
		CCoordination outputCoordination = new CCoordination(aXCoord, aYCoord);
		Double lCanvasheight = SimCanvas.getHeight();
		Double lCanvaswidth  = SimCanvas.getWidth();
		
		// get View Points
		double lCenterX = CAtsolSimMain.getInstance().getiViewPoint().getXCoordination();
		double lCenterY = CAtsolSimMain.getInstance().getiViewPoint().getYCoordination();
		double lRatio   = CAtsolSimMain.getInstance().getiViewPointR();
		
		
		// Normalize Coordination
		Double newXCoord = (aXCoord * lRatio - lCenterX * lRatio + lCanvaswidth/2);
		Double newYCoord = (lCenterY * lRatio - aYCoord * lRatio + lCanvasheight/2); 
		outputCoordination.setXCoordination(newXCoord);
		outputCoordination.setYCoordination(newYCoord);
		
		return outputCoordination;		
	}
	
	public void initialize() {
						
		SimCanvas.widthProperty().bind(SimCanvasPane.widthProperty());
		SimCanvas.heightProperty().bind(SimCanvasPane.heightProperty());
		SimCanvas.widthProperty().addListener(evt -> drawDrawingObjectList());
		SimCanvas.heightProperty().addListener(evt -> drawDrawingObjectList());
		SimCanvas.setCache(true);
		SimCanvas.setCacheHint(CacheHint.SPEED);
		CCanvasEventHandler lCanvasEventHandler = new CCanvasEventHandler();
		SimCanvas.setOnKeyPressed(lCanvasEventHandler);
		SimCanvas.setOnMouseMoved(lCanvasEventHandler);
		SimCanvas.setOnMouseClicked(lCanvasEventHandler);
		SimCanvas.setOnMousePressed(lCanvasEventHandler);
		SimCanvas.setOnMouseDragged(lCanvasEventHandler);
		SimCanvas.setOnMouseDragEntered(lCanvasEventHandler);
		SimCanvas.setOnScroll(lCanvasEventHandler);
		
		
	}
	
	
	
	private class CCanvasEventHandler implements EventHandler<Event>{
		double iMousePositionXPrev = 0;
		double iMousePositionYPrev = 0;
		double iMousePositionXCurrent= 0;
		double iMousePositionYCurrent = 0;
		
		double iScrollStep			   =0.1;
		int	   iScrollCount			   =0;
		@Override
		public void handle(Event aEvent) {
			// TODO Auto-generated method stub
//			System.out.println(aEvent.getEventType());
			
			switch(aEvent.getEventType().toString()) {
			case "MOUSE_DRAGGED":							
				// Wheel Dragging
				if(((MouseEvent) aEvent).getButton() == MouseButton.MIDDLE){
					iMousePositionXCurrent = ((MouseEvent) aEvent).getSceneX();
					iMousePositionYCurrent = ((MouseEvent) aEvent).getSceneY();
					double lMovementX = (iMousePositionXCurrent-iMousePositionXPrev);
					double lMovementY = (iMousePositionYCurrent-iMousePositionYPrev);
					CAtsolSimMain.getInstance().getiViewPoint().moveX(-lMovementX/CAtsolSimMain.getInstance().getiViewPointR());
					CAtsolSimMain.getInstance().getiViewPoint().moveY(lMovementY/CAtsolSimMain.getInstance().getiViewPointR());					
					drawDrawingObjectList();
					iMousePositionXPrev = iMousePositionXCurrent;
					iMousePositionYPrev = iMousePositionYCurrent;
				}
				break;
			case "MOUSE_MOVED":
				break;
			case "MOUSE_PRESSED":		
				// Initialize Mouse Position for Wheel Dragging				
				iMousePositionXPrev = ((MouseEvent) aEvent).getSceneX();
				iMousePositionYPrev = ((MouseEvent) aEvent).getSceneY();
				break;
			case "MOUSE_CLICKED":		
				// Initialize Mouse Position for Wheel Dragging
				iMousePositionXPrev = ((MouseEvent) aEvent).getSceneX();
				iMousePositionYPrev = ((MouseEvent) aEvent).getSceneY();
				break;
			case "SCROLL":		 // Zoon In/Out		
//				if(((ScrollEvent) aEvent).getDeltaY()<0 && CAtsolSimMain.getInstance().getiViewPointR() - iScrollStep<iScrollStep) {
//					iScrollStep/=10;
//					iScrollCount=0;
//				}else if(((ScrollEvent) aEvent).getDeltaY()>0 && CAtsolSimMain.getInstance().getiViewPointR() + iScrollStep>iScrollStep) {
//					iScrollCount++;
//					System.out.println(iScrollCount);
//					if(iScrollCount % 10 == 0) {
//						iScrollStep*=10;
//						iScrollCount=0;
//					}
//						
//				}				
				if(((ScrollEvent) aEvent).getDeltaY()<0) {
//					CAtsolSimMain.getInstance().setiViewPointR(CAtsolSimMain.getInstance().getiViewPointR() - iScrollStep);
					CAtsolSimMain.getInstance().setiViewPointR(CAtsolSimMain.getInstance().getiViewPointR()*0.7);
				}else {
//					CAtsolSimMain.getInstance().setiViewPointR(CAtsolSimMain.getInstance().getiViewPointR() +iScrollStep);
					CAtsolSimMain.getInstance().setiViewPointR(CAtsolSimMain.getInstance().getiViewPointR()/0.7);
				}
				
				drawDrawingObjectList();
				break;
			}
		}
		
	}
	
	
	
	public void addview(CAtsolSimGuiView aGuiview) {
		fGuiview = aGuiview;
	}
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






