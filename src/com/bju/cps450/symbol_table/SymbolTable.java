package com.bju.cps450.symbol_table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.bju.cps450.Application;
import com.bju.cps450.declarations.ClassDeclaration;
import com.bju.cps450.declarations.Declaration;
import com.bju.cps450.declarations.MethodDeclaration;
import com.bju.cps450.declarations.ParameterDeclaration;
import com.bju.cps450.declarations.VariableDeclaration;
import com.bju.cps450.types.Type;

public class SymbolTable {
	public class Symbol {
		private String name;
		private Declaration decl;
		
		public Symbol(String name, Declaration decl) {
			this.name = name;
			this.decl = decl;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Declaration getDeclaration() {
			return this.decl;
		}
	}
	
	private Stack<HashMap<String, Symbol>> symbolTable = new Stack<HashMap<String, Symbol>>();
	private Declaration lastPushedDeclaration;
	private ClassDeclaration lastPushedClass;
	private MethodDeclaration lastPushedMethod;
	private VariableDeclaration lastPushedVariable;
	
	private int scope = 0;
	public static int GLOBAL_SCOPE = 0;
	public static int CLASS_SCOPE = 1;
	public static int METHOD_SCOPE = 2;
	
	public SymbolTable() {
		// push global scope to symbol table
		symbolTable.push(new HashMap<String, Symbol>());
		
		Type reader = new Type("Reader");	
		Type writer = new Type("Writer");
		
		ClassDeclaration readerClass = new ClassDeclaration("Reader", reader);
		MethodDeclaration readerMethod = new MethodDeclaration("readInt", Type.integer);
		ClassDeclaration writerClass = new ClassDeclaration("Writer", writer);
		MethodDeclaration writerMethod = new MethodDeclaration("writeInt", Type.bool);
		
		writerMethod.addParameter(new ParameterDeclaration("num", Type.integer));
		
		readerClass.addMethod(readerMethod);
		writerClass.addMethod(writerMethod);
		
		push("Reader", readerClass);
		push("in", new VariableDeclaration("in", reader));
		
		
		push("Writer", new ClassDeclaration("Writer", writer));
		push("out", new VariableDeclaration("out", writer));
	}
	
	public Symbol push(String name, Declaration decl) {
		//create new symbol
		Symbol s = new Symbol(name, decl);
		//push on top of symbol stable
		symbolTable.peek().put(name, s);
		
		//keep track of last pushed
		if(decl instanceof ClassDeclaration) {
			this.lastPushedClass = (ClassDeclaration)decl;
		}
		if(decl instanceof MethodDeclaration) {
			this.lastPushedMethod = (MethodDeclaration)decl;
		}
		if(decl instanceof ClassDeclaration) {
			this.lastPushedClass = (ClassDeclaration)decl;
		}
		if(decl instanceof VariableDeclaration) {
			this.lastPushedVariable = (VariableDeclaration)decl;
		}
		this.lastPushedDeclaration = decl;
		
		return s;
	}
	
	public Symbol lookup(String name) throws Exception {
		for(int i = symbolTable.size() - 1; i >= 0; --i) {
			if(symbolTable.get(i).get(name) != null) {
				return symbolTable.get(i).get(name);
			}
		}
		//throw an exception if the symbol was not found
		throw new Exception(name + " was not found on symbol table");
	}
	
	public void beginScope() throws Exception {
		if(symbolTable.size() == 3) {
			throw new Exception("stack overflow");
		}
		++scope;
		symbolTable.push(new HashMap<String, Symbol>());
	}
	
	public void endScope() throws Exception {
		if(symbolTable.size() == 0) {
			throw new Exception("stack underflow");
		}
		
		--scope;
		HashMap<String, Symbol> current = symbolTable.pop();
		
		//merge down into parent
		Iterator iter = current.entrySet().iterator();
		if(scope == CLASS_SCOPE) { //if parent is method
			MethodDeclaration decl = getLastPushedMethod();
			
			while(iter.hasNext()) {
				Map.Entry<String, Symbol> entry = (Map.Entry<String, Symbol>)iter.next();
				
				if(entry.getValue().getDeclaration() instanceof VariableDeclaration) {
					decl.addVariable((VariableDeclaration)entry.getValue().getDeclaration());
				}
			}
		} else if (scope == GLOBAL_SCOPE) { //if parent is class
			ClassDeclaration decl = getLastPushedClass();
			
			while(iter.hasNext()) {
				Map.Entry<String, Symbol> entry = (Map.Entry<String, Symbol>)iter.next();
				
				if(entry.getValue().getDeclaration() instanceof VariableDeclaration) {
					decl.addVariable((VariableDeclaration)entry.getValue().getDeclaration());
				} else if (entry.getValue().getDeclaration() instanceof MethodDeclaration) {
					decl.addMethod((MethodDeclaration)entry.getValue().getDeclaration());
				}
			}
		}
	}
	
	public int getScope() {
		return this.scope;
	}
	
	public Declaration getLastPushed() {
		return this.lastPushedDeclaration;
	}
	
	public ClassDeclaration getLastPushedClass() {
		return this.lastPushedClass;
	}
	
	public MethodDeclaration getLastPushedMethod() {
		return this.lastPushedMethod;
	}
	
	public VariableDeclaration getLastPushedVariable() {
		return this.lastPushedVariable;
	}
}
