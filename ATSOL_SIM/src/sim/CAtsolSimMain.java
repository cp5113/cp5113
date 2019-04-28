package sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import elements.IElementObservableClock;
import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.table.ATable;
import elements.table.CAirRouteTable;
import elements.table.CAircraftTable;
import elements.table.CAircraftTypeTable;
import elements.table.CAirportTable;
import elements.table.CControllerTable;
import elements.table.CWaypointTable;
import elements.table.ITableAble;
import elements.util.geo.CCoordination;
import javafx.application.Application;
import sim.clock.CSimClockOberserver;
import sim.gui.IDrawingObject;
import sim.gui.view.CAtsolSimGuiView;

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
 * @date : Mar 23, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 23, 2019 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
/**
 * @author S. J. Yun
 *
 */
public class CAtsolSimMain {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	private static CAtsolSimMain    iMain = new CAtsolSimMain();
	private static CAtsolSimGuiView iGUI = new CAtsolSimGuiView();
	private static CSimClockOberserver		iSimClock = CSimClockOberserver.getInstance();

	private static List<ATable>				iAllTableList		= Collections.synchronizedList(new ArrayList<ATable>());
	
	private static CAirportTable 			iAirportTable 		= new CAirportTable();
	private static CAircraftTypeTable		iAircraftTypeTable 	= new CAircraftTypeTable();
	private static CWaypointTable			iWaypointTable      = new CWaypointTable();
	private static CAirRouteTable			iAirRouteTable      = new CAirRouteTable();
	private static CAircraftTable			iAircraftTable      = new CAircraftTable();
	private static CControllerTable			iControllerTable	= new CControllerTable();
	private static List<IDrawingObject> iDrawingObjectList = Collections.synchronizedList(new ArrayList<IDrawingObject>());
	
	private static String					iProjectFileName   = null;
	
	// Graphic Control
	private static CCoordination iViewPoint;	
	private static Double		 iViewPointR;
	
	
	
	private CAtsolSimMain() {
		
	}
	public static CAtsolSimMain getInstance() {
		if(iMain==null) {
			iMain = new CAtsolSimMain();
		}
		return iMain;
	}
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		iProjectFileName = "C:\\\\Users\\\\cp511\\\\git\\\\cp5113\\\\ATSOL_SIM\\\\SimStudy\\\\Test001_Project.txt";
		System.out.println("Loading Project : " + iProjectFileName);
		
