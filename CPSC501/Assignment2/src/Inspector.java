import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Assignment 2 for CPSC501
 * Janet Leahy, 10104311, T03
 * 
 * Main class, containing the method used to perform the inspections
 * 
 * @author Janet Leahy
 * 
 *
 */


public class Inspector {
	//stores the objects stored in fields in a linked list,
	// from which they can be inspected after the main inspection is complete
	// if recursive is set to true
	LinkedList<Object> toInspect;
	
	public Inspector() {
		toInspect = new LinkedList<Object>();
	}

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

		printFields(obj, recursive);

		printConstructors(obj);

		printMethods(obj);

		//end of the current class
		System.out.print("}\n\n");
		
		//reads all objects stored in the linked list of objects
		// (i.e. objects stored in fields if recursive is true)and
		// inspects them

		if (!toInspect.isEmpty()) {
			System.out.print("Other objects referred to:\n---------------------------\n");
		}
		
		while (!toInspect.isEmpty()) {
			//recurse using a new inspector object, so the linked list
			// not overwritten
			Inspector subInspector = new Inspector(); 
			subInspector.inspect(toInspect.removeFirst(), false);
		}
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
		if (interfaces.length > 0) {
			System.out.print(" implements");
			for (int i=0; i< interfaces.length; i++) {
				if (i > 0) {
					System.out.print(",");
				}
				System.out.print(" " + interfaces[i].getName());
			}
		}
	}
	
	
	//prints the name, type and modifiers of all the fields in the given object
	// if recursive is set to true, add any objects stored in fields to the 
	// recursion list, and they will be fully inspected later
	public void printFields(Object obj, boolean recursive) {
		Class classObj = obj.getClass();
		Field[] ownFields = classObj.getDeclaredFields();
		ArrayList<Field> fields = new ArrayList<Field>();
		
		//copy the object's own fields into the ArrayList structure
		for (int i=0; i < ownFields.length; i++) {
			fields.add(ownFields[i]);
		}
		
		addInterfaceFields(fields, classObj);
		addSuperclassFields(fields, classObj);
		
		for (int i=0; i< fields.size(); i++) {
			//indent the fields for readability
			System.out.print("\t");
			
			printModifiers(fields.get(i).getModifiers());
			
			fields.get(i).setAccessible(true);
			
			try {
				Object value = fields.get(i).get(obj);
				
				if (value == null) {
					System.out.print(" = null");
				}
				
				else if (value.getClass().isArray()) {
					Class componentType = value.getClass();
					String classname = value.getClass().getName();
					int dimension = 0;
					
					//finds the base component type, even for multi-dimensional arrays
					for (int j=0; j<classname.length(); j++) {
						if (classname.charAt(j) == '[') {
							componentType = componentType.getComponentType();
							dimension++; 
						}
					}
										
					System.out.print(componentType.getName());
					//prints the square brackets to indicate dimension
					for (int j=0; j<dimension; j++) {
						System.out.print("[]");
					}
					System.out.print(" " + fields.get(i).getName() + " = ");
					
					boolean isObjArray = classname.contains("L");
					printArray(value, dimension, (recursive&&isObjArray));
					if (recursive&&isObjArray) {
						System.out.print(" (recurse)");
					}
				}
				else {
					System.out.print(fields.get(i).getType() + " ");
					System.out.print(fields.get(i).getName() + " = ");

					if (value.getClass() == Integer.class) {
						System.out.print(fields.get(i).getInt(obj));
					} else if (value.getClass() == Double.class) {
						System.out.print(fields.get(i).getDouble(obj));
					} else if (value.getClass() == Short.class) {
						System.out.print(fields.get(i).getShort(obj));
					} else if (value.getClass() == Long.class) {
						System.out.print(fields.get(i).getLong(obj));
					} else if (value.getClass() == Float.class) {
						System.out.print(fields.get(i).getFloat(obj));
					} else if (value.getClass() == Character.class) {
						System.out.print(fields.get(i).getChar(obj));
					} else if (value.getClass() == Boolean.class) {
						System.out.print(fields.get(i).getBoolean(obj));
					} else if (value.getClass() == Byte.class) {
						System.out.print(fields.get(i).getByte(obj));
					} else if (value.getClass() == String.class) {
						System.out.print((String) value);
					}
					else {
						//field contains an object, so investigate depending on
						// how recursion flag is set
						printObject(value);
						if (recursive) {
							//add to recursion list
							System.out.print(" (recurse)");
							toInspect.add(value);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}	
			System.out.print("\n");
		}
	}
	
	public void printArray(Object array, int dim, boolean recursive) {

		System.out.print("[");

		int len = Array.getLength(array);
		for(int j=0; j<len; j++) {
			if (dim == 1) {
				Object elem = Array.get(array, j);
				if (elem == null) {
					System.out.print("null");
				}
				else if (elem.getClass().equals(Integer.class) || elem.getClass().equals(Double.class) ||
						elem.getClass().equals(Long.class) || elem.getClass().equals(Short.class) ||
						elem.getClass().equals(Float.class) || elem.getClass().equals(Byte.class) ||
						elem.getClass().equals(Character.class) || elem.getClass().equals(Boolean.class) ||
						elem.getClass().equals(String.class)
						) {
					System.out.print(elem);
				}
				else {
					printObject(elem);
					if (recursive) {
						toInspect.add(elem);
					}
				}
			}
			else if (dim > 1) {
				printArray(Array.get(array, j), dim-1, recursive);
			}
			//don't need the comma for the last element
			if (j<len-1) {
				System.out.print(", ");
			}
		}
		System.out.print("]");
	}

	//prints the object's name and identityHashCode
	public void printObject(Object obj) {
		System.out.print(obj.getClass().getName() + " " + System.identityHashCode(obj));
	}
	
	public void printModifiers(int mod) {
		if (mod != 0) {
			//if no modifiers, don't need the space
			System.out.print(Modifier.toString(mod) + " ");
		}
	}
	
	//reads any fields from the object's interfaces and adds them to the list
	// of available fields for the original object
	public void addInterfaceFields(ArrayList<Field> fields, Class classObj) {
		Class[] interfaces = classObj.getInterfaces();
		
		for (int i=0; i<interfaces.length; i++) {
			Field[] interfaceFields = interfaces[i].getDeclaredFields();
			for (int j=0; j<interfaceFields.length; j++) {
				fields.add(interfaceFields[j]);
			}
		}
	}
	
	//reads any methods from the object's interfaces and adds them to the list
	// of available methods for the original object
	public void addInterfaceMethods(ArrayList<Method> methods, Class classObj) {
		Class[] interfaces = classObj.getInterfaces();
		
		for (int i=0; i<interfaces.length; i++) {
			Method[] interfaceMethods = interfaces[i].getDeclaredMethods();
			for (int j=0; j<interfaceMethods.length; j++) {
				methods.add(interfaceMethods[j]);
			}
		}
	}
	
	//reads any constructors from the object's interfaces and adds them to the list
	// of available constructors for the original object
	public void addInterfaceConstructors(ArrayList<Constructor> constructors, Class classObj) {
		Class[] interfaces = classObj.getInterfaces();
		
		for (int i=0; i<interfaces.length; i++) {
			Constructor[] interfaceConstructors = interfaces[i].getConstructors();
			for (int j=0; j<interfaceConstructors.length; j++) {
				constructors.add(interfaceConstructors[j]);
			}
		}
	}
	
	
	//reads any fields from the object's superclass and adds them to the list
	// of available fields for the original object. If given class does not
	// inherit directly from Object, search recursively and add its superclass'
	// superclasses' fields to the list
	public void addSuperclassFields(ArrayList<Field> fields, Class classObj) {
		Class superClass = classObj.getSuperclass();
		Field[] superFields = superClass.getDeclaredFields();
				
		for (int i=0; i<superFields.length; i++) {
			fields.add(superFields[i]);
		}
		
		//if a higher superclass exists, recursively add its fields too
		if (!superClass.equals(Object.class)) {
			addSuperclassFields(fields, superClass);
		}
	}
	
	//reads any methods from the object's superclass and adds them to the list
	// of available methods for the original object. If given class does not
	// inherit directly from Object, search recursively and add its superclass'
	// superclasses' methods to the list
	public void addSuperclassMethods(ArrayList<Method> methods, Class classObj) {
		Class superClass = classObj.getSuperclass();
		Method[] superMethods = superClass.getDeclaredMethods();
				
		for (int i=0; i<superMethods.length; i++) {
			methods.add(superMethods[i]);
		}
		
		//if a higher superclass exists, recursively add its fields too
		if (!superClass.equals(Object.class)) {
			addSuperclassMethods(methods, superClass);
		}
	}
	
	//reads any constructors from the object's superclass and adds them to the list
	// of available constructors for the original object. If given class does not
	// inherit directly from Object, search recursively and add its superclass'
	// superclasses' constructors to the list
	public void addSuperclassConstructors(ArrayList<Constructor> constructors, Class classObj) {
		Class superClass = classObj.getSuperclass();
		Constructor[] superConstructors = superClass.getConstructors();
				
		for (int i=0; i<superConstructors.length; i++) {
			constructors.add(superConstructors[i]);
		}
		
		//if a higher superclass exists, recursively add its fields too
		if (!superClass.equals(Object.class)) {
			addSuperclassConstructors(constructors, superClass);
		}
	}
	
	
	//prints the name, modifiers, return type, parameter types and exceptions 
	// thrown for each of the object's methods
	public void printMethods(Object obj) {
		Class classObj = obj.getClass();
		Method[] ownMethods = classObj.getDeclaredMethods();
		ArrayList<Method> methods = new ArrayList<Method>();
		
		//copy the object's own fields into the ArrayList structure
		for (int i=0; i < ownMethods.length; i++) {
			methods.add(ownMethods[i]);
		}

		addInterfaceMethods(methods, classObj);
		addSuperclassMethods(methods, classObj);
		
		for (int i=0; i< methods.size(); i++) {
			//indent the methods for readability
			System.out.print("\t");

			printModifiers(methods.get(i).getModifiers());
			System.out.print(methods.get(i).getReturnType().getName() + " ");
			System.out.print(methods.get(i).getName() + "(");
			Object[] paramTypes = methods.get(i).getParameterTypes();
			for (int j=0; j<paramTypes.length; j++) {
				System.out.print(paramTypes[j]);

				if (j < paramTypes.length-1) {
					System.out.print(", ");
				}
			}
			System.out.print(")\n");
		}
	}
	
	public void printConstructors(Object obj) {
		Class classObj = obj.getClass();
		Constructor[] ownConstructors = classObj.getConstructors();
		ArrayList<Constructor> constructors = new ArrayList<Constructor>();
		
		for (int i=0; i<ownConstructors.length; i++) {
			constructors.add(ownConstructors[i]);
		}

		addInterfaceConstructors(constructors, classObj);
		addSuperclassConstructors(constructors, classObj);
		
		for (int i=0; i< constructors.size(); i++) {
			//indent the methods for readability
			System.out.print("\t");

			printModifiers(constructors.get(i).getModifiers());
			
			System.out.print(constructors.get(i).getName() + "(");
			Object[] paramTypes = constructors.get(i).getParameterTypes();
			for (int j=0; j<paramTypes.length; j++) {
				System.out.print(paramTypes[j]);

				if (j < paramTypes.length-1) {
					System.out.print(", ");
				}
			}
			System.out.print(")\n");
		}
	}
	
	
}
