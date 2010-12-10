package fr.epsi.csii3.secu.business.flux;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private List<Node> sons;
	private String line;
	private int offset;
	
	public Node(String line, int offset) {
		this.sons = new ArrayList<Node>();
		this.line = line;
		this.offset = offset;
	}

	public void addSon(Node son) {
		this.sons.add(son);
	}

	
	private void simplify() {
		if(sons.size()>1) {
			if(line.contains("iffalse")) {
				sons.remove(1);
			}
			else if(line.contains("iftrue"))
				sons.remove(0);

			if(sons.size()<=1)
				line = "";
		}
		else if(sons.size()==1) {
			//sons.get(0).simplify();
		}
		else {
			// Done
		}
		
		for(Node s : sons)
			s.simplify();
	}
	
	public Node toSyntheticTree() {
		this.simplify();
		syntheticRoot = new Node(this.line, this.offset);
		syntheticNodes = new ArrayList<Node>();
		syntheticNodes.add(syntheticRoot);
		this.buildSynthetic(null);
		return syntheticRoot;
	}
	
	private Node buildSynthetic(Node stop) {
		if(this.sons.size()==0) {
			// Nothing to do
			return null;
		}
		else if(this.sons.size()==1) {
			Node son = this.sons.get(0);
			if(!son.equals(stop)) {
				addSonTo(this, son);
				son.buildSynthetic(stop);
				// TODO : null ?
				return this;
			}
			else {
				return this;
			}
		}
		else {
			Node cv = findConvergence();
			List<Node> lasts = new ArrayList<Node>();
			for(Node son : this.sons) {
				addSonTo(this, son);
				if(!son.equals(cv))
					lasts.add(son.buildSynthetic(cv));
			}
			for(Node l : lasts) {
				addSonTo(l, cv);
			}
			return cv;
		}
	}

	private static Node syntheticRoot;
	private static List<Node> syntheticNodes;
	private static void addSonTo(Node n, Node s) {
		Node newSon = new Node(s.line, s.offset);
		syntheticNodes.add(newSon);
		for(Node node : syntheticNodes) {
			if(n.equals(node)) {
				node.addSon(newSon);
				return;
			}
		}
		Node newFather = new Node(n.line, n.offset);
		syntheticNodes.add(newFather);
		newFather.addSon(newSon);
	}
	
	
	
	private Node findConvergence() {
		if(this.sons.size()==0)
			return null;
		else if(this.sons.size()==1) {
			this.sons.get(0).findConvergence();
			return null;
		}
		else {
			List<Node> browsedSonsOne = new ArrayList<Node>();
			List<Node> browsedSonsTwo = new ArrayList<Node>();
			Node oneSon = this.sons.get(0);
			Node twoSon = this.sons.get(1);
			while(!browsedSonsOne.contains(twoSon) && !browsedSonsTwo.contains(oneSon)) {
				if(oneSon.sons.size()>1)
					oneSon = oneSon.findConvergence();
				else if(oneSon.sons.size()==1)
					oneSon = oneSon.sons.get(0);
				if(twoSon.sons.size()>1)
					twoSon = twoSon.findConvergence();
				else if(twoSon.sons.size()==1)
					twoSon = twoSon.sons.get(0);
				browsedSonsOne.add(oneSon);
				browsedSonsTwo.add(twoSon);
			}
			if(browsedSonsOne.contains(twoSon))
				return twoSon;
			else
				return oneSon;
		}
	}

	
	@Override
	public String toString() {
		String newLine = System.getProperty("line.separator");
		String ret = new String(line+newLine);
		for(Node s : sons) {
			ret += s.toString()+newLine;
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object n) {
		return (n instanceof Node) && this.line.equals(((Node)n).line) && this.offset == ((Node)n).offset ;
	}
	
	
	
	
	private final static String[] branchOperators = new String[] {"ifnlt", "ifnle", "ifngt", "ifnge", "iftrue", "iffalse", "ifeq", "ifne", "iflt", "ifle", "ifgt", "ifge", "ifstricteq", "ifstrictne"};
	private final static String[] jumpOperators = new String[] {"jump"};
	private final static String[] returnOperators = new String[] {"return"};

	
	public static Node createFromAbc(String[] lines) {
		Node realRoot = new Node("Begin", -1);
		buildFromAbc(lines, 0, realRoot, new ArrayList<Integer>());
		return realRoot;
	}
	
	public static void buildFromAbc(String[] lines, int initialOffset, Node currentRoot, List<Integer> branchesEncountered) {
		Node son = new Node(lines[initialOffset], initialOffset);
		currentRoot.addSon(son);
		if(initialOffset >= lines.length || isOperator(lines[initialOffset], returnOperators)) {
			// finished
			//System.out.println("Done");
		}
		else {
			List<Integer> nextOffsets = getNextOffsets(lines, initialOffset);
			if(nextOffsets.size()>1 && !branchesEncountered.contains(initialOffset)) {
				branchesEncountered.add(initialOffset);
				List<Integer> newBranchesEncountered = new ArrayList<Integer>();
				for(Integer i : branchesEncountered) {
					newBranchesEncountered.add(new Integer(i));
				}
				int oneWayOffset = nextOffsets.get(0);
				int twoWayOffset = nextOffsets.get(1);	
				buildFromAbc(lines, oneWayOffset, son, branchesEncountered);
				buildFromAbc(lines, twoWayOffset, son, newBranchesEncountered);
			}
			else {
				// Fucking loop already visited or direct path
				int nextOffset = nextOffsets.get(0);
				buildFromAbc(lines, nextOffset, son, branchesEncountered);
			}
		}
	}
	
	private static List<Integer> getNextOffsets(String[] lines, int currentOffset) {
		List<Integer> nextOffsets = new ArrayList<Integer>();
		String line = lines[currentOffset].trim();
		
		boolean isJumpOperator = isOperator(line, jumpOperators);
		boolean isBranchOperator = isOperator(line, branchOperators);
		if(isJumpOperator) {
			nextOffsets.add(getLabelOffset(lines, line.substring(line.length()-3, line.length()).trim()));
		}
		else if(isBranchOperator) {
			String labelToJump = line.substring(line.length()-3, line.length()).trim();
			int currentOneOffset = currentOffset+1;
			int currentTwoOffset = getLabelOffset(lines, labelToJump);
			nextOffsets.add(currentOneOffset);
			nextOffsets.add(currentTwoOffset);
		}
		else
			nextOffsets.add(currentOffset+1);

		
		return nextOffsets;
	}
	
	private static int getLabelOffset(String[] lines, String label) {
		//System.out.println("Label : " + label);
		for(int i = 0;i<lines.length;i++)
			if(lines[i].trim().startsWith(label))
				return i;
		return -1;
	}
	
	private static boolean isOperator(String line, String[] operators) {
		for(String op : operators) {
			if(line.contains(op)) {
				return true;
			}
		}
		return false;
	}
}