		CAtsolSimMain.getInstance().connectTable();
		CAtsolSimMain.getInstance().loadEnvironment(new File(iProjectFileName));		
		CAtsolSimMain.getInstance().createDrawingObjectList();
		CAtsolSimMain.getInstance().openGui();		
	}
	
	public void connectTable() {
//		Field[] lFieldList = CAtsolSimMain.class.getDeclaredFields();
//		
//		for(int loopField = 0; loopField<lFieldList.length;loopField++) {
//			System.out.println(lFieldList[loopField].getType().toString());
//			if(lFieldList[loopField].getType().toString().contains("elements.table.")) {
//				
//				System.out.println(lFieldList[loopField].get.getName());
//				System.out.println();
//			}
//		}
		
		iAllTableList.add(iAirRouteTable);
		iAllTableList.add(iAircraftTypeTable);
		iAllTableList.add(iAirportTable);
		iAllTableList.add(iWaypointTable);
		iAllTableList.add(iAircraftTable);
		iAllTableList.add(iControllerTable);	
		iAirRouteTable.clearTalbe();
		iAircraftTypeTable.clearTalbe();
		iAirportTable.clearTalbe();
		iAircraftTable.clearTalbe();
		iControllerTable.clearTalbe();
		
					 
		
	}

	private void openGui() {
		
		new Thread(()->CAtsolSimGuiView.main(new String[0])).start();
//		new Thread(() -> Application.launch(iGUI.getClass()),"GUI Thread");
		System.out.println("Openning GUI is done!");
	}

	
	public void createDrawingObjectList() {
		iDrawingObjectList.clear();
		
		for(int loopList = 0; loopList < iAllTableList.size(); loopList++) {
			List<ITableAble> lElementList = iAllTableList.get(loopList).getElementList();
			for(int loopElement = 0; loopElement < lElementList.size(); loopElement++) {
				try {	
					iDrawingObjectList.add((IDrawingObject) lElementList.get(loopElement));				
				}catch(Exception e) {
					
				}
			}
		}
		
		CAirport lAirport = (CAirport)(iAirportTable.getElementTable().get("RKSI"));
		Iterator<CTaxiwayLink> iter = lAirport.getTaxiwayLinkList().iterator();		
		while(iter.hasNext()) {
			CTaxiwayLink lTaxiwayLink = iter.next();			
			iDrawingObjectList.add(lTaxiwayLink);
		}
		
		Iterator<CTaxiwayNode> iter2 = lAirport.getTaxiwayNodeList().iterator();		
		while(iter2.hasNext()) {
			CTaxiwayNode lTaxiwaynode= iter2.next();			
			iDrawingObjectList.add(lTaxiwaynode);
		}
		
		
		for(ITableAble loopwpt : iWaypointTable.getElementList()) {
			iDrawingObjectList.add((IDrawingObject) loopwpt);
		}
//		while(iter2.hasNext()) {
//			CTaxiwayNode lTaxiwaynode= iter2.next();			
//			iDrawingObjectList.add(lTaxiwaynode);
//		}
		
		
//		CAirport lWaypoint = (CWaypoint)(iWaypointTable.getElementTable().get("RKSI"));
//		Iterator<CTaxiwayLink> iter = lAirport.getTaxiwayLinkList().iterator();
		
	}

	public void loadEnvironment(File aProjectFile) {
		ArrayList<File> lAirportFileList = new ArrayList<File>();
		ArrayList<File> lAircraftFileTypeList = new ArrayList<File>();
		ArrayList<File> lWaypointFileList = new ArrayList<File>();
		ArrayList<File> lAirRouteFileList = new ArrayList<File>();
		ArrayList<File> lAircraftFilelist = new ArrayList<File>();
		ArrayList<File> lControllerFileList = new ArrayList<File>();
		
		// Read Project File				
		try {			
			BufferedReader lBR = new BufferedReader(new FileReader(aProjectFile));
			String lLine = "";
			String lLinePrev = "";
			while(true ) {
				
				switch(lLinePrev) {
				case "@Airport":
					while(true) {
						lAirportFileList.add(new File(aProjectFile.getParent().toString()+lLine));
						lLine = lBR.readLine();
						if(lLine.contains("@")|| lLine==null) {lLinePrev = lLine; break;}						
					}
					iAirportTable.createTable(lAirportFileList);
					break;
				case "@ViewPoint":					
					String[] lviewPoint= lLine.split(",");
					iViewPoint = new CCoordination(Double.parseDouble(lviewPoint[0]),Double.parseDouble(lviewPoint[1]));
					iViewPointR= Double.parseDouble(lviewPoint[2]);
					break;
				case "@Aircraft":
					while(true) {
						if(lLine.contains("AircraftType.csv")) {
							lAircraftFileTypeList.add(new File(aProjectFile.getParent().toString()+lLine));							
						}else if(lLine.contains("Aircraft.csv")) {
							
						}
						lLine = lBR.readLine();
						if(lLine==null || lLine.contains("@") ) {lLinePrev = lLine; break;}
					}
					iAircraftTypeTable.createTable(lAircraftFileTypeList);
					break;
				case "@Waypoint":
					while(true) {
						if(lLine.contains("Waypoint.csv")) {
							lWaypointFileList.add(new File(aProjectFile.getParent().toString()+lLine));
						}
						lLine = lBR.readLine();
						if(lLine==null || lLine.contains("@") ) {lLinePrev = lLine; break;}
					}
					iWaypointTable.createTable(lWaypointFileList);
					break;
				case "@Route":
					while(true) {
						if(lLine.contains("Route.csv")) {
							lAirRouteFileList.add(new File(aProjectFile.getParent().toString()+lLine));
						}
						lLine = lBR.readLine();
						if(lLine==null || lLine.contains("@") ) {lLinePrev = lLine; break;}
					}
					iAirRouteTable.createTable(lAirRouteFileList);
					break;
				case "@FlightSchedule":
					while(true) {
						if(lLine.contains("FlightSchedule.csv")) {
							lAircraftFilelist.add(new File(aProjectFile.getParent().toString()+lLine));
						}
						lLine = lBR.readLine();
						if(lLine==null || lLine.contains("@") ) {lLinePrev = lLine; break;}
					}
					iAircraftTable.createTable(lAircraftFilelist);
					break;
				case "@Controller":
					while(true) {
						if(lLine.contains("Controller.csv")) {
							lControllerFileList.add(new File(aProjectFile.getParent().toString()+lLine));
						}
						lLine = lBR.readLine();
						if(lLine==null || lLine.contains("@") ) {lLinePrev = lLine; break;}
					}
					iControllerTable.createTable(lControllerFileList);
					break;
				default:
				}
				lLinePrev = lLine;
				lLine = lBR.readLine();
				if(lLine==null) break;
			}
			
			lBR.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
		
		
		
		
	}
	public synchronized CAtsolSimGuiView getiGUI() {
		return iGUI;
	}
	public synchronized void setiGUI(CAtsolSimGuiView aIGUI) {
		iGUI = aIGUI;
	}
	public synchronized CAirportTable getiAirportTable() {
		return iAirportTable;
	}
	public synchronized void setAirportTable(CAirportTable aIAirportTable) {
		iAirportTable = aIAirportTable;
	}
	public synchronized List<IDrawingObject> getDrawingObjectList() {
		return iDrawingObjectList;
	}
	public synchronized void setDrawingObjectList(List<IDrawingObject> aIDrawingObjectList) {
		iDrawingObjectList = aIDrawingObjectList;
	}
	public synchronized CCoordination getViewPoint() {
		return iViewPoint;
	}
	public synchronized void setViewPoint(CCoordination aIViewPoint) {
		iViewPoint = aIViewPoint;
	}
	public synchronized Double getViewPointR() {
		return iViewPointR;
	}
	public synchronized void setViewPointR(Double aIViewPointR) {
		iViewPointR = aIViewPointR;
	}
	public CSimClockOberserver getSimClock() {
		return iSimClock;
	}
	public CWaypointTable getWaypointTable() {
		return iWaypointTable;
	}
	public CAirRouteTable getAirRouteTable() {
		return iAirRouteTable;
	}
	
	public ATable getAircraftTypeTable() {
		// TODO Auto-generated method stub
		return iAircraftTypeTable;
	}
	
	public List<ATable> getAllTableList(){
		return iAllTableList;
	}
	public CAircraftTable getAircraftTable() {
		return iAircraftTable;
	}
	public static CControllerTable getControllerTable() {
		return iControllerTable;
	}
	
	
	public String getProjectFileName() {
		return iProjectFileName;
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






