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
		expected += " {\n}\n";
		
		assertEquals(expected, outBytes.toString());
	}
	
	@Test
	public void testReadSimpleHeader() {
		Inspector inspector = new Inspector();
		BasicObject obj = new BasicObject();
		
		inspector.inspect(obj, true);
		String expected = "class BasicObject";
		expected += " extends BasicSuperclass";
		expected += " {\n}\n";

		assertEquals(expected, outBytes.toString());
	}
	
	
	@Test
	public void testFields() {
		Inspector inspector = new Inspector();
		FieldObject obj = new FieldObject();

		inspector.inspect(obj, true);
		
		String expected = "class FieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "public static int anInteger\n";
		expected += "private class java.lang.String aString\n";
		expected += "int anotherInteger\n";
		expected +=	"}\n";
		
		assertEquals(expected, outBytes.toString());
	}

}
