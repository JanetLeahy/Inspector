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
		FieldObject obj = new FieldObject(2);

		inspector.inspect(obj, true);
		
		String expected = "class FieldObject";
		expected += " extends BasicSuperclass";
		expected +=	" {\n";
		expected += "public static int anInteger = 1\n";
		expected += "private class java.lang.String aString = XXX\n";
		expected += "int anotherInteger = 2\n";
		expected += "class BasicSuperclass obj = BasicObject 1190900417\n";
		expected +=	"}\n";
		
		//checks everything up to the hash code
		assertEquals(expected.substring(0, expected.indexOf("1190900417") - 1), outBytes.toString().substring(0, expected.indexOf("1190900417") - 1));
	}

}
