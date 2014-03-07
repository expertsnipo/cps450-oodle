package com.bju.cps450;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bju.cps450.analysis.DepthFirstAdapter;
import com.bju.cps450.node.AAssignStatementStatement;
import com.bju.cps450.node.ABooleanType;
import com.bju.cps450.node.AClassDefinition;
import com.bju.cps450.node.AClasses;
import com.bju.cps450.node.AFalseExpression;
import com.bju.cps450.node.AIdentifierExpression;
import com.bju.cps450.node.AIntType;
import com.bju.cps450.node.AIntegerExpression;
import com.bju.cps450.node.AMethod;
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
	
	private void initNode(Node node) {
		if(attributeGrammarMap.get(node) == null) {
			attributeGrammarMap.put(node, new HashMap<String, Object>());
		}		
	}

	@Override
	public void inAClasses(AClasses node) {
		// TODO Auto-generated method stub
		super.inAClasses(node);
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
		
		try {
			//make sure assignment types match
			//also make sure variable exists in the current scope			
			
			String name = node.getIdentifier().getText();
			Declaration decl = Application.symbolTable.lookup(name).getDeclaration();
			
			
			Type lhs = decl.getType();
			//Object obj = node.getExpression().
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
		
		Type t = Type.error;
		Type lhs = (Type)attributeGrammarMap.get(node.getLhs()).get("type");
		Type rhs = (Type)attributeGrammarMap.get(node.getRhs()).get("type");
		if(lhs.compareTo(rhs) == 0 && (lhs.compareTo(Type.integer) == 0/* || other types here */)) {
			t = Type.integer;
		} else {
			//print an error
			System.out.println("Subtract expression types not equal or not integer.");
		}

		attributeGrammarMap.get(node).put("type", t);
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
		String name = node.getIdentifier().getText();
		try {
			Declaration d = Application.symbolTable.lookup(name).getDeclaration();
			if(!(d instanceof VariableDeclaration)) {
				//print error
				attributeGrammarMap.get(node).put("type", Type.error);
			} else {
				attributeGrammarMap.get(node).put("type", d.getType());
			}
		} catch (Exception e) {
			//print error
			attributeGrammarMap.get(node).put("type", Type.error);
		}
	}

	@Override
	public void outAClasses(AClasses node) {
		// TODO Auto-generated method stub
		super.outAClasses(node);
	}

	@Override
	public void inAClassDefinition(AClassDefinition node) {
		super.inAClassDefinition(node);
		
		try {
			//make sure class isn't already defined and make sure begin and end ids match
			String name = node.getFirst().getText();
			if (!(name.equals(node.getLast().getText()))) {
				//ids aren't the same, semantics error
				System.out.println("Class: " + name + " does not have a matching end identifier.");
				return;
			} else {
				try {
					Declaration decl = Application.symbolTable.lookup(name).getDeclaration();
					if (decl instanceof ClassDeclaration) {
						// print an error
						System.out.println("Class: " + name + " has already been defined.");
						return;
					}
				} catch (Exception e) {
					// good exception				
				}
				
				// create classdeclaration
				ClassDeclaration decl = new ClassDeclaration(name, new Type(name));
				Application.symbolTable.push(name, decl);
				
				// start scope for new class
				Application.symbolTable.beginScope();
				
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
			Application.symbolTable.endScope();
		} catch (Exception e) {
			// ack
			System.out.println("Error creating class: " + e.getMessage());
			//e.printStackTrace();
		}
	}


	@Override
	public void outAVariable(AVariable node) {
		super.outAVariable(node);
		
		//make sure the variable does not yet exist
		String name = node.getIdentifier().getText();
		try {
			Declaration decl = Application.symbolTable.lookup(name).getDeclaration();
			if (decl instanceof VariableDeclaration) {
				// print error
				System.out.println("The variable " + name + "already exists in this scope.");
				return;
			}
		} catch (Exception e) {
			
		}
		
		// create a variable declaration
		Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
		VariableDeclaration decl = new VariableDeclaration(name, t);
		Application.symbolTable.push(name, decl);		
	}

	
	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outABooleanType(com.bju.cps450.node.ABooleanType)
	 */
	@Override
	public void outABooleanType(ABooleanType node) {
		super.outABooleanType(node);
		initNode(node);
		attributeGrammarMap.get(node).put("type", Type.bool);
	}

	@Override
	public void outAIntType(AIntType node) {
		super.outAIntType(node);
		initNode(node);
		attributeGrammarMap.get(node).put("type", Type.integer);
	}

	@Override
	public void inAMethod(AMethod node) {
		super.inAMethod(node);
		
		try {
			// make sure method does not yet exist
			String name = node.getFirst().getText();
			try {
				Declaration decl = Application.symbolTable.lookup(name).getDeclaration();
				if (decl instanceof MethodDeclaration) {
					// method declaration already exists
					return;
				}
			} catch (Exception e) {
				
			}
			
			// create a Method declaration
			MethodDeclaration decl = new MethodDeclaration();
			decl.setName(name);
			Application.symbolTable.push(name, decl);
			
			// start scope for the new method
			Application.symbolTable.beginScope();
		} catch (Exception e) {
			// error
			System.out.println("Method declaration error: " + e.getMessage());
		}
	}

	@Override
	public void outAMethod(AMethod node) {
		super.outAMethod(node);
		try {
			Application.symbolTable.endScope();
		} catch (Exception e) {
			// print a helpful here
			System.out.println("Problem with coming out of method scope: " + e.getMessage());
		}
	}

	@Override
	public void caseAMethod(AMethod node) {
		inAMethod(node);
		
		if (node.getType() != null) {
			node.getType().apply(this);
			// pull off type and add to method
			Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
			Application.symbolTable.getLastPushedMethod().setType(t);
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
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAStringType(com.bju.cps450.node.AStringType)
	 */
	@Override
	public void outAStringType(AStringType node) {
		super.outAStringType(node);
		initNode(node);		
		//string type is not supported, issue message
		System.out.println("Unsupported feature: string: " + node.toString() + "not supported.");
		
		attributeGrammarMap.get(node).put("type", Type.error);
	}

	/* (non-Javadoc)
	 * @see com.bju.cps450.analysis.DepthFirstAdapter#outAStringExpression(com.bju.cps450.node.AStringExpression)
	 */
	@Override
	public void outAStringExpression(AStringExpression node) {
		super.outAStringExpression(node);
		initNode(node);
		//string type is not supported, issue message
		System.out.println("Unsupported feature: string expression " + node.toString() + "not supported.");	
		
		attributeGrammarMap.get(node).put("type", Type.error);
	}
	
}
