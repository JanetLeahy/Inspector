
public class ArrayFieldObject {
	private int[] anArray;
	public Object[] anObjArray;
	private boolean[][] multiArray;
	
	public ArrayFieldObject() {
		anArray = new int[10];
		multiArray = new boolean[5][2];
		
		for (int i=0; i<10; i++) {
			anArray[i] = i;
		}
		
		anObjArray = new Object[2];
		anObjArray[0] = new BasicObject();
		anObjArray[1] = new FieldObject(6);
		
		multiArray[2][0] = true;
	}
	
}
