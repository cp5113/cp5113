package elements.table;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.util.geo.CCoordination;
import util.file.SReadCSV;

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
 * @date : Mar 26, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 26, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CWaypointTable extends ATable {
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
			if(aFileArrayList.get(loopFile).getName().contains("Waypoint.csv")) {
				createWaypointTable(aFileArrayList.get(loopFile));
			}
		}
				
	}
	
	
	

	private void createWaypointTable(File aFile) {
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
			
			
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);
			
			// Extract Property
			String lName = lData.get("Name");
			CCoordination lCoordination = new CCoordination(parseDouble(lData.get("XCoord")), parseDouble(lData.get("YCoord")));
			
			// Create Object
			CWaypoint lWaypoint = new CWaypoint(lName,lCoordination);
			
			addElement(lWaypoint);
		} // for(String key : lDataList.keySet()) 
	}
	
		
	
	
	public static void main(String args[]) {
		CWaypointTable my = new CWaypointTable();
		ArrayList<File> aFile = new ArrayList<File>();
		aFile.add(new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\airspace\\Waypoint.csv"));
		my.createTable(aFile);
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






