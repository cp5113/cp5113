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


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Hashtable;

import elements.AElement;

public abstract class ATable {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private List<AElement> iElementList = Collections.synchronizedList(new ArrayList<AElement>());
	private Hashtable<String, AElement> iElementTable = new Hashtable<String, AElement>();
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public List<AElement> getElementList(){
		return iElementList;
	} 
	public Hashtable<String,AElement> getElementTable(){
		return iElementTable;
	} 
	
	public void setElementList(ArrayList<AElement> aElimentList){
		iElementList = aElimentList;		
		for(int i = 0; i < aElimentList.size();i++) {
			iElementTable.put(aElimentList.get(i).toString(),aElimentList.get(i));
		}
	} 
	public void addElement(AElement aElement) {
		iElementList.add(aElement);
		iElementTable.put(aElement.toString(),aElement);
	}
	public void removeElement(AElement aElement) {
		iElementList.remove(aElement);
		iElementTable.remove(aElement.toString());
	}
	
	
	
	abstract public void createTable(ArrayList<File> aFileArrayList); 
	
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






