
public class ArrayFieldObject extends BasicSuperclass{
	private int[] anArray;
	
	public ArrayFieldObject() {
		anArray = new int[10];
		
		for (int i=0; i<10; i++) {
			anArray[i] = i;
		}
	}
	
}
