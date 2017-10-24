
public class BasicMethodObject {
			
	public BasicMethodObject() {
		//do nothing
	}
	
	public static void sayHi() {
		System.out.println("Hello world");
	}
	
	public int aMethod(String aString) {
		anotherMethod(1,2);
		return 10;
	}
	
	private String anotherMethod(int x1, int x2) {
		return "x1 + x2 = " + (x1 + x2);
	}
			
}
