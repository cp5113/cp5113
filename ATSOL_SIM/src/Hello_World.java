import test.objCounter.TEST;
import test.objCounter.TEST_AB;
import elements.mobile.human.CAreaController;
import elements.util.geo.CCoordination;
import math.basic.BasicMath;


public class Hello_World {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello from S. J. Yun~ Wo ttw");
		
		CAreaController Con1 = new CAreaController();
		CAreaController Con2 = new CAreaController();
		CAreaController Con3 = new CAreaController();
		CAreaController Con4 = new CAreaController();
		
		System.out.println(Con1.getID());
		System.out.println(Con2.getID());
		System.out.println(Con3.getID());
		System.out.println(Con4.getID());
		
		
		System.out.println(Con1.getName());
		System.out.println(Con2.getName());
		
		
		TEST t1 = new TEST();
		TEST t2 = new TEST();
		
		System.out.println(t1.objID + "\\" + t1.ww);
		System.out.println(t2.objID + "\\" + t2.ww);
		System.out.println(t1.Type);
		
		
		System.out.println(BasicMath.power(2,0));
		
		CCoordination pt1 = new CCoordination(0, 0);
		CCoordination pt2 = new CCoordination(1, 1);
		
		System.out.println("distance : " + pt1.calculateDistance(pt2));
		
		
		
		
			}

}
