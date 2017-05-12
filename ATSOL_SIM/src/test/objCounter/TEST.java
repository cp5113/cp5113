package test.objCounter;

public class TEST extends TEST_AB{
	
	public int objID = TEST_AB.getObjectCount();
	public int ww = 1;
	
	public TEST(){
		ww = 3;
		System.out.println("WWW");
	}

}
