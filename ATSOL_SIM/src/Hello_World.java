import test.objCounter.TEST;
import elements.mobile.CAreaController;


public class Hello_World {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello from S. J. Yun~ Wo ttw");
		
		CAreaController Con1 = new CAreaController();
		CAreaController Con2 = new CAreaController();
		CAreaController Con3 = new CAreaController();
		CAreaController Con4 = new CAreaController();
		
		System.out.println(Con1.getMobileID());
		System.out.println(Con2.getMobileID());
		System.out.println(Con3.getMobileID());
		System.out.println(Con4.getMobileID());
		
		TEST t1 = new TEST();
		TEST t2 = new TEST();
		
		System.out.println(t1.objID + "\\" + t1.ww);
		System.out.println(t2.objID + "\\" + t2.ww);
		
	}

}
