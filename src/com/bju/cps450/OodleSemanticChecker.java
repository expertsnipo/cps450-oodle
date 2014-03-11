package com.bju.cps450;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bju.cps450.analysis.DepthFirstAdapter;
import com.bju.cps450.node.AAddExpression;
import com.bju.cps450.node.AAndExpression;
import com.bju.cps450.node.AArgument;
import com.bju.cps450.node.AArrayExpression;
import com.bju.cps450.node.AArrayType;
import com.bju.cps450.node.AAssignStatementStatement;
import com.bju.cps450.node.ABooleanType;
import com.bju.cps450.node.ACallExpression;
import com.bju.cps450.node.ACallStatementStatement;
import com.bju.cps450.node.AClassDefinition;
import com.bju.cps450.node.AClasses;
import com.bju.cps450.node.AConcatExpression;
import com.bju.cps450.node.ADivideExpression;
import com.bju.cps450.node.AEqualsExpression;
import com.bju.cps450.node.AFalseExpression;
import com.bju.cps450.node.AGreaterExpression;
import com.bju.cps450.node.AGtEqualExpression;
import com.bju.cps450.node.AIdentifierExpression;
import com.bju.cps450.node.AIfStatementStatement;
import com.bju.cps450.node.AInheritsClass;
import com.bju.cps450.node.AIntType;
import com.bju.cps450.node.AIntegerExpression;
import com.bju.cps450.node.ALoopStatementStatement;
import com.bju.cps450.node.AMeExpression;
import com.bju.cps450.node.AMethod;
import com.bju.cps450.node.AMultExpression;
import com.bju.cps450.node.ANegExpression;
import com.bju.cps450.node.ANewExpression;
import com.bju.cps450.node.ANotExpression;
import com.bju.cps450.node.AOrExpression;
import com.bju.cps450.node.AParenExpression;
import com.bju.cps450.node.APosExpression;
import com.bju.cps450.node.AStringExpression;
import com.bju.cps450.node.AStringType;
import com.bju.cps450.node.ASubtractExpression;
import com.bju.cps450.node.ATrueExpression;
import com.bju.cps450.node.AVariable;
import com.bju.cps450.node.Node;
import com.bju.cps450.node.PArgument;
import com.bju.cps450.node.PStatement;
import com.bju.cps450.node.PVariable;
import com.bju.cps450.symbol_table.SymbolTable;
import com.bju.cps450.types.Type;
import com.bju.cps450.declarations.ClassDeclaration;
import com.bju.cps450.declarations.Declaration;
import com.bju.cps450.declarations.MethodDeclaration;
import com.bju.cps450.declarations.ParameterDeclaration;
import com.bju.cps450.declarations.VariableDeclaration;


public class OodleSemanticChecker extends DepthFirstAdapter {

	//private SymbolTable symbolTable = new SymbolTable();
	//symbolTable is declared in Application as globally accessible
	private HashMap<Node, HashMap<String, Object>> attributeGrammarMap = new HashMap<Node, HashMap<String, Object>>();
	private int lastLine;
	
	private void initNode(Node node) {
		if(attributeGrammarMap.get(node) == null) {
			attributeGrammarMap.put(node, new HashMap<String, Object>());
		}		
	}

	
	
	/* printError
	 * Arguments:
	 *   error : String - the error to print
	 * Purpose: print error messages to standard out
	 */
	private void printError(String error) {
		Application.getErrors().addSemanticErrors();
		System.out.println("At line " + lastLine + " - " + error);
	}
	
	private Type checkOperator(Type lhs, Type rhs, List<Type> expected, Type result, String operator) {
		Type ret = Type.error;
		if(lhs.compareTo(rhs) != 0) {
			printError(operator + " does not know how to operate on type " + lhs.getName() + " and type " + rhs.getName());
		} else {
			for(int i = 0; i < expected.size(); ++i) {
				if(lhs.compareTo(expected.get(i)) == 0) {
					if(result != null) {
						ret = result;
					} else {
						ret = lhs;
					}
				}
			}
			if(ret.compareTo(Type.error) == 0) {
				String errorMsg = operator + " expected type " + expected.get(0).getName();
				for(int i = 1; i < expected.size(); ++i) {
					errorMsg += " or " + expected.get(i).getName();
				}
				errorMsg += " but got type " + lhs.getName();
				printError(errorMsg);
			}
		}
		return ret;
	}

