package com.bju.cps450.declarations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bju.cps450.types.Type;

public class MethodDeclaration extends DeclarationsWithVariables {

	private ClassDeclaration parent;
	private HashMap<String, ParameterDeclaration> parameters = new HashMap<String, ParameterDeclaration>();
	
	public MethodDeclaration() {
		super();
		this.parent = null;
	}
	
	public MethodDeclaration(String name, Type t) {
		super(name, t);
		this.parent = null;
	}
	/* addVariable
	 * Arguments:
	 *   variable : VariableDeclaration - the name of the variable to be added
	 * Purpose: adds a variable to the current declaration, throws error if already declared
	 */
	@Override
	public void addVariable(VariableDeclaration variable) throws Exception{
		if(variables.get(variable.getName()) == null && parameters.get(variable.getName()) == null) {
			variables.put(variable.getName(), variable);
		} else if(variables.get(variable.getName()) != null) {
			throw new Exception("a variable with the name " + variable.getName() + " already exists in declaration");
		} else {
			throw new Exception("a parameter with name " + variable.getName() + " already exists in declaration");
		}
	}
	
	
	public void addParameter(ParameterDeclaration param) throws Exception {
		param.setOrder(parameters.size());
		if(parameters.get(param.getName()) == null) {
			parameters.put(param.getName(), param);
		} else {
			throw new Exception("a parameter with name " + param.getName() + " already exists in declaration");
		}
	}
	
	
	
	/* getParameters
	 * Arguments:
	 * 
	 * Purpose: returns a list of parameters for the current method
	 */
	public List<ParameterDeclaration> getParameters() {
		List<ParameterDeclaration> parametersList = new ArrayList<ParameterDeclaration>(parameters.size());		
		Iterator<Entry<String, ParameterDeclaration>> it = parameters.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, ParameterDeclaration> pair = (Map.Entry<String, ParameterDeclaration>)it.next();
			//parametersList.add(pair.getValue().getOrder(), pair.getValue());
			//the above causes indexing errors
			parametersList.add(pair.getValue());
		}
		Collections.sort(parametersList, new CustomComparator());
		return parametersList;
	}
	
	/* lookupInParent
	 * Arguments:
	 *   name : String - the name of the variable to be searched for
	 * Purpose: returns the variable if found, null otherwise
	 */
	@Override
	protected VariableDeclaration lookupInParent(String name) {
		if(parameters.get(name) == null) {
			try {
				return parent.lookupVariable(name);
			} catch(Exception e) { ; }
		} else {
			ParameterDeclaration parameter = parameters.get(name);
			return new VariableDeclaration(parameter.getName(), parameter.getType());
		}
		return null;
	}
	
}
