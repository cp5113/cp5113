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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.network.ANode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.clock.CDispatchAircraftThreadByTime;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingAreaObject;
import sim.gui.IDrawingObject;
import sim.gui.view.CAtsolSimGuiView;

public class CAtsolSimGuiControl {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	@SuppressWarnings("unused")
	private static CAtsolSimGuiView fGuiview;
	@FXML
	private MenuItem MenuRunProject;
	@FXML
	private MenuItem MenuLoadProject;
	@FXML
	private MenuItem MenuSaveProject;
	@FXML
	private MenuItem MenuClose;
	
	@FXML
	private Button	ButtonPause;
	@FXML
	private Button	ButtonStep;
	
	private double viewPointX=-99999999;
	private double viewPointY=-99999999;
	private double viewPointR = -9999999; 
	@FXML
	private Pane	SimCanvasPane;
	@FXML
	private Canvas   SimCanvas;
	@FXML
	private BorderPane RootBoderPane;
	public static CAtsolSimGuiControl iInstance;
	
	private static IDrawingObject iDistanceLine = new IDrawingObject() {
		private CCoordination start = new CCoordination(0, 0);
		private CCoordination end = new CCoordination(0, 0);
		private CDrawingInform   drawingInfo = new CDrawingInform(new ArrayList<CCoordination>() {{add(start);add(end);}}, new CAltitude(0, EGEOUnit.FEET), EShape.LINE, Color.RED, true, (double) 3);
		protected String distance = "";
		@Override
		public CDrawingInform getDrawingInform() {			
			return drawingInfo;
		}
		
		public String toString() {
			int di = ((int)(Math.sqrt((start.getXCoordination()-end.getXCoordination()) * (start.getXCoordination()-end.getXCoordination()) + 
					(start.getYCoordination()-end.getYCoordination()) * (start.getYCoordination()-end.getYCoordination())) * 10) );
			distance = String.valueOf(1.0*di/10);
			return distance + "m";
		}
	};
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public Canvas getSimCanvase() {
		return SimCanvas;
	}
	public void MenuCloseAction() {
		System.exit(0);
	}
	
	public void MenuRunProjectAction() {		
		
		
		if(!CAtsolSimMain.getInstance().getSimClock().isRunning()) {
			
			
			System.out.println("Reload Project Files");
			CAtsolSimMain.getInstance().connectTable();
			CAtsolSimMain.getInstance().loadEnvironment(new File(CAtsolSimMain.getInstance().getProjectFileName()));		
			CAtsolSimMain.getInstance().createDrawingObjectList();
			
			if(viewPointX!=-99999999) {
				CAtsolSimMain.getInstance().getViewPoint().setXYCoordination(viewPointX, viewPointY);
				CAtsolSimMain.getInstance().setViewPointR(viewPointR);
			}
			
			// Output Setup
			System.out.println("Create Output files");
			try {
				CAtsolSimMain.setOutputFileTrajectoryWriter	(new BufferedWriter(new FileWriter( new File(CAtsolSimMain.getOutputFileTrajectory()))));
				CAtsolSimMain.setOutputFileATCWriter		(new BufferedWriter(new FileWriter( new File(CAtsolSimMain.getOutputFileATC()))));
				
				// Header
				CAtsolSimMain.getOutputFileTrajectoryWriter().write	("Time,Callsign,Registration,Mode,MovementMode,MovementStatus,MoveState,Link,Node,X,Y,Z,V\n");
				CAtsolSimMain.getOutputFileATCWriter().write		("Time,Name,Aircraft,Instruction,InstructionTimeInSeconds\n");
				
			} catch (IOException e) {
				System.out.println("Cannot create Output file : " + CAtsolSimMain.getOutputFileTrajectory());
				System.out.println("Cannot create Output file : " + CAtsolSimMain.getOutputFileATC());
			} 
			
			
			System.out.println("Create link between Time and Elements(Controller only. The other element will be generated by Dispatch)");
			CAtsolSimMain.getInstance().getSimClock().createLinkBetweenTimeAndElements();
			
			System.out.println("Generate Thread Dispatch");
			CDispatchAircraftThreadByTime.getInstance().initializeDispatch();
			
			System.out.println("Clock Setting...");			
			CAtsolSimMain.getInstance().getSimClock().setStartTimeInMilliSeconds(CDispatchAircraftThreadByTime.getInstance().getSimStartTime()          );
			CAtsolSimMain.getInstance().getSimClock().setEndTimeInMilliSeconds(  CDispatchAircraftThreadByTime.getInstance().getSimStartTime() + 36000000);			
			Thread myThread = new Thread(CAtsolSimMain.getInstance().getSimClock(),"SimClockThread");
			
			System.out.println("Clock is Start");
			myThread.start();

			System.out.println("Create Thread and Start Thread (eg., Aircraft, Controller...)");
			CAtsolSimMain.getInstance().getSimClock().createThreadOfElements();
			
			
			
		}else {			
			viewPointX=CAtsolSimMain.getInstance().getViewPoint().getXCoordination();
			viewPointY=CAtsolSimMain.getInstance().getViewPoint().getYCoordination();
			viewPointR=CAtsolSimMain.getInstance().getViewPointR();
//			System.out.println(viewPointX + ", " + viewPointY);
			CAtsolSimMain.getInstance().getSimClock().stopClock();
			try {
				CAtsolSimMain.getOutputFileTrajectoryWriter().close();
				CAtsolSimMain.getOutputFileATCWriter().close();
			} catch (IOException e) {
				System.out.println("Output stream closed");
			}
		}
			
	}
	
	
	public void ButtonPauseAction() {
		CAtsolSimMain.getInstance().getSimClock().setPause(true);
	}
	public void ButtonStepAction() {
		CAtsolSimMain.getInstance().getSimClock().stepFoward();
	}
	