	/*@Override
	public void inAAssignStatementStatement(AAssignStatementStatement node) {
		super.inAAssignStatementStatement(node);
		
		try {
			//make sure assignment types match
			//also make sure variable exists in the current scope			
			
			String name = node.getIdentifier().getText();
			//Declaration decl = Application.symbolTable.lookup(name).getDeclaration();
			
			Type lhs = (Type)attributeGrammarMap.get(node.getIdentifier()).get("type");
			Type rhs = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
			if (lhs.compareTo(rhs) == 0) {
				//we're good to go
				
			} else {
				// semantic error
				System.out.println("Assignment statement error: " + name + ":" + lhs.toString() + " trying to assign " + rhs.toString());
				return;
			}
			
			
		} catch (Exception e) {
			// error
			System.out.println("Assignment error: " + e.getMessage());
		}
	}*/

	@Override
	public void outAAssignStatementStatement(AAssignStatementStatement node) {
		super.outAAssignStatementStatement(node);	
		initNode(node);
		VariableDeclaration variable = null;
		MethodDeclaration method = null;
		String name = node.getIdentifier().getText();
		
		try {
			//make sure assignment types match
			//also make sure variable exists in the current scope		
			
			Declaration decl = Application.getSymbolTable().lookup(name).getDeclaration();
			if (decl instanceof VariableDeclaration) {			
				variable = (VariableDeclaration)decl;
			} else if (decl instanceof MethodDeclaration) {
				method = (MethodDeclaration)decl;
				if (method != null) {
					Type t = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
					if (method.getType().compareTo(t) != 0) {
						printError("Variable " + name + " needs type " + method.getType().getName() + " but got type " + t.getName());
					}
				}
			} else {
				throw new Exception("No variable with name " + name + " exists in the current scope");
			}
		} catch (Exception e) {
			printError(e.getMessage());
		}
		if (variable != null) {
			Type t = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
			if (variable.getType().compareTo(t) != 0) {
				printError("Variable " + name + " needs type " + variable.getType().getName() + " but got type " + t.getName());
			}
		}			
			
			/*Type lhs = decl.getType();
			//Object obj = node.getExpression().
			Type rhs = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
			if (lhs.compareTo(rhs) == 0) {
				//we're good to go
				
			} else {
				// semantic error
				System.out.println("Assignment statement error: " + name + ":" + lhs.toString() + " trying to assign " + rhs.toString());
				return;
			}*/		
		
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outACallExpression(com.bju.cps450.node.ACallExpression)
	 */
	@Override
	public void outACallExpression(ACallExpression node) {
		super.outACallExpression(node);
		initNode(node);
		lastLine = node.getIdentifier().getLine();
		String name = node.getIdentifier().getText();
		ClassDeclaration parent = Application.getSymbolTable().getLastClassDeclaration();
		if(node.getDot() != null) { // if there is something before the dot use that class for lookups
			Type t = (Type)attributeGrammarMap.get(node.getDot()).get("type");
			try {
				parent = (ClassDeclaration)Application.getSymbolTable().lookup(t.getName(), 0).getDeclaration();
			} catch(Exception e) {
				printError("no class of name " + t.getName() + " was found in the current scope");
			}
		}
		MethodDeclaration method = null;
		try { //try to find method
			method = parent.lookupMethod(name);
		} catch(Exception e) {
			printError("no method of name " + node.getIdentifier().getText() + " was found in class " + parent.getType().getName());
		}
		if(method != null) { //if found, check parameters
			if(method.getParameters().size() != node.getExpression().size()) {
				printError("method " + method.getName() + " wanted " + method.getParameters().size() + " parameters but got " + node.getExpression().size() + " parameters");
			}
			for(int i = 0; i < method.getParameters().size() && i < node.getExpression().size(); ++i) {
				Type wanted = method.getParameters().get(i).getType();
				Type got = (Type)attributeGrammarMap.get(node.getExpression().get(i)).get("type");
				if(wanted.compareTo(got) != 0 && got.compareTo(Type.voidType) != 0) {
					printError("method " + method.getName() + "'s parameter " + i + " wanted type " + wanted.getName() + " but got " + got.getName());
				}
			}
			attributeGrammarMap.get(node).put("type", method.getType());
		} else {
			attributeGrammarMap.get(node).put("type", Type.error);
		}
		
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAOrExpression(com.bju.cps450.node.AOrExpression)
	 */
	@Override
	public void outAOrExpression(AOrExpression node) {
		super.outAOrExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.bool);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, Type.bool, "or"));
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAGreaterExpression(com.bju.cps450.node.AGreaterExpression)
	 */
	@Override
	public void outAGreaterExpression(AGreaterExpression node) {
		super.outAGreaterExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.integer);
		//expectedTypes.add(Type.string);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, Type.bool, "greater than"));
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAParenExpression(com.bju.cps450.node.AParenExpression)
	 */
	@Override
	public void outAParenExpression(AParenExpression node) {
		super.outAParenExpression(node);
		initNode(node);
		Type t = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
		attributeGrammarMap.get(node).put("type", t);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outATrueExpression(com.bju.cps450.node.ATrueExpression)
	 */
	@Override
	public void outATrueExpression(ATrueExpression node) {
		super.outATrueExpression(node);
		initNode(node);				
		attributeGrammarMap.get(node).put("type", Type.bool);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAFalseExpression(com.bju.cps450.node.AFalseExpression)
	 */
	@Override
	public void outAFalseExpression(AFalseExpression node) {
		super.outAFalseExpression(node);
		initNode(node);		
		attributeGrammarMap.get(node).put("type", Type.bool);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outASubtractExpression(com.bju.cps450.node.ASubtractExpression)
	 */
	@Override
	public void outASubtractExpression(ASubtractExpression node) {
		super.outASubtractExpression(node);
		initNode(node);
		
		
		Type lhs = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhs = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.integer);
		attributeGrammarMap.get(node).put("type", checkOperator(lhs, rhs, expectedTypes, Type.integer, "minus"));
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAIntegerExpression(com.bju.cps450.node.AIntegerExpression)
	 */
	@Override
	public void outAIntegerExpression(AIntegerExpression node) {
		super.outAIntegerExpression(node);
		initNode(node);		
		attributeGrammarMap.get(node).put("type", Type.integer);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAIdentifierExpression(com.bju.cps450.node.AIdentifierExpression)
	 */
	@Override
	public void outAIdentifierExpression(AIdentifierExpression node) {
		super.outAIdentifierExpression(node);
		initNode(node);
		VariableDeclaration variable = null;
		MethodDeclaration method = null;
		String name = node.getIdentifier().getText();
		lastLine = node.getIdentifier().getLine();
		try {
			Declaration decl = Application.getSymbolTable().lookup(name).getDeclaration();
			if(decl instanceof VariableDeclaration) {
				variable = (VariableDeclaration)decl;
			} else if (decl instanceof MethodDeclaration) {
				// it's a method definition
				method = (MethodDeclaration)decl;
				attributeGrammarMap.get(node).put("type", method.getType());
			} else {
				throw new Exception("No variable with name " + name + " exists in the current scope");
			}
		} catch (Exception e) {
			printError(e.getMessage());
		}
		if (variable != null) {
			attributeGrammarMap.get(node).put("type", variable.getType());
		} else {
			attributeGrammarMap.get(node).put("type", Type.error);
		}
		
	}

	

	@Override
	public void inAClassDefinition(AClassDefinition node) {
		super.inAClassDefinition(node);
		lastLine = node.getFirst().getLine();
		String name = node.getFirst().getText();
		try {
			//make sure class isn't already defined and make sure begin and end ids match			
			if (!(name.equals(node.getLast().getText()))) {
				//ids aren't the same, semantics error
				System.out.println("Class: " + name + " does not have a matching end identifier.");
				return;
			} else {
				try {
					Declaration decl = Application.getSymbolTable().lookup(name).getDeclaration();
					if (decl instanceof ClassDeclaration) {
						// print an error
						System.out.println("Class: " + name + " has already been defined.");
						return;
					}
				} catch (Exception e) {
					// good exception				
				}
				
				// create classdeclaration
				try {
				ClassDeclaration decl = new ClassDeclaration(name, new Type(name));
				Application.getSymbolTable().push(name, decl);
				} catch (Exception e) {
					printError(e.getMessage());
				}
				
				// start scope for new class
				try {
					Application.getSymbolTable().beginScope();
				} catch (Exception e) {
					printError(e.getMessage());
				}
				
				
			}			
		} catch (Exception e) { 
			// oh no
			System.out.println("An exception has occurred: " + e.getMessage());
			//e.printStackTrace();
		}
	}

	@Override
	public void outAClassDefinition(AClassDefinition node) {
		super.outAClassDefinition(node);
		try {
			Application.getSymbolTable().endScope();
		} catch (Exception e) {
			// ack
			printError(e.getMessage());
			//e.printStackTrace();
		}
	}


	@Override
	public void outAVariable(AVariable node) {
		super.outAVariable(node);
		lastLine = node.getIdentifier().getLine();
		Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
		//make sure the variable does not yet exist in current scope
		String name = node.getIdentifier().getText();
		try {
			VariableDeclaration decl = new VariableDeclaration(name, t);
			Application.getSymbolTable().push(name, decl);
			try {
				if (Application.getSymbolTable().getScope() == SymbolTable.CLASS_SCOPE) {
					Application.getSymbolTable().getLastClassDeclaration().addVariable(decl);
				} else if (Application.getSymbolTable().getScope() == SymbolTable.METHOD_SCOPE) {
					Application.getSymbolTable().getLastMethodDeclaration().addVariable(decl);
				}			
			} catch (Exception e) {
				printError(e.getMessage());
			}
		} catch (Exception e) {
			printError(e.getMessage());
		}
				
	}

	
	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outABooleanType(com.bju.cps450.node.ABooleanType)
	 */
	@Override
	public void outABooleanType(ABooleanType node) {
		super.outABooleanType(node);
		initNode(node);
		lastLine = node.getBoolean().getLine();
		attributeGrammarMap.get(node).put("type", Type.bool);
	}

	@Override
	public void outAIntType(AIntType node) {
		super.outAIntType(node);
		initNode(node);
		lastLine = node.getInt().getLine();
		attributeGrammarMap.get(node).put("type", Type.integer);
	}

	@Override
	public void inAMethod(AMethod node) {
		super.inAMethod(node);
		lastLine = node.getFirst().getLine();
		String name = node.getFirst().getText();
		try {
			// make sure method does not yet exist		
			MethodDeclaration methodDecl = new MethodDeclaration();
			methodDecl.setName(name);
			Application.getSymbolTable().push(name, methodDecl);
			Application.getSymbolTable().getLastClassDeclaration().addMethod(methodDecl);
			//Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
			//node.g
			//attributeGrammarMap.get(node).put("type", );
		} catch (Exception e) {
			printError(e.getMessage());
		}
		try {
			Application.getSymbolTable().beginScope();
		} catch (Exception e) {
			printError(e.getMessage());
		}
			
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAArrayType(com.bju.cps450.node.AArrayType)
	 */
	@Override
	public void outAArrayType(AArrayType node) {
		super.outAArrayType(node);
		initNode(node);
		//lastLine = node.getExpression().
		lastLine = 0;
		//string type is not supported, issue message
		String error = "Unsupported feature: array type - " + node.toString() + "not supported.";	
		printError(error);
		attributeGrammarMap.get(node).put("type", Type.error);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAAndExpression(com.bju.cps450.node.AAndExpression)
	 */
	@Override
	public void outAAndExpression(AAndExpression node) {
		// TODO Auto-generated method stub
		super.outAAndExpression(node);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAAddExpression(com.bju.cps450.node.AAddExpression)
	 */
	@Override
	public void outAAddExpression(AAddExpression node) {
		super.outAAddExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.integer);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, null, "plus"));
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAInheritsClass(com.bju.cps450.node.AInheritsClass)
	 */
	@Override
	public void outAInheritsClass(AInheritsClass node) {
		// TODO Auto-generated method stub
		super.outAInheritsClass(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAIfStatementStatement(com.bju.cps450.node.AIfStatementStatement)
	 */
	@Override
	public void outAIfStatementStatement(AIfStatementStatement node) {
		super.outAIfStatementStatement(node);
		Type t = (Type)attributeGrammarMap.get(node.getExpression()).get("type");
		if(t.compareTo(Type.bool) != 0) {
			printError("if statement expects condition to be type bool but was type " + t.getName());
		}
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outALoopStatementStatement(com.bju.cps450.node.ALoopStatementStatement)
	 */
	@Override
	public void outALoopStatementStatement(ALoopStatementStatement node) {
		// TODO Auto-generated method stub
		super.outALoopStatementStatement(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAEqualsExpression(com.bju.cps450.node.AEqualsExpression)
	 */
	@Override
	public void outAEqualsExpression(AEqualsExpression node) {
		super.outAEqualsExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.integer);
		expectedTypes.add(Type.string);
		expectedTypes.add(Type.bool);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, Type.bool, "equals"));
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAGtEqualExpression(com.bju.cps450.node.AGtEqualExpression)
	 */
	@Override
	public void outAGtEqualExpression(AGtEqualExpression node) {
		super.outAGtEqualExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		expectedTypes.add(Type.integer);
		//expectedTypes.add(Type.string);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, Type.bool, "greater than"));
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAConcatExpression(com.bju.cps450.node.AConcatExpression)
	 */
	@Override
	public void outAConcatExpression(AConcatExpression node) {
		super.outAConcatExpression(node);
		initNode(node);
		Type lhsType = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhsType = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		List<Type> expectedTypes = new ArrayList<Type>();
		//expectedTypes.add(Type.integer);
		expectedTypes.add(Type.string);
		attributeGrammarMap.get(node).put("type", checkOperator(lhsType, rhsType, expectedTypes, Type.string, "concatenation"));
		
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAMultExpression(com.bju.cps450.node.AMultExpression)
	 */
	@Override
	public void outAMultExpression(AMultExpression node) {
		// TODO Auto-generated method stub
		super.outAMultExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outADivideExpression(com.bju.cps450.node.ADivideExpression)
	 */
	@Override
	public void outADivideExpression(ADivideExpression node) {
		// TODO Auto-generated method stub
		super.outADivideExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAPosExpression(com.bju.cps450.node.APosExpression)
	 */
	@Override
	public void outAPosExpression(APosExpression node) {
		// TODO Auto-generated method stub
		super.outAPosExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outANegExpression(com.bju.cps450.node.ANegExpression)
	 */
	@Override
	public void outANegExpression(ANegExpression node) {
		// TODO Auto-generated method stub
		super.outANegExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outANotExpression(com.bju.cps450.node.ANotExpression)
	 */
	@Override
	public void outANotExpression(ANotExpression node) {
		// TODO Auto-generated method stub
		super.outANotExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAMeExpression(com.bju.cps450.node.AMeExpression)
	 */
	@Override
	public void outAMeExpression(AMeExpression node) {
		// TODO Auto-generated method stub
		super.outAMeExpression(node);
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outANewExpression(com.bju.cps450.node.ANewExpression)
	 */
	@Override
	public void outANewExpression(ANewExpression node) {
		// TODO Auto-generated method stub
		super.outANewExpression(node);
	}



	
	
	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAArrayExpression(com.bju.cps450.node.AArrayExpression)
	 */
	@Override
	public void outAArrayExpression(AArrayExpression node) {
		super.outAArrayExpression(node);
		initNode(node);
		lastLine = node.getIdentifier().getLine();
		String error = "Unsupported feature: array expression - " + node.toString() + "not supported.";	
		printError(error);
		attributeGrammarMap.get(node).put("type", Type.error);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAArgument(com.bju.cps450.node.AArgument)
	 */
	@Override
	public void outAArgument(AArgument node) {
		super.outAArgument(node);
		lastLine = node.getIdentifier().getLine();
		// find the parent method and create parameters to insert
		Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
		String name = node.getIdentifier().getText();
		ParameterDeclaration decl = new ParameterDeclaration(name, t);
		try { //add a parameter to the last method
			Application.getSymbolTable().getLastMethodDeclaration().addParameter(decl);
		} catch (Exception e) {
			printError(e.getMessage());
		}
		try { //psuedo treat a parameter as a variable so we can perform lookups
			VariableDeclaration variableDecl = new VariableDeclaration(name, t);
			Application.getSymbolTable().push(name, variableDecl);
		} catch (Exception e) {
			printError(e.getMessage());
		}
		
	}

	@Override
	public void outAMethod(AMethod node) {
		super.outAMethod(node);
		try {
			Application.getSymbolTable().endScope();
		} catch (Exception e) {
			// print a helpful here
			printError(e.getMessage());
		}
	}

	@Override
	public void caseAMethod(AMethod node) {
		inAMethod(node);
		
		if (node.getType() != null) {
			node.getType().apply(this);
			// pull off type and add to method
			Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
			Application.getSymbolTable().getLastMethodDeclaration().setType(t);
		} else {
			//method is void, ignore return values
			Application.getSymbolTable().getLastMethodDeclaration().setType(Type.voidType);
		}
		
		if (node.getFirst() != null) {
			node.getFirst().apply(this);
		}
		{
			List<PArgument> copy = new ArrayList<PArgument>(node.getArgument());
            for(PArgument e : copy)
            {
                e.apply(this);
            }			
		}
		{
			 List<PVariable> copy = new ArrayList<PVariable>(node.getVariable());
	            for(PVariable e : copy)
	            {
	                e.apply(this);
	            }			
		}
		{
			 List<PStatement> copy = new ArrayList<PStatement>(node.getStatement());
	            for(PStatement e : copy)
	            {
	                e.apply(this);
	            }			
		}
		outAMethod(node);		
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outACallStatementStatement(com.bju.cps450.node.ACallStatementStatement)
	 */
	@Override
	public void outACallStatementStatement(ACallStatementStatement node) {
		super.outACallStatementStatement(node);
		initNode(node);
		lastLine = node.getIdentifier().getLine();
		String name = node.getIdentifier().getText();
		ClassDeclaration parent = Application.getSymbolTable().getLastClassDeclaration();
		if(node.getDot() != null) { //if there is something before the dot, use that class instead
			Type t = (Type)attributeGrammarMap.get(node.getDot()).get("type");
			try {
				parent = (ClassDeclaration)Application.getSymbolTable().lookup(t.getName(), 0).getDeclaration();
			} catch(Exception e) {
				printError("no class of name " + t.getName() + " was found in the current scope");
			}
		}
		MethodDeclaration method = null;
		try {
			//find the method
			method = parent.lookupMethod(name);
		} catch (Exception e) {
			printError("No method of name " + node.getIdentifier().getText() + " was found in class " + parent.getType().getName());
		}
		if (method != null) {
			if(method.getParameters().size() != node.getExpression().size()) {
				printError("method " + method.getName() + " wanted " + method.getParameters().size() + " parameters but got " + node.getExpression().size() + " parameters");
			}
			for(int i = 0; i < method.getParameters().size() && i < node.getExpression().size(); ++i) {
				Type wanted = method.getParameters().get(i).getType();
				Type got = (Type)attributeGrammarMap.get(node.getExpression().get(i)).get("type");
				if(wanted.compareTo(got) != 0 && got.compareTo(Type.voidType) != 0) {
					printError("method " + method.getName() + "'s parameter " + i + " wanted type " + wanted.getName() + " but got " + got.getName());
				}
			}
			attributeGrammarMap.get(node).put("type", method.getType());
		} else {
			attributeGrammarMap.get(node).put("type", Type.error);
		}
	}



	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAStringType(com.bju.cps450.node.AStringType)
	 */
	@Override
	public void outAStringType(AStringType node) {
		super.outAStringType(node);
		initNode(node);	
		lastLine = node.getString().getLine();
		//string type is not supported, issue message
		String error = "Unsupported feature: string: " + node.toString() + "not supported.";
		printError(error);
		attributeGrammarMap.get(node).put("type", Type.string);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAStringExpression(com.bju.cps450.node.AStringExpression)
	 */
	@Override
	public void outAStringExpression(AStringExpression node) {
		super.outAStringExpression(node);
		initNode(node);
		lastLine = node.getStringLiteral().getLine();
		//string type is not supported, issue message
		String error = "Unsupported feature: string expression " + node.toString() + "not supported.";	
		printError(error);
		attributeGrammarMap.get(node).put("type", Type.string);
	}
	
}
