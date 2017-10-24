import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InspectorTest {
	
	ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
	PrintStream output = new PrintStream(outBytes);
	
	//allows for checking the console output in the unit tests
	@Before
	public void setUp() throws Exception {
		//System.setOut(output);
	}

	@After
	public void tearDown() throws Exception {
		//System.setOut(null);
	}

	
	@Test
	public void testReadHeaderWithInterfaces() {
		Inspector inspector = new Inspector();
		BasicInterfaceObject obj = new BasicInterfaceObject();

		inspector.inspect(obj, false);
		
		String expected = "class BasicInterfaceObject";
		expected += " extends BasicSuperclass";
		expected += " implements BasicInterface1, BasicInterface2";
		expected += " {\n";
		
		assert(outBytes.toString().contains(expected));
	}
	
	@Test
	public void testReadSimpleHeader() {
		Inspector inspector = new Inspector();
		BasicObject obj = new BasicObject();
		
		inspector.inspect(obj, false);
		String expected = "class BasicObject";
		expected += " extends BasicSuperclass";
		expected += " {\n";

		assert(outBytes.toString().contains(expected));
	}
	
	
	@Test
	public void testFields() {
		Inspector inspector = new Inspector();
		FieldObject obj = new FieldObject(2);

		inspector.inspect(obj, false);

		String field1 = "\tpublic static int anInteger = 1\n";
		String field2 = "\tprivate class java.lang.String aString = XXX\n";
		String field3 = "\tint anotherInteger = 2\n";
		String field4 = "\tclass BasicSuperclass obj = BasicObject 1190900417\n";
		
		//checks everything up to the hash code
		assert(outBytes.toString().contains(field1));
		assert(outBytes.toString().contains(field2));
		assert(outBytes.toString().contains(field3));
		assert(outBytes.toString().contains(field4));
	}
	
	@Test
	public void testArrayFields() {
		Inspector inspector = new Inspector();
		ArrayFieldObject obj = new ArrayFieldObject();
		
		inspector.inspect(obj, false);
		
		String array1 = "private int[] anArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]";
		String array2 = "public java.lang.Object[] anObjArray = [BasicObject ";
		String array3 = "private boolean[][] multiArray = [[false, false], [false, false], [true, false], [false, false], [false, false]]";
		
		assert(outBytes.toString().contains(array1));
		assert(outBytes.toString().contains(array2));
		assert(outBytes.toString().contains(array3));
	}
	
	@Test
	public void testMethods() {
		Inspector inspector = new Inspector();
		BasicMethodObject obj = new BasicMethodObject();
		
		inspector.inspect(obj, false);
		
		//methods appear in any order, so just check that the result contains these
		String method1 = "\tpublic static void sayHi()\n";
		String method2 = "\tpublic int aMethod(class java.lang.String)\n";
		String method3 = "\tprivate java.lang.String anotherMethod(int, int)\n";
		
		assert(outBytes.toString().contains(method1));
		assert(outBytes.toString().contains(method2));
		assert(outBytes.toString().contains(method3));
	}
	
	@Test
	public void testConstructors() {
		Inspector inspector = new Inspector();
		BasicConstructorObject obj = new BasicConstructorObject();

		inspector.inspect(obj, false);
		
		//just check the output contains all of these (may appear in a different
		// order each run)
		String constructor1 = "\tpublic BasicConstructorObject()\n";
		String constructor2 = "\tpublic BasicConstructorObject(int)\n";
		String constructor3 = "\tpublic BasicConstructorObject(class java.lang.String, boolean)\n";
		String constructor4 = "\tpublic BasicConstructorObject(class BasicObject)\n";
		
		assert(outBytes.toString().contains(constructor1));
		assert(outBytes.toString().contains(constructor2));
		assert(outBytes.toString().contains(constructor3));
		assert(outBytes.toString().contains(constructor4));
	}
	
	@Test
	public void testRecursion() {
		Inspector inspector = new Inspector();
		FieldObject obj = new FieldObject(2);

		inspector.inspect(obj, true);
				
		String expected = "class BasicObject";
		expected += " extends BasicSuperclass";
		expected += " {\n";
		expected += "\tpublic BasicObject()\n";
		expected +=	"}\n\n";
		
		//checks everything up to the hash code
		assert(outBytes.toString().contains("(recurse)"));
		assert(outBytes.toString().contains(expected));
	}
	
	@Test
	public void testArrayRecursion() {
		Inspector inspector = new Inspector();
		ArrayFieldObject obj = new ArrayFieldObject();
		
		inspector.inspect(obj, true);
		
		String array2 = "public java.lang.Object[] anObjArray = [BasicObject ";
		String expectedObj1 =  "class BasicObject";
		expectedObj1 += " extends BasicSuperclass";
		expectedObj1 += " {\n";
		expectedObj1 += "\tpublic BasicObject()\n";
		expectedObj1 +=	"}\n\n";
		
		
		String expectedObj2 = "class FieldObject";
		expectedObj2 += " extends BasicSuperclass";
		expectedObj2 +=	" {\n";
		expectedObj2 += "\tpublic static int anInteger = 1\n";
		expectedObj2 += "\tprivate class java.lang.String aString = XXX\n";
		expectedObj2 += "\tint anotherInteger = 6\n";
		expectedObj2 += "\tclass BasicSuperclass obj = BasicObject ";
		//simply stops before the changing identityHashMap
		
		
		assert(outBytes.toString().contains(array2));
		assert(outBytes.toString().contains(" (recurse)"));
		assert(outBytes.toString().contains(expectedObj1));
		assert(outBytes.toString().contains(expectedObj2));
	}

}
