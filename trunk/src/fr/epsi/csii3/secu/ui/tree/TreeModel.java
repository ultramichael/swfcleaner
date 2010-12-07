package fr.epsi.csii3.secu.ui.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.epsi.csii3.secu.ui.generic.AbstractModel;

public class TreeModel extends AbstractModel {
	private Map<String, String> methods = new HashMap<String, String>();
	
	public Set<String> getMethodsNames() {
		return methods.keySet();
	}
	
	public String getMethodBody(String methodName) {
		return methods.get(methodName);
	}
	
	public void setMethods(Map<String, String> methods) {
		this.methods = methods;
		super.notifyChanges();
	}

}
