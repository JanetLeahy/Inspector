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

		inspector.inspect(obj, true);
		
		String expected = "class BasicInterfaceObject";
		expected += " extends BasicSuperclass";
		expected += " implements BasicInterface1, BasicInterface2";
		expected += " {\n}\n\n";
		
		assertEquals(expected, outBytes.toString());
	}
	
	@Test
	public void testReadSimpleHeader() {
		Inspector inspector = new Inspector();
		BasicObject obj = new BasicObject();
		
		inspector.inspect(obj, true);
		String expected = "class BasicObject";
		expected += " extends BasicSuperclass";
		expected += " {\n}\n\n";

		assertEquals(expected, outBytes.toString());
	}
	
	
	@Test
	public void testFields() {
		Inspector inspector = new Inspector();
		FieldObject obj = new FieldObject(2);

		inspector.inspect(obj, true);
		
		String expected = "class FieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "\tpublic static int anInteger = 1\n";
		expected += "\tprivate class java.lang.String aString = XXX\n";
		expected += "\tint anotherInteger = 2\n";
		expected += "\tclass BasicSuperclass obj = BasicObject 1190900417\n";
		expected +=	"}\n\n";
		
		//checks everything up to the hash code
		assertEquals(expected.substring(0, expected.indexOf("1190900417") - 1), outBytes.toString().substring(0, expected.indexOf("1190900417") - 1));
	}
	
	@Test
	public void testArrayFields() {
		Inspector inspector = new Inspector();
		ArrayFieldObject obj = new ArrayFieldObject();
		
		inspector.inspect(obj, true);
		
		String expected = "class ArrayFieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "\tprivate [I anArray = []\n";
		expected +=	"}\n\n";

		assertEquals(expected, outBytes.toString());
		
	}
	
	@Test
	public void testMethods() {
		Inspector inspector = new Inspector();
		BasicMethodObject obj = new BasicMethodObject();
		
		inspector.inspect(obj, true);
		
		//methods appear in any order, so just check that the result contains these
		String method1 = "\tpublic static void sayHi()\n";
		String method2 = "\tpublic int aMethod(boolean)\n";
		String method3 = "\tprivate java.lang.String anotherMethod(int, int)\n";
		
		assertEquals(outBytes.toString().contains(method1), true);
		assertEquals(outBytes.toString().contains(method2), true);
		assertEquals(outBytes.toString().contains(method3), true);
	}
	
	

}
