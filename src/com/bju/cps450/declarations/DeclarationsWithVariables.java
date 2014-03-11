package com.bju.cps450.declarations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bju.cps450.types.Type;

public abstract class DeclarationsWithVariables extends Declaration {
	protected abstract VariableDeclaration lookupInParent(String name);
	
	protected HashMap<String, VariableDeclaration> variables = new HashMap<String, VariableDeclaration>();
	
	protected DeclarationsWithVariables() {
		super();
	}
	
	protected DeclarationsWithVariables(String name, Type t) {
		super(name, t);
	}
	
	/* addVariable
	 * Arguments:
	 *   variable : VariableDeclaration - the name of the variable to be added
	 * Purpose: adds a variable to the current declaration, throws error if already declared
	 */
	public void addVariable(VariableDeclaration variable) throws Exception {
		if(variables.get(variable.getName()) == null) {
			variables.put(variable.getName(), variable);
		} else {
			throw new Exception("a variable with the name " + variable.getName() + " already exists in declaration");
		}
	}
	
	public List<VariableDeclaration> getVariables() {
		List<VariableDeclaration> variablesList = new ArrayList<VariableDeclaration>();
		Iterator<Entry<String, VariableDeclaration>> it = variables.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, VariableDeclaration> pair = (Map.Entry<String, VariableDeclaration>)it.next();
			variablesList.add(pair.getValue());
		}
		return variablesList;
	}
	
	public VariableDeclaration lookupVariable(String name) throws Exception {
		if(variables.get(name) == null) {
			VariableDeclaration variable = lookupInParent(name);
			if(variable == null) {
				throw new Exception("no variable with name " + name + " was found");
			} else {
				return variable;
			}
		} else {
			return variables.get(name);
		}
	}
}
