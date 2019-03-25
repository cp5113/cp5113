package test.clone;
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

public class CloneObjectTestDrive {
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
	public static void main(String args[]) {
		
		String a = "123";
		System.out.println(a);
		
		change(a);
		System.out.println(a);
		
		
		Person studnet = new Person("SJ");
		System.out.println(studnet.name);
		
		changeName(studnet);
		System.out.println(studnet.name);
	}
	public static void change(String aa) {
		aa = aa.concat("234");
	}
	public static void changeName(Person aperson) {
		aperson.name = "WW";
	}
	

}
class Person  {
	  String name;

	  public Person(String name) {
	    this.name = name;
	  }
}







