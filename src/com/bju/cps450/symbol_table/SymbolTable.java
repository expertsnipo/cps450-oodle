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
		private Declaration declaration;
		
		public Symbol(String name, Declaration declaration) {
			this.name = name;
			this.declaration = declaration;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Declaration getDeclaration() {
			return this.declaration;
		}
	}
	
	private Stack<HashMap<String, Symbol>> symbolTable;
	private ClassDeclaration lastClassDeclaration;
	private MethodDeclaration lastMethodDeclaration;
	private VariableDeclaration lastVariableDeclaration;
	
	private int currentScope;
	public static final int GLOBAL_SCOPE = 1;
	public static final int CLASS_SCOPE = 2;
	public static final int METHOD_SCOPE = 3;
	
	public SymbolTable() {
		// push global scope to symbol table
		currentScope = 1;
		symbolTable = new Stack<HashMap<String, Symbol>>();
		
		symbolTable.add(new HashMap<String, Symbol>());
		
		Type reader = new Type("Reader");	
		Type writer = new Type("Writer");
		
		ClassDeclaration readerClass = new ClassDeclaration("Reader", reader);
		MethodDeclaration readerMethod = new MethodDeclaration("readInt", Type.integer);
		ClassDeclaration writerClass = new ClassDeclaration("Writer", writer);
		MethodDeclaration writerMethod = new MethodDeclaration("writeInt", Type.voidType);
		
		try {
			writerMethod.addParameter(new ParameterDeclaration("num", Type.integer));
		} catch (Exception e1) { ; }
		try {
			writerMethod.addVariable(new VariableDeclaration("out", writer));
		} catch (Exception e1) { ; }
		
		try {
			readerClass.addMethod(readerMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			writerClass.addMethod(writerMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		symbolTable.peek().put("Writer", new Symbol("Writer", writerClass));
		symbolTable.peek().put("Reader", new Symbol("Reader", readerClass));
		
		symbolTable.peek().put("out", new Symbol("out", new VariableDeclaration("out", new Type("Writer"))));
		symbolTable.peek().put("in", new Symbol("in", new VariableDeclaration("in", new Type("Reader"))));
		
		/*push("Reader", readerClass);
		push("in", new VariableDeclaration("in", reader));
		
		
		push("Writer", new ClassDeclaration("Writer", writer));
		push("out", new VariableDeclaration("out", writer));
		*/
	}
	
	public Symbol push(String name, Declaration decl) throws Exception {
		//create new symbol
		Symbol s = new Symbol(name, decl);
		//push on top of symbol stable
		//symbolTable.peek().put(name, s);
		if (symbolTable.get(symbolTable.size() - 1).get(name) == null) {
			symbolTable.get(symbolTable.size() - 1).put(name, s);
		} else {
			throw new Exception("an object of the same type and same level with the same name already exists");
		}
		
		//keep track of last pushed
		if(decl instanceof ClassDeclaration) {
			this.lastClassDeclaration = (ClassDeclaration)decl;
		}
		if(decl instanceof MethodDeclaration) {
			this.lastMethodDeclaration = (MethodDeclaration)decl;
		}
		if(decl instanceof VariableDeclaration) {
			this.lastVariableDeclaration = (VariableDeclaration)decl;
		}
		
		return s;
	}
	
	public Symbol lookup(String name) throws Exception {
		return lookup(name, symbolTable.size() - 1);
	}
	
	public Symbol lookup(String name, int startAt) throws Exception {
		if (startAt < 0) {
			throw new Exception("startAt must be greater than 0");
		}
		for(int i = startAt; i >= 0; --i) {
			if(symbolTable.get(i).get(name) != null) {
				return symbolTable.get(i).get(name);
			}
		}
		//throw an exception if the symbol was not found
		throw new Exception(name + " was not found in symbol table");
	}
	
	public void beginScope() throws Exception {
		++currentScope;
		symbolTable.add(new HashMap<String, Symbol>());
		
		if (currentScope > METHOD_SCOPE) {
			throw new Exception("stack overflow");
		}
	}
	
	public void endScope() throws Exception {
		--currentScope;
		if(currentScope < GLOBAL_SCOPE) {
			throw new Exception("stack underflow");
		}
		symbolTable.remove(symbolTable.size() - 1);
		
		/*//merge down into parent
		Iterator iter = current.entrySet().iterator();
		if(scope == CLASS_SCOPE) { //if parent is method
			MethodDeclaration decl = getLastMethodDeclaration();
			
			while(iter.hasNext()) {
				Map.Entry<String, Symbol> entry = (Map.Entry<String, Symbol>)iter.next();
				
				if(entry.getValue().getDeclaration() instanceof VariableDeclaration) {
					decl.addVariable((VariableDeclaration)entry.getValue().getDeclaration());
				}
			}
		} else if (scope == GLOBAL_SCOPE) { //if parent is class
			ClassDeclaration decl = getLastClassDeclaration();
			
			while(iter.hasNext()) {
				Map.Entry<String, Symbol> entry = (Map.Entry<String, Symbol>)iter.next();
				
				if(entry.getValue().getDeclaration() instanceof VariableDeclaration) {
					decl.addVariable((VariableDeclaration)entry.getValue().getDeclaration());
				} else if (entry.getValue().getDeclaration() instanceof MethodDeclaration) {
					decl.addMethod((MethodDeclaration)entry.getValue().getDeclaration());
				}
			}
		}*/
	}
	
	public int getScope() {
		return currentScope;
	}
	
	public ClassDeclaration getLastClassDeclaration() {
		return this.lastClassDeclaration;
	}
	
	public MethodDeclaration getLastMethodDeclaration() {
		return this.lastMethodDeclaration;
	}
	
	public VariableDeclaration getLastVariableDeclaration() {
		return this.lastVariableDeclaration;
	}
}
