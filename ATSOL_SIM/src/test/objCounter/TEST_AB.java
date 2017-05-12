package test.objCounter;

public abstract class TEST_AB {
	private static int ObjCount = 0;
	
	public TEST_AB(){
		ObjCount += 1;
	}
	
	public static int getObjectCount(){
		
		return ObjCount;
	}
}
