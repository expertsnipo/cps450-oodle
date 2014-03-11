package com.bju.cps450.declarations;

import java.util.Comparator;
import com.bju.cps450.declarations.ParameterDeclaration;

public class CustomComparator implements Comparator<ParameterDeclaration> {
	@Override
	public int compare(ParameterDeclaration decl1, ParameterDeclaration decl2) {		
		if (decl1.getOrder() < decl2.getOrder()) {
			return 0;
		} else {
			return 1;
		}
	}

}
