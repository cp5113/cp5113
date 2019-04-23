package util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
 * @date : Mar 20, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 20, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class SReadCSV {
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
	
	public static HashMap<String,Object> readCSV(File aFile, Class<?> aOutputClass,String aSeparator) {
		System.out.println("Reading File : " + aFile.getName());
		// Create Output Object List
		HashMap<String, Object> oOutputObjectList = new HashMap<String,Object>();
		
		
		
		/*
		 * Read CSV
		 */
		try {
			// Read Header
			BufferedReader lBR 					= new BufferedReader(new FileReader(aFile));			
			String[] lHeader   					= lBR.readLine().split(aSeparator);
			int[]   lHeaderLocatedInObjectField = new int[lHeader.length];
			
			// Get Object Field and Match Header with Field
			Field[] lObjectField 				= aOutputClass.getDeclaredFields();
			int[]   lObjectFieldLocatedInHeader = new int[lObjectField.length];
			for(int loopHeader = 0; loopHeader < lHeader.length; loopHeader++) {
				int lIndexInFieldPrev = 99999;
				for(int loopField = 0; loopField < lObjectField.length; loopField++) {
					int lIndexInField = lObjectField[loopField].getName().indexOf(lHeader[loopHeader]);				
					if(lIndexInField>=0 && lIndexInField<lIndexInFieldPrev) {
						lHeaderLocatedInObjectField[loopHeader] = loopField;
						lObjectFieldLocatedInHeader[loopField]	= loopHeader;
						lIndexInFieldPrev = lIndexInField;
					}
					
				}			
			}
			
			// Read Data
			String lALine = "";
			while((lALine = lBR.readLine()) != null) {
				String[] lDataSplited = lALine.split(aSeparator);
				if(lDataSplited.length==1 && lDataSplited[0].equalsIgnoreCase("")) {
					continue;
				}
				
				// Create new Object
				Object aOutputObject = null;
				try {
					aOutputObject =aOutputClass.getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				/*
				 * Divide Data using Header information
				 */				
				for(int loopH = 0; loopH<lHeader.length;loopH++) {
					setField(aOutputObject, lObjectField[lHeaderLocatedInObjectField[loopH]].getName(), lObjectField[lHeaderLocatedInObjectField[loopH]].getType().toString(), lDataSplited[loopH]);					
				}
				oOutputObjectList.put(aOutputObject.toString(), aOutputObject);
			}
			lBR.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Read CSV Exception : " + aFile);
			e.printStackTrace();
		}
		
		
		
		return oOutputObjectList;
		
	}
	
	
	
	
	public static HashMap<String,HashMap<String,String>> readCSVHashMap(File aFile, Class<?> aOutputClass,String aSeparator) {
		System.out.println("Reading File : " + aFile.getName());
		// Create Output Object List
		// To Unique data
		 HashMap<String,HashMap<String,String>>  lOutputHashMap = new  HashMap<String,HashMap<String,String>>();
		
		
		
		/*
		 * Read CSV
		 */
		try {
			// Read Header
			BufferedReader lBR 					= new BufferedReader(new FileReader(aFile));			
			String[] lHeader   					= lBR.readLine().split(aSeparator);
			
			
			// Read Data
			String lALine = "";
			while((lALine = lBR.readLine()) != null) {
				String[] lDataSplited = lALine.split(aSeparator);				
				if(lDataSplited.length==0) {
					continue;
				}
				if(lDataSplited.length==1 && lDataSplited[0].equalsIgnoreCase("")) {
					continue;
				}
				
				// Create a line data
				// Fieldname, Data
				HashMap<String,String> lAlineHashMap = new HashMap<String,String>();
				StringBuilder lUniqueKey = new StringBuilder();
				for(int loopHeader = 0; loopHeader < lHeader.length; loopHeader++) {
					try {
//						System.out.println(lHeader[loopHeader]);
						lAlineHashMap.put(lHeader[loopHeader], lDataSplited[loopHeader]);
						lUniqueKey.append(lDataSplited[loopHeader]);
					}catch(Exception e) {
						lAlineHashMap.put(lHeader[loopHeader], "");
						lUniqueKey.append("");
					}
					
				}
				
				// Merge to global Data
				lOutputHashMap.put(lUniqueKey.toString(), lAlineHashMap);
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Read CSV Exception : " + aFile);
			e.printStackTrace();
		}
		
		
		
		return lOutputHashMap;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static HashMap<String,String> readCSVaLine(String[] aHeader, String[] aString){
		HashMap<String,String> lOutput = new HashMap<String,String>();
		for(int i = 0 ; i<aHeader.length;i++) {
			lOutput.put(aHeader[i], aString[i]);
		}
		return lOutput;
		
	}
	private static void setField(Object aObject, String aFieldName, String aFieldType, String aInputData) {
		try {
			
			aObject.getClass().getDeclaredField(aFieldName).setAccessible(true);
			Field field = aObject.getClass().getDeclaredField(aFieldName);
			if(Modifier.isPrivate(field.getModifiers())) {
				field.setAccessible(true);
			}
			
			
			if(aFieldType.equalsIgnoreCase("short")) {
				if(aInputData.length()==0) {
					field.set(aObject, Short.parseShort("0"));
				}else {
					field.set(aObject, Short.parseShort(aInputData));
				}								
			}else if(aFieldType.equalsIgnoreCase("int")) {
				if(aInputData.length()==0) {
					field.set(aObject, Integer.parseInt("0"));
				}else {
					field.set(aObject, Integer.parseInt(aInputData));
				}				
			}else if(aFieldType.equalsIgnoreCase("long")) {
				if(aInputData.length()==0) {
					field.set(aObject, Long.parseLong("0"));
				}else {
					field.set(aObject, Long.parseLong(aInputData));
				}				
			}else if(aFieldType.equalsIgnoreCase("double")) {
				if(aInputData.length()==0) {
					field.set(aObject, Double.parseDouble("0"));
				}else {
					field.set(aObject, Double.parseDouble(aInputData));
				}	
			}else if(aFieldType.equalsIgnoreCase("float")) {
				if(aInputData.length()==0) {
					field.set(aObject, Float.parseFloat("0"));
				}else {
					field.set(aObject, Float.parseFloat(aInputData));
				}	
			}else if(aFieldType.equalsIgnoreCase("class java.lang.String")) {
				field.set(aObject, aInputData);
			}else if(aFieldType.equalsIgnoreCase("class java.util.Date")) {

				if (aInputData.contains("-")) {
					SimpleDateFormat l_tempFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date l_tempDate			      = l_tempFormat.parse(aInputData);
					field.set(aObject, l_tempDate);
				} else {
					SimpleDateFormat l_tempFormat = new SimpleDateFormat("HH:mm:ss");
					Date l_tempDate			      = l_tempFormat.parse(aInputData);
					field.set(aObject, l_tempDate);	
				}
			}
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
//	public static void main(String args[]) {
//		
//		HashMap<String, Object> aOutput = SReadCSV.readCSV(new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\src\\util\\file\\TestCSVObject.csv"), TestCSVObject.class,",");
//		System.out.println("Testing Main Code is working..");
//		
//	}
	

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






