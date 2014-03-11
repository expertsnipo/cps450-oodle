package com.bju.cps450.types;

public class Type implements Comparable<Type> {
	public static Type error = new Type("Error"),
			                 integer = new Type("Int"),
			                 bool = new Type("Bool"),
			                 string = new Type("String"),
							 voidType = new Type("Void");
			
	private String name;		
	
	public Type(String name) {
		this.name = name;
	}
	/* getName
	 * Arguments:
	 * 
	 * Purpose: returns the name of the type
	 */
	public String getName() {
		return this.name;
	}

	/* compareTo
	 * Arguments:
	 *   arg0: Type - the type to compare the current type against
	 * Purpose: determines if two types are equal
	 */
	@Override
	public int compareTo(Type arg0) {
		return name.compareTo(arg0.getName());
	}
}