	public synchronized void drawDrawingObjectList()  {	
		Platform.runLater(()->{
			// Cleaning Canvas
			GraphicsContext gc = SimCanvas.getGraphicsContext2D();
			gc.setFill(Color.LIGHTGRAY);
			gc.fillRect(0, 0, SimCanvas.getWidth(), SimCanvas.getHeight());
			
			
			Iterator<IDrawingObject> iter = CAtsolSimMain.getInstance().getDrawingObjectList().iterator();
			while(iter.hasNext()) {
				IDrawingObject lAnObject = iter.next();
				CDrawingInform lDrawingInform = lAnObject.getDrawingInform();
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
					if(p1.getXCoordination()<0 || p1.getXCoordination()>SimCanvas.getWidth() || p1.getYCoordination()<0 || p1.getYCoordination()>SimCanvas.getHeight() ) {
						continue;
					}
					
					gc.setFill(lDrawingInform.getColor());			
					double lRadius =  50*CAtsolSimMain.getInstance().getViewPointR();
					gc.fillOval(p1.getXCoordination()-lRadius/2, p1.getYCoordination()-lRadius/2,lRadius, lRadius);
					
					break;
				case CIRCLE:
					CCoordination lCoordinationCircle = lDrawingInform.getCoordinationList().get(0);
					double p1XCircle = lCoordinationCircle.getXCoordination();
					double p1YCircle = lCoordinationCircle.getYCoordination();
					CCoordination p1Circle = changeCoordinatesInCanvas(p1XCircle, p1YCircle);
					if(p1Circle.getXCoordination()<0 || p1Circle.getXCoordination()>SimCanvas.getWidth() || p1Circle.getYCoordination()<0 || p1Circle.getYCoordination()>SimCanvas.getHeight() ) {
						continue;
					}					
					double lRadiusCircle =  10*CAtsolSimMain.getInstance().getViewPointR();
					if(lAnObject instanceof CWaypoint) {
						lRadiusCircle = 5;
					}
					gc.setStroke(lDrawingInform.getColor());
					gc.strokeOval(p1Circle.getXCoordination()-lRadiusCircle/2, p1Circle.getYCoordination()-lRadiusCircle/2,lRadiusCircle, lRadiusCircle);
					
					break;
				case RECTANGEL:
					break;
				case SQUARE:
					break;
				default:
					break;
				} //switch(lDrawingInform.getShape()) {
				
				
				
				// Drawing Area Object
				if(lAnObject instanceof IDrawingAreaObject) {
					
					// Extract Polygon information
					ObservableList<Double> lPolygonPointList = ((IDrawingAreaObject) lAnObject).getSafetyPolygonInform().getPoints();
					
					if(lPolygonPointList.size()<=4) continue;
					
					try {
					// Create Polygon Array
					double[] lpolygonX = new double[9];
					double[] lpolygonY = new double[9];
					int countPoly = 0;
					for(int i = 0; i < lPolygonPointList.size(); i+=2) {
						double pX = lPolygonPointList.get(i);						
						double pY = lPolygonPointList.get(i+1);
						CCoordination p = changeCoordinatesInCanvas(pX, pY);
					
						lpolygonX[countPoly] = p.getXCoordination();
						lpolygonY[countPoly] = p.getYCoordination();
						countPoly++;
					}					
					
					gc.strokePolygon(lpolygonX,lpolygonY, 6);
					
					}catch(Exception e) {
						
					}
					
					// Extract Polygon information
					try {
					ObservableList<Double> lShapePolygonPointList = ((IDrawingAreaObject) lAnObject).getShapePolygonInform().getPoints();
					if(lShapePolygonPointList.size()<=1) continue;
					// Create Polygon Array
					double[] lShapepolygonX = new double[9];
					double[] lShapepolygonY = new double[9];
					int countShapePoly = 0;
					for(int i = 0; i < lShapePolygonPointList.size(); i+=2) {
						double pX = lShapePolygonPointList.get(i);
						double pY = lShapePolygonPointList.get(i+1);
						CCoordination p = changeCoordinatesInCanvas(pX, pY);
						lShapepolygonX[countShapePoly] = p.getXCoordination();						
						lShapepolygonY[countShapePoly] = p.getYCoordination();
					
						countShapePoly++;
					}					
					
					gc.strokePolygon(lShapepolygonX,lShapepolygonY, 9);
					}catch(Exception e) {
						
					}
				}
				
			}//while(iter.hasNext()) {
			
			
		
		});
		
		
		
//		System.out.println(CAtsolSimMain.getInstance().getiViewPoint());
//		System.out.println(CAtsolSimMain.getInstance().getiViewPointR());
		
	}
	
	public CCoordination changeCoordinatesInCanvas(double aXCoord, double aYCoord) {
		// Initialize
		CCoordination outputCoordination = new CCoordination(aXCoord, aYCoord);
		Double lCanvasheight = SimCanvas.getHeight();
		Double lCanvaswidth  = SimCanvas.getWidth();
		
		// get View Points
		double lCenterX = CAtsolSimMain.getInstance().getViewPoint().getXCoordination();
		double lCenterY = CAtsolSimMain.getInstance().getViewPoint().getYCoordination();
		double lRatio   = CAtsolSimMain.getInstance().getViewPointR();
		
		
		// Normalize Coordination
		Double newXCoord = (aXCoord * lRatio - lCenterX * lRatio + lCanvaswidth/2);
		Double newYCoord = (lCenterY * lRatio - aYCoord * lRatio + lCanvasheight/2); 
		outputCoordination.setXCoordination(newXCoord);
		outputCoordination.setYCoordination(newYCoord);
		
		return outputCoordination;		
	}
	private CCoordination changeCoordinatesFromCanvas(double aXCoord, double aYCoord) {
		CCoordination outputCoordination = new CCoordination(aXCoord, aYCoord);
		Double lCanvasheight = SimCanvas.getHeight();
		Double lCanvaswidth  = SimCanvas.getWidth();
		// get View Points
		double lCenterX = CAtsolSimMain.getInstance().getViewPoint().getXCoordination();
		double lCenterY = CAtsolSimMain.getInstance().getViewPoint().getYCoordination();
		double lRatio   = CAtsolSimMain.getInstance().getViewPointR();
		
		// Normalize Coordination
		Double newXCoord = (aXCoord + lCenterX * lRatio - lCanvaswidth/2)/lRatio;
		Double newYCoord = (lCenterY * lRatio  + lCanvasheight/2 - aYCoord)/lRatio; 
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
		CAtsolSimMain.getInstance().getDrawingObjectList().add(iDistanceLine);
		
	}
	
	
	
	private class CCanvasEventHandler implements EventHandler<Event>{
		double iMousePositionXPrev = 0;
		double iMousePositionYPrev = 0;
		double iMousePositionXCurrent= 0;
		double iMousePositionYCurrent = 0;
		int		iSecondaryMouseClicked = 0;
		double iScrollStep			   =0.1;
		int	   iScrollCount			   =0;
				
		@Override
		public void handle(Event aEvent) {
			// TODO Auto-generated method stub
//			System.out.println(aEvent.getEventType());
//			System.out.println("Input!!");
			switch(aEvent.getEventType().toString()) {
			case "MOUSE_DRAGGED":							
				// Wheel Dragging
				if(((MouseEvent) aEvent).getButton() == MouseButton.MIDDLE){
					iMousePositionXCurrent = ((MouseEvent) aEvent).getX();
					iMousePositionYCurrent = ((MouseEvent) aEvent).getY();
					double lMovementX = (iMousePositionXCurrent-iMousePositionXPrev);
					double lMovementY = (iMousePositionYCurrent-iMousePositionYPrev);
					CAtsolSimMain.getInstance().getViewPoint().moveX(-lMovementX/CAtsolSimMain.getInstance().getViewPointR());
					CAtsolSimMain.getInstance().getViewPoint().moveY(lMovementY/CAtsolSimMain.getInstance().getViewPointR());
//					System.out.println(CAtsolSimMain.getInstance().getViewPoint());
					drawDrawingObjectList();
					iMousePositionXPrev = iMousePositionXCurrent;
					iMousePositionYPrev = iMousePositionYCurrent;
				}
				break;
			case "MOUSE_MOVED":
				drawDrawingObjectList();
				Platform.runLater(()->{
					
					iMousePositionXCurrent = ((MouseEvent) aEvent).getX();
					iMousePositionYCurrent = ((MouseEvent) aEvent).getY();
					//				System.out.println(iMousePositionXCurrent);
					CCoordination lOriginalCoordination =  changeCoordinatesFromCanvas(iMousePositionXCurrent, iMousePositionYCurrent);
					GraphicsContext gc = SimCanvas.getGraphicsContext2D();
					gc.setStroke(Color.BLACK);
					gc.strokeText("(" + (int) lOriginalCoordination.getXCoordination() + ", " + (int) lOriginalCoordination.getYCoordination() + ")", iMousePositionXCurrent+50, iMousePositionYCurrent+3);
					
					
					double lDistance = Double.MAX_VALUE;
					IDrawingObject lTheObject = null;
					Iterator<IDrawingObject> iter = CAtsolSimMain.getInstance().getDrawingObjectList().iterator();
					while(iter.hasNext()) {
						IDrawingObject lAnObject = iter.next();
						CDrawingInform lDrawingInform = lAnObject.getDrawingInform();
						
						int index = lDrawingInform.getCoordinationList().size();
						if(!lDrawingInform.isVisible()) continue;
						
						double ltempDistance;
						if(lAnObject instanceof CTaxiwayLink) {
							ltempDistance = ((lDrawingInform.getCoordinationList().get(index-2).getXCoordination() - lOriginalCoordination.getXCoordination())*(lDrawingInform.getCoordinationList().get(index-2).getXCoordination() - lOriginalCoordination.getXCoordination()) +
									(lDrawingInform.getCoordinationList().get(index-2).getYCoordination() - lOriginalCoordination.getYCoordination())*(lDrawingInform.getCoordinationList().get(index-2).getYCoordination() - lOriginalCoordination.getYCoordination()));
						}else {
							ltempDistance = ((lDrawingInform.getCoordinationList().get(index-1).getXCoordination() - lOriginalCoordination.getXCoordination())*(lDrawingInform.getCoordinationList().get(index-1).getXCoordination() - lOriginalCoordination.getXCoordination()) +
									(lDrawingInform.getCoordinationList().get(index-1).getYCoordination() - lOriginalCoordination.getYCoordination())*(lDrawingInform.getCoordinationList().get(index-1).getYCoordination() - lOriginalCoordination.getYCoordination()));
						}
						

						if(ltempDistance<lDistance) {
							lTheObject = lAnObject;
							lDistance=ltempDistance;
							
						}					
					}

					int index = lTheObject.getDrawingInform().getCoordinationList().size();
					
					double p1X;
					double p1Y;
					if(lTheObject instanceof CTaxiwayLink) {
						p1X = lTheObject.getDrawingInform().getCoordinationList().get(index-2).getXCoordination();
						p1Y = lTheObject.getDrawingInform().getCoordinationList().get(index-2).getYCoordination();
					}else {
						p1X = lTheObject.getDrawingInform().getCoordinationList().get(index-1).getXCoordination();
						p1Y = lTheObject.getDrawingInform().getCoordinationList().get(index-1).getYCoordination();
					}
					CCoordination p1 = changeCoordinatesInCanvas(p1X, p1Y);
//					System.out.println(lTheObject);
					gc = SimCanvas.getGraphicsContext2D();
					gc.setStroke(Color.BLACK);
					double lFactor = -SimCanvas.getHeight()*0.04;
					if(lTheObject instanceof ANode) {
						if(lTheObject instanceof CTaxiwayNode) {
							if( ((CTaxiwayNode)lTheObject).getSpot() != null) {
								gc.strokeText(lTheObject.toString() + "\n Node : "+((ANode)lTheObject).getVehicleWillUseList() + "\n Spot : " + ((CTaxiwayNode)lTheObject).getSpot().getVehicleWillUseList(), p1.getXCoordination(), p1.getYCoordination()+lFactor);
							}						
								
							gc.strokeText(lTheObject.toString() + "\n"+((ANode)lTheObject).getVehicleWillUseList(), p1.getXCoordination(), p1.getYCoordination()+lFactor);
						}
					}else if(lTheObject instanceof CAircraft){
						CAircraft lAircraft = (CAircraft) lTheObject;
						if(lAircraft.getLeadingVehicle() != null) {
							gc.strokeText(lTheObject.toString() + "(" +lAircraft.getATCController() + ") : " + lAircraft.getMoveState().getClass().getSimpleName() + " ->"+ lAircraft.getLeadingVehicle(), p1.getXCoordination(), p1.getYCoordination()+lFactor);
						}else {
							gc.strokeText(lTheObject.toString() + "(" +lAircraft.getATCController() + ") : " + lAircraft.getMoveState().getClass().getSimpleName(), p1.getXCoordination(), p1.getYCoordination()+lFactor);
						}
						
					}else if(lTheObject instanceof CTaxiwayLink){
						CTaxiwayLink lLink = (CTaxiwayLink) lTheObject;
						gc.strokeText(lTheObject.toString() +"(" +lLink.getATCController() + ") : " + lLink.getOccupyingSchedule(), p1.getXCoordination(), p1.getYCoordination()+lFactor);						
					}else {
						gc.strokeText(lTheObject.toString(), p1.getXCoordination(), p1.getYCoordination()+lFactor);
					}
										
//					gc.setFill(Color.THISTLE);			
//					double lRadius =  10*CAtsolSimMain.getInstance().getViewPointR();
//					gc.fillOval(p1.getXCoordination()-lRadius/2, p1.getYCoordination()-lRadius/2,lRadius, lRadius);
					
					
					
					
					if(iSecondaryMouseClicked % 2 != 0) {
						iDistanceLine.getDrawingInform().getCoordinationList().get(1).setXYCoordination(lOriginalCoordination.getXCoordination(), lOriginalCoordination.getYCoordination());;
					}
					
					
				});
				break;
			case "MOUSE_PRESSED":		
				// Initialize Mouse Position for Wheel Dragging	
				if(((MouseEvent) aEvent).getButton() == MouseButton.MIDDLE){
					iMousePositionXPrev = ((MouseEvent) aEvent).getX();
					iMousePositionYPrev = ((MouseEvent) aEvent).getY();
				}
				break;
			case "MOUSE_CLICKED":
				
				if (((MouseEvent) aEvent).getButton() == MouseButton.SECONDARY) {
					iSecondaryMouseClicked++;
					if(iSecondaryMouseClicked % 2 == 0) {
						iMousePositionXCurrent = ((MouseEvent) aEvent).getX();
						iMousePositionYCurrent = ((MouseEvent) aEvent).getY();
						
//							drawDrawingObjectList();
						
					}else {
						// Initialize Mouse Position for Wheel Dragging
						iMousePositionXPrev = ((MouseEvent) aEvent).getX();
						iMousePositionYPrev = ((MouseEvent) aEvent).getY();
						CCoordination lCoordinationPrev=  changeCoordinatesFromCanvas(iMousePositionXPrev, iMousePositionYPrev);
						iDistanceLine.getDrawingInform().getCoordinationList().get(0).setXYCoordination(lCoordinationPrev.getXCoordination(), lCoordinationPrev.getYCoordination());;
					}
				}
				
				
				
//				System.out.println(iMousePositionXPrev + ", " + iMousePositionYPrev);
				
				break;
			case "SCROLL":		 // Zoon In/Out		
	
				if(((ScrollEvent) aEvent).getDeltaY()<0) {
					CAtsolSimMain.getInstance().setViewPointR(CAtsolSimMain.getInstance().getViewPointR()*0.7);
				}else {
					CAtsolSimMain.getInstance().setViewPointR(CAtsolSimMain.getInstance().getViewPointR()/0.7);
				}
				
				drawDrawingObjectList();
				break;
			}
		}
		
	}
	
	@SuppressWarnings("static-access")
	public CAtsolSimGuiControl() {
		this.iInstance = this;
	}
	
	public static CAtsolSimGuiControl getInstance() {
		return iInstance;
	}
	public void addview(CAtsolSimGuiView aGuiview) {
		fGuiview = aGuiview;
	}
	
	public Canvas getSimCanvas() {
		return SimCanvas;
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






