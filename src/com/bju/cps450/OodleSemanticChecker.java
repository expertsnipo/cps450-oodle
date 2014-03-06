package com.bju.cps450;

import java.util.HashMap;

import com.bju.cps450.analysis.DepthFirstAdapter;
import com.bju.cps450.node.AClassDefinition;
import com.bju.cps450.node.AClasses;
import com.bju.cps450.node.AMethod;
import com.bju.cps450.node.AVariable;
import com.bju.cps450.node.Node;
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
			System.out.println("An exception has occurred.");
			e.printStackTrace();
		}
	}

	@Override
	public void outAClassDefinition(AClassDefinition node) {
		super.outAClassDefinition(node);
		try {
			Application.symbolTable.endScope();
		} catch (Exception e) {
			// ack
			System.out.println("Error creating class");
			e.printStackTrace();
		}
	}

	@Override
	public void inAVariable(AVariable node) {
		// TODO Auto-generated method stub
		super.inAVariable(node);
	}

	@Override
	public void outAVariable(AVariable node) {
		// TODO Auto-generated method stub
		super.outAVariable(node);
	}

	@Override
	public void inAMethod(AMethod node) {
		// TODO Auto-generated method stub
		super.inAMethod(node);
	}

	@Override
	public void outAMethod(AMethod node) {
		// TODO Auto-generated method stub
		super.outAMethod(node);
	}

	@Override
	public void caseAMethod(AMethod node) {
		inAMethod(node);
		
		if (node.getType() != null) {
			node.getType().apply(this);
			// pull off type and add to method
			Type t = (Type)attributeGrammarMap.get(node.getType()).get("type");
		}
		
		
	}
	
}
