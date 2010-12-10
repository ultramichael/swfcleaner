package fr.epsi.csii3.secu.business.flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adobe.abc.GlobalOptimizer;

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
		//System.out.println("Done : "+syntheticRoot.sons.get(0).sons.size());
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
				//this.addSon(son);
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
				//this.addSon(son);
				addSonTo(this, son);
				if(!son.equals(cv))
					lasts.add(son.buildSynthetic(cv));
			}
			for(Node l : lasts) {
				//l.addSon(cv);
				addSonTo(l, cv);
			}
			return cv;
		}
	}
	
	/*private void recursiveSyntheticTree(Node syntheticTreeRoot, List<Node> path) {
		System.out.println(this.sons.size());
		if(this.sons.size()>1) {
			//Node convergenceNode = this.findConvergence();
			Node curOne = this.sons.get(0), curTwo = this.sons.get(1);
			Node syntheticCurOne = new Node(curOne.line, curOne.offset);
			Node syntheticCurTwo = new Node(curTwo.line, curTwo.offset);
			syntheticTreeRoot.addSon(syntheticCurOne);
			syntheticTreeRoot.addSon(syntheticCurTwo);
			
			List<Node> newPath = new ArrayList<Node>();
			this.sons.get(0).recursiveSyntheticTree(syntheticCurOne, newPath);
			this.sons.get(1).recursiveSyntheticTree(syntheticCurTwo, newPath);

			//System.out.println(offset+" CONVERGES TO : "+convergenceNode.offset);
		}
		else if(this.sons.size() == 1) {
			Node syntheticSon = new Node(this.sons.get(0).line, this.sons.get(0).offset);
			if(path != null) {
				if(!path.contains(syntheticSon))
					path.add(syntheticSon);
				else {
					for(Node p : path) {
						if(p.equals(syntheticSon)) {
							syntheticSon = p;
							break;
						}
					}
				}
			}
			syntheticTreeRoot.addSon(syntheticSon);
			this.sons.get(0).recursiveSyntheticTree(syntheticSon, path);
			//syntheticTreeRoot = son;
		}
		else {
			// Done
		}
		
		//for(Node s : sons)
		//	s.recursiveSyntheticTree(syntheticTreeRoot, path);
	}*/
	

	private static Node syntheticRoot;
	private static List<Node> syntheticNodes;
	private static void addSonTo(Node n, Node s) {
		//System.out.println("Add son "+s.line+" to "+n.line+" ("+syntheticRoot.line+")");
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
				//System.out.println(oneSon.offset+"("+oneSon.sons.size()+"),"+twoSon.offset+"("+twoSon.sons.size()+")");
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
	
	/*private List<Integer> getNextOffsets() {
		List<Integer> nextOffsets = new ArrayList<Integer>();
		
		boolean isJumpOperator = isOperator(line, jumpOperators);
		boolean isBranchOperator = isOperator(line, branchOperators);
		if(isJumpOperator) {
			nextOffsets.add(getLabelOffset(line.substring(line.length()-3, line.length()).trim()));
		}
		else if(isBranchOperator) {
			String labelToJump = line.substring(line.length()-3, line.length()).trim();
			int currentOneOffset = offset+1;
			int currentTwoOffset = getLabelOffset(labelToJump);
			nextOffsets.add(currentOneOffset);
			nextOffsets.add(currentTwoOffset);
		}
		else
			nextOffsets.add(offset+1);

		
		return nextOffsets;
	}*/
	
	private int getLabelOffset(String labelToJump) {
		List<Node> allSons = new ArrayList<Node>();
		this.getNodeSet(allSons);
		for(Node n : allSons)
			if(n.line.equals(labelToJump))
				return n.offset;
		return -1;
	}
	
	private void getNodeSet(List<Node> buffer) {
		for(Node s : this.sons) {
			buffer.add(s);
			s.getNodeSet(buffer);
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
	//private final static String[] labelOperators = new String[] {"L"};
	
	private static List<Node> labelNodes = new ArrayList<Node>();
	
	
	public static Node createFromAbc(String[] lines) {
		Node realRoot = new Node("Begin", -1);
		buildFromAbc(lines, 0, realRoot);
		return realRoot;
	}
	
	public static void buildFromAbc(String[] lines, int initialOffset, Node currentRoot) {
		Node son = new Node(lines[initialOffset], initialOffset);
		currentRoot.addSon(son);
		if(initialOffset >= lines.length || isOperator(lines[initialOffset], returnOperators)) {
			// finished
			//System.out.println("Done");
		}
		else {
			List<Integer> nextOffsets = getNextOffsets(lines, initialOffset);
			if(nextOffsets.size()>1) {
				int oneWayOffset = nextOffsets.get(0);
				int twoWayOffset = nextOffsets.get(1);
				/*Node oneSon = new Node(lines[oneWayOffset]);
				Node twoSon = new Node(lines[twoWayOffset]);
				currentRoot.addSon(oneSon);
				currentRoot.addSon(twoSon);*/

				buildFromAbc(lines, oneWayOffset, son);
				buildFromAbc(lines, twoWayOffset, son);

			}
			else {
				int nextOffset = nextOffsets.get(0);
				buildFromAbc(lines, nextOffset, son);
			}
		}
	}
	
	
	/*public static Node createFromAbc(String[] lines, int initialOffset, int stopOffset) {
		Node realRoot = new Node((initialOffset == 0 ? "Begin" : lines[initialOffset]));
		Node root = realRoot;
		
		int offset = initialOffset;
		boolean isReturnOperator = false;
		while(!isReturnOperator) {
			String line = lines[offset];
			line = line.trim();
			
			// Stops if return or offset==stopoffset
			isReturnOperator = isOperator(line, returnOperators) || (offset == stopOffset);
			boolean isLabel = isOperator(line, labelOperators);
			
			Node current = !isLabel ? new Node(line) : getLabelNode(line);
			List<Integer> nextOffsets = getNextOffsets(lines, offset);
			
			// Branching point
			if(nextOffsets.size()>1) {
				System.out.println("Branch : "+line);
				root.addSon(current);
				root = current;
				
				int currentOneOffset = nextOffsets.get(0);
				int currentTwoOffset = nextOffsets.get(1);
				int convergenceOffset = findConvergence(lines, currentOneOffset, currentTwoOffset);
				
				Node oneWay = new Node(lines[currentOneOffset]);
				Node twoWay = new Node(lines[currentTwoOffset]);
				
				// TODO : construction
				oneWay = createFromAbc(lines, currentOneOffset, convergenceOffset);
				twoWay = createFromAbc(lines, currentTwoOffset, convergenceOffset);
				
				root.addSon(oneWay);
				root.addSon(twoWay);
				
				root = getLabelNode(lines[convergenceOffset]);

				// Because convergenceOffset is a Label. So next is obviously convergenceOffset+1
				offset = getNextOffsets(lines, convergenceOffset).get(0);
			}
			else {
				offset = nextOffsets.get(0);
				root.addSon(current);
				root = current;
			}
		}
		
		return realRoot;
	}*/
	
	/*private static int findConvergence(String[] lines, int currentOneOffset, int currentTwoOffset) {
		System.out.println("Convergence between "+currentOneOffset+" and "+currentTwoOffset);
		List<Integer> browsedOffsetsOne = new ArrayList<Integer>();
		List<Integer> browsedOffsetsTwo = new ArrayList<Integer>();
		while(!browsedOffsetsOne.contains(currentTwoOffset) && !browsedOffsetsTwo.contains(currentOneOffset)) {
			List<Integer> nextOffsetsOne = getNextOffsets(lines, currentOneOffset);
			List<Integer> nextOffsetsTwo = getNextOffsets(lines, currentTwoOffset);
			int nextOffsetOne = 0, nextOffsetTwo = 0;
			if(nextOffsetsOne.size()>1)
				nextOffsetOne = findConvergence(lines, nextOffsetsOne.get(0), nextOffsetsOne.get(1));
			else
				nextOffsetOne = nextOffsetsOne.get(0);
			if(nextOffsetsTwo.size()>1)
				nextOffsetTwo = findConvergence(lines, nextOffsetsTwo.get(0), nextOffsetsTwo.get(1));
			else
				nextOffsetTwo = nextOffsetsTwo.get(0);
			browsedOffsetsOne.add(currentOneOffset);
			browsedOffsetsTwo.add(currentTwoOffset);
			currentOneOffset = nextOffsetOne;
			currentTwoOffset = nextOffsetTwo;
			
		}
		System.out.println("Results : "+currentOneOffset+","+currentTwoOffset);
		if(browsedOffsetsOne.contains(currentTwoOffset))
			return currentTwoOffset;
		else
			return currentOneOffset;
	}*/
	
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
	
	/*private static Node getLabelNode(String labelNode) {
		Node potential = new Node(labelNode);
	
		for(Node n : labelNodes)
			if(n.equals(potential))
				return n;
		
		labelNodes.add(potential);
		return potential;
	}*/

}
