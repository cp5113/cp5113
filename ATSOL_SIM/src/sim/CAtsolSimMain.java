package sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import elements.facility.CAirport;
import elements.facility.CTaxiwayLink;
import elements.table.CAirportTable;
import elements.util.geo.CCoordination;
import javafx.application.Application;
import sim.clock.CSimClock;
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
	private static CSimClock		iSimClock = CSimClock.getInstance();

	private static CAirportTable iAirportTable = new CAirportTable();
	private static List<IDrawingObject> iDrawingObjectList = Collections.synchronizedList(new ArrayList<IDrawingObject>());
	
	
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
		CAtsolSimMain.getInstance().loadEnvironment(new File("C:\\\\Users\\\\cp511\\\\git\\\\cp5113\\\\ATSOL_SIM\\\\SimStudy\\\\Test001_Project.txt"));		
		CAtsolSimMain.getInstance().createDrawingObjectList();
		CAtsolSimMain.getInstance().openGui();
		
	}
	

	private void openGui() {
		// TODO Auto-generated method stub
		Application.launch(iGUI.getClass());
	}

	private void createAirportTables(ArrayList<File> aFileArrayLise) {
		// TODO Auto-generated method stub
		iAirportTable.createTable(aFileArrayLise);
	}
	
	private void createDrawingObjectList() {
		CAirport lAirport = (CAirport)(iAirportTable.getElementTable().get("RKSI"));
		Iterator<CTaxiwayLink> iter = lAirport.getTaxiwayLinkList().iterator();		
		while(iter.hasNext()) {
			CTaxiwayLink lTaxiwayLink = iter.next();			
			iDrawingObjectList.add(lTaxiwayLink);
//			System.out.println(lTaxiwayLink.getNodeList().get(0).getCoordination() +"," + lTaxiwayLink.getNodeList().get(1).getCoordination());
		}
	}

	private void loadEnvironment(File aProjectFile) {
		ArrayList<File> lAirportFileList = new ArrayList<File>();
		
		// Read Project File				
		try {			
			BufferedReader lBR = new BufferedReader(new FileReader(aProjectFile));
			String lLine = "";
			String lLinePrev = "";
			while(true ) {
				lLinePrev = lLine;
				switch(lLinePrev) {
				case "@Airport":
					while(true) {
						lLine = lBR.readLine();
						if(lLine.contains("@")) {lLinePrev = lLine; break;}
						lAirportFileList.add(new File(aProjectFile.getParent().toString()+lLine));
					}
					createAirportTables(lAirportFileList);
					
				case "@ViewPoint":
					lLine = lBR.readLine();
					String[] lviewPoint= lLine.split(",");
					iViewPoint = new CCoordination(Double.parseDouble(lviewPoint[0]),Double.parseDouble(lviewPoint[1]));
					iViewPointR= Double.parseDouble(lviewPoint[2]);
				default:
				}
				
				if(!lLinePrev.contains("@"))lLine = lBR.readLine();
				if(lLine==null) break;
			}
			
			
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
	public synchronized void setiAirportTable(CAirportTable aIAirportTable) {
		iAirportTable = aIAirportTable;
	}
	public synchronized List<IDrawingObject> getiDrawingObjectList() {
		return iDrawingObjectList;
	}
	public synchronized void setiDrawingObjectList(List<IDrawingObject> aIDrawingObjectList) {
		iDrawingObjectList = aIDrawingObjectList;
	}
	public synchronized CCoordination getiViewPoint() {
		return iViewPoint;
	}
	public synchronized void setiViewPoint(CCoordination aIViewPoint) {
		iViewPoint = aIViewPoint;
	}
	public synchronized Double getiViewPointR() {
		return iViewPointR;
	}
	public synchronized void setiViewPointR(Double aIViewPointR) {
		iViewPointR = aIViewPointR;
	}
	public CSimClock getSimClock() {
		return iSimClock;
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






