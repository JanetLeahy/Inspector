
public class FieldObject extends BasicSuperclass {
	public static int anInteger = 1;
	private String aString;
	int anotherInteger;
	
	BasicSuperclass obj;
	
	public FieldObject(int i) {
		aString = "XXX";
		anotherInteger = i;
		
		obj = new BasicObject();
	} 
}
