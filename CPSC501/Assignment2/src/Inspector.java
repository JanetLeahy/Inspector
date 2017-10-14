/*
 * Assignment 2 for CPSC501
 * Janet Leahy, 10104311, T06
 * 
 * Main class, containing the method used to perform the inspections
 * 
 * @author Janet Leahy
 * 
 */


public class Inspector {

	/*
	 * Inspects the object passed to find out its name, functions, 
	 * constructors and fields, as well as those of any superclasses
	 * and interfaces, and prints the results to standard output
	 * 
	 * @param obj - the object to be inspected
	 * @param recursive - if true, fully inspects the fields of the passed object,
	 * otherwise, simply returns the object's information
	 */
	public void inspect(Object obj, boolean recursive) {
		printHeader(obj);
		//end of the first line of the class declaration
		System.out.print(" {\n");
		
		
		//end of the current class
		System.out.print("}\n");
	}
	
	
	
	
	//prints the class name of the given object, as well as the names of the 
	// direct superclass and any interfaces, in a single line
	public void printHeader(Object obj) {
		Class classObj = obj.getClass();
		System.out.print("class " + classObj.getName());
		
		//prints the direct superclass and any interfaces
		Class superclass = classObj.getSuperclass();
		if (superclass != null) {
			System.out.print(" extends " + superclass.getName());
		}
		
		Class[] interfaces = classObj.getInterfaces();
		if (interfaces != null) {
			System.out.print(" implements");
			for (int i=0; i< interfaces.length; i++) {
				if (i > 0) {
					System.out.print(",");
				}
				System.out.print(" " + interfaces[i].getName());
			}
		}
	}
}
