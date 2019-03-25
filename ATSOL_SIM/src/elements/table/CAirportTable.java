/**
 * ATSOL_SIM
 * elements.table
 * CAirportTable.java
 */
package elements.table;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import elements.AElement;
import elements.IElementControlledByClock;
import elements.facility.CAirport;
import elements.facility.CSpot;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;
import elements.util.geo.EGEOUnit;
import elements.util.geo.EUnit;
import util.file.CListUtil;
import util.file.SReadCSV;

/**
 * @author S. J. Yun
 *
 */
public class CAirportTable extends ATable {

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
	
	/** (non-Javadoc)
	 * @see elements.table.ATable#readCSV(java.io.File)
	 */
	@Override
	public void createTable(ArrayList<File> aFileArrayList) {
		// TODO Auto-generated method stub
		
		for(int loopFile = 0; loopFile< aFileArrayList.size() ; loopFile++) {
			if(aFileArrayList.get(loopFile).getName().contains("Airport.csv")) {
				createAirport(aFileArrayList.get(loopFile));
			}else if(aFileArrayList.get(loopFile).getName().contains("AirportGroundNode")) {
				createGroundNode(aFileArrayList.get(loopFile));
			}else if(aFileArrayList.get(loopFile).getName().contains("AirportGroundSPOT")) {
				createGroundSpot(aFileArrayList.get(loopFile));
			}else if(aFileArrayList.get(loopFile).getName().contains("AirportGroundLink")) {
				createGroundLink(aFileArrayList.get(loopFile));
			}
		}
		
				
	}
	
	
	private void createGroundLink(File aFile) {
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		/*
		 * Create Airport Table
		 */
		for(String key : lDataList.keySet()) {
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);

			// get Properties
			String lTaxiwayLinkName = lData.get("Name");
			String lTaxiwayNodeConnected1 = lData.get("StartNode");
			String lTaxiwayNodeConnected2 = lData.get("EndNode");
			String lAirportStr = lData.get("Airport");
			HashMap<String,Boolean> lAPCAvailable = new HashMap<String,Boolean>();
			lAPCAvailable.put("A",lData.get("A").equalsIgnoreCase("A"));
			lAPCAvailable.put("B",lData.get("B").equalsIgnoreCase("B"));
			lAPCAvailable.put("C",lData.get("C").equalsIgnoreCase("C"));
			lAPCAvailable.put("D",lData.get("D").equalsIgnoreCase("D"));
			lAPCAvailable.put("E",lData.get("E").equalsIgnoreCase("E"));
			lAPCAvailable.put("F",lData.get("F").equalsIgnoreCase("F"));
			
			// Find Linked Airport			
			CAirport lTargetAirport  = (CAirport) getElementTable().get(lAirportStr);

			// Create Adjacent Node in Node class
			CTaxiwayNode lStartNode = (CTaxiwayNode) CListUtil.searchElementInListUsingName(lTargetAirport.getTaxiwayNodeList(), lTaxiwayNodeConnected1);			
			CTaxiwayNode lEndNode   = (CTaxiwayNode) CListUtil.searchElementInListUsingName(lTargetAirport.getTaxiwayNodeList(), lTaxiwayNodeConnected2);
			lStartNode.getAdjacentNode().add(lEndNode);
			lEndNode.getAdjacentNode().add(lStartNode);
			
			// Create Object and Set Properties
			CTaxiwayLink lTaxiwayLink = new CTaxiwayLink();
			lTaxiwayLink.setName(lTaxiwayLinkName);
			lTaxiwayLink.setDistance(CCoordination.calculateDistance(lStartNode.getCoordination(), lEndNode.getCoordination()));
			lTaxiwayLink.setOrigin(lStartNode);
			lTaxiwayLink.setDestination(lEndNode);
			lTaxiwayLink.getNodeList().add(lStartNode);
			lTaxiwayLink.getNodeList().add(lEndNode);
			lTaxiwayLink.setOwnerObject(lTargetAirport);
//			System.out.println(lTaxiwayLink.getNodeList().get(0).getCoordination()+","+lTaxiwayLink.getNodeList().get(1).getCoordination());
			
			// Add to Airport
			lTargetAirport.getTaxiwayLinkList().add(lTaxiwayLink);

		} // for(String key : lDataList.keySet()) 
		
		
	}


	private void createGroundSpot(File aFile) {
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		/*
		 * Create Airport Table
		 */
		for(String key : lDataList.keySet()) {
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);

			// get Properties
			String lSpotName = lData.get("Name");
			String lAirportStr = lData.get("Airport");
			HashMap<String,Boolean> lAPCAvailable = new HashMap<String,Boolean>();
			lAPCAvailable.put("A",lData.get("A").equalsIgnoreCase("A"));
			lAPCAvailable.put("B",lData.get("B").equalsIgnoreCase("B"));
			lAPCAvailable.put("C",lData.get("C").equalsIgnoreCase("C"));
			lAPCAvailable.put("D",lData.get("D").equalsIgnoreCase("D"));
			lAPCAvailable.put("E",lData.get("E").equalsIgnoreCase("E"));
			lAPCAvailable.put("F",lData.get("F").equalsIgnoreCase("F"));
			
			// Find Linked Airport			
			CAirport lTargetAirport  = (CAirport) getElementTable().get(lAirportStr);

			// Find Linked TaxiwayNode
			Iterator<CTaxiwayNode> iter = lTargetAirport.getTaxiwayNodeList().iterator();
			CTaxiwayNode lTargetTaxiwayNode = null;
			while(iter.hasNext()) {
				lTargetTaxiwayNode =iter.next();
				if(lTargetTaxiwayNode.getName().equalsIgnoreCase(lSpotName)) {
					 break;
				}
			}
			
			// Create Object and Set Properties
			CSpot lSpot = new CSpot(lTargetTaxiwayNode);
			lSpot.setACTypeAPC(lAPCAvailable);
			lSpot.setOwnerObject(lTargetAirport);

			// Add to Airport
			lTargetAirport.getSpotList().add(lSpot);

		} // for(String key : lDataList.keySet()) 
		
		
	}


	private void createGroundNode(File aFile) {
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		
		/*
		 * Create Airport Table
		 */
		for(String key : lDataList.keySet()) {
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);

			// Find Linked Airport
			String lAirportStr = lData.get("Airport");
			CAirport lTargetAirport  = (CAirport) getElementTable().get(lAirportStr);
			Hashtable<String, AElement> a = getElementTable();
			// Set Properties
			CTaxiwayNode lTaxiwayNode = new CTaxiwayNode(lData.get("Name"),new CCoordination(Double.parseDouble(lData.get("Xcoord")), Double.parseDouble(lData.get("Ycoord")),EGEOUnit.TM));
			lTaxiwayNode.setNameGroup(lData.get("NameGroup"));
			lTaxiwayNode.setOwnerObject(lTargetAirport); // Strength Reference
			
			// Add to Airport
			lTargetAirport.getTaxiwayNodeList().add(lTaxiwayNode);
//			System.out.println(lTaxiwayNode.getCoordination().getXCoordination() + "," + lTaxiwayNode.getCoordination().getYCoordination());
		} // for(String key : lDataList.keySet()) 
		
	}


	protected void createAirport(File aFile) {
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		
		/*
		 * Create Airport Table
		 */
		for(String key : lDataList.keySet()) {
			// Create Object
			CAirport lAirport = new CAirport();
			
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);
			
			// Create Object Properties - String
			lAirport.setName(lData.get("Name"));
			lAirport.setOperator(lData.get("Owner"));
			lAirport.setAirportIATA(lData.get("AirportIATA"));
			lAirport.setAirportICAO(lData.get("AirportICAO"));
			
			// Create Object Properties - Class			
			lAirport.setElevation(new CAltitude(Double.parseDouble(lData.get("Elevation")),EGEOUnit.FEET));						
			lAirport.setVariation(new CDegree(Double.parseDouble(lData.get("Variation"))));
			lAirport.setARP(new CCoordination(Double.parseDouble(lData.get("Xcoord")), Double.parseDouble(lData.get("Ycoord")),EGEOUnit.TM));
			
			// Merge to Table
			addElement(lAirport);
			
		} // for(String key : lDataList.keySet()) 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) {
		CAirportTable my = new CAirportTable();
		File[] aFile = new File[4];
		aFile[0] = new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\authority\\Airport.csv");
		aFile[1] = new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\network\\AirportGroundNode.csv");
		aFile[2] = new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\network\\AirportGroundSPOT.csv");
		aFile[3] = new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\network\\AirportGroundLink.csv");
		
//		my.createTable(aFile);
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






