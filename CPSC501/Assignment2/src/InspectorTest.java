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
		System.setOut(output);
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(null);
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
		expected += "\tpublic BasicInterfaceObject()\n";
		expected +=	"}\n\n";
		
		assertEquals(expected, outBytes.toString());
	}
	
	@Test
	public void testReadSimpleHeader() {
		Inspector inspector = new Inspector();
		BasicObject obj = new BasicObject();
		
		inspector.inspect(obj, false);
		String expected = "class BasicObject";
		expected += " extends BasicSuperclass";
		expected += " {\n";
		expected += "\tpublic BasicObject()\n";
		expected +=	"}\n\n";

		assertEquals(expected, outBytes.toString());
	}
	
	
	@Test
	public void testFields() {
		Inspector inspector = new Inspector();
		FieldObject obj = new FieldObject(2);

		inspector.inspect(obj, false);
		
		String expected = "class FieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "\tpublic static int anInteger = 1\n";
		expected += "\tprivate class java.lang.String aString = XXX\n";
		expected += "\tint anotherInteger = 2\n";
		expected += "\tclass BasicSuperclass obj = BasicObject 1190900417\n";
		expected += "\tpublic FieldObject(int)\n";
		expected +=	"}\n\n";
		
		//checks everything up to the hash code
		assertEquals(expected.substring(0, expected.indexOf("1190900417") - 1), outBytes.toString().substring(0, expected.indexOf("1190900417") - 1));
	}
	
	@Test
	public void testArrayFields() {
		Inspector inspector = new Inspector();
		ArrayFieldObject obj = new ArrayFieldObject();
		
		inspector.inspect(obj, false);
		
		String expected = "class ArrayFieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "\tprivate [I anArray = []\n";
		expected += "\tpublic ArrayFieldObject()\n";
		expected +=	"}\n\n";

		assertEquals(expected, outBytes.toString());
		
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
		
		assertEquals(outBytes.toString().contains(method1), true);
		assertEquals(outBytes.toString().contains(method2), true);
		assertEquals(outBytes.toString().contains(method3), true);
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
		
		assertEquals(outBytes.toString().contains(constructor1), true);
		assertEquals(outBytes.toString().contains(constructor2), true);
		assertEquals(outBytes.toString().contains(constructor3), true);
		assertEquals(outBytes.toString().contains(constructor4), true);
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
		assertEquals(outBytes.toString().contains("(recurse)"), true);
		assertEquals(outBytes.toString().contains(expected), true);
		
	}
	
	

}
