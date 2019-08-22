import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.Node;

public class CongressTree {

	private static int maxTreeDepth = 0;
	private static ArrayList<ArrayList<Boolean>> congressMembers = new ArrayList<ArrayList<Boolean>>();
	private static ArrayList<String> voteLabels = new ArrayList<String>();

	private static String classification1;
	private static String classification0;
	private static Random rand = new Random();

	public static void main(String[] args) throws IOException { 
		maxTreeDepth = Integer.parseInt(args[1]);
		File congressionalVotes = new File(args[0]);
		BufferedReader bufferReader = new BufferedReader(new FileReader(congressionalVotes));

		ArrayList<ArrayList<String>> answers = new ArrayList<>();
		ArrayList<String> yesOrNo;

		boolean needFirst = true;
		boolean needSecond = true;

		String st;
		boolean firstRow = true;

		while ((st = bufferReader.readLine()) != null) {
			if(firstRow) {
				voteLabels = new ArrayList<> (Arrays.asList(st.split(",")));
				voteLabels.remove(voteLabels.size()-1);
				firstRow = false;
				continue;
			}
			yesOrNo = new ArrayList<> (Arrays.asList(st.split(",")));
			answers.add(yesOrNo);
		}

		for(ArrayList<String> votes: answers) {
			boolean[] yesOrNoBool = new boolean[votes.size()];
			for(int i = 0; i < votes.size(); i++) {
				int choice = rand.nextInt(2);
				yesOrNoBool[i] = true;

				if(votes.get(i).equals("Yea")) {
					yesOrNoBool[i] = true;
				}
				else if(votes.get(i).equals("Nay")) {
					yesOrNoBool[i] = false;
				}
				else
				{	
					if(choice == 0) {
						yesOrNoBool[i] = true;
					}
					else if(choice == 1) {
						yesOrNoBool[i] = false;
					}
				}

				//make this binary and find the 2 different classes instead of hardcoding
				if(needFirst) {
					classification1 = votes.get(votes.size()-1);
					needFirst = false;
				}
				if(needSecond && !(votes.get(votes.size()-1).equals(classification1))) {
					classification0 = votes.get(votes.size()-1);
					needSecond = false;
				}

				if (votes.get(votes.size()-1).equals(classification1)) {
					yesOrNoBool[yesOrNoBool.length-1] = true;
				}else if(needSecond  || votes.get(votes.size()-1).equals(classification0)) {
					yesOrNoBool[yesOrNoBool.length-1] = false;
				}
			}

			Boolean[] yon = new Boolean[yesOrNoBool.length];
			for(int i = 0; i<yon.length; i++) {
				yon[i] = (Boolean)yesOrNoBool[i];
			}

			if(yon != null) {
				congressMembers.add(new ArrayList<Boolean>(Arrays.asList(yon)));
			}
		}

		ArrayList<ArrayList<Boolean>> part1 = new ArrayList<ArrayList<Boolean>> (congressMembers.subList(0, congressMembers.size()/5));
		ArrayList<ArrayList<Boolean>> part2 = new ArrayList<ArrayList<Boolean>> (congressMembers.subList(congressMembers.size()/5, congressMembers.size()/5*2));
		ArrayList<ArrayList<Boolean>> part3 = new ArrayList<ArrayList<Boolean>> (congressMembers.subList(congressMembers.size()/5*2, congressMembers.size()/5*3));
		ArrayList<ArrayList<Boolean>> part4 = new ArrayList<ArrayList<Boolean>> (congressMembers.subList(congressMembers.size()/5*3, congressMembers.size()/5*4));
		ArrayList<ArrayList<Boolean>> part5 = new ArrayList<ArrayList<Boolean>> (congressMembers.subList(congressMembers.size()/5*4, congressMembers.size()));

		ArrayList<ArrayList<Boolean>> trainingData1 = new ArrayList<ArrayList<Boolean>>();
		trainingData1.addAll(part1);
		trainingData1.addAll(part2);
		trainingData1.addAll(part3);
		trainingData1.addAll(part4);
		DecisionTree<String> decisionTree1 = decisionTreeLearning(trainingData1, voteLabels, null);
		maxTreeDepth = Integer.parseInt(args[1]);
		decisionTree1.output(0);
		System.out.println();
		//test with part 5

		ArrayList<ArrayList<Boolean>> trainingData2 = new ArrayList<ArrayList<Boolean>>();
		trainingData2.addAll(part2);
		trainingData2.addAll(part3);
		trainingData2.addAll(part4);
		trainingData2.addAll(part5);
		DecisionTree<String> decisionTree2 = decisionTreeLearning(trainingData2, voteLabels, null);
		maxTreeDepth = Integer.parseInt(args[1]);
		decisionTree2.output(0);
		System.out.println();
		//test with part 1

		ArrayList<ArrayList<Boolean>> trainingData3 = new ArrayList<ArrayList<Boolean>>();
		trainingData3.addAll(part3);
		trainingData3.addAll(part4);
		trainingData3.addAll(part5);
		trainingData3.addAll(part1);
		DecisionTree<String> decisionTree3 = decisionTreeLearning(trainingData3, voteLabels, null);
		maxTreeDepth = Integer.parseInt(args[1]);
		decisionTree3.output(0);
		System.out.println();
		//test with part 2

		ArrayList<ArrayList<Boolean>> trainingData4 = new ArrayList<ArrayList<Boolean>>();
		trainingData4.addAll(part4);
		trainingData4.addAll(part5);
		trainingData4.addAll(part1);
		trainingData4.addAll(part2);
		DecisionTree<String> decisionTree4 = decisionTreeLearning(trainingData4, voteLabels, null);
		maxTreeDepth = Integer.parseInt(args[1]);
		decisionTree4.output(0);
		System.out.println();
		//test with part 3

		ArrayList<ArrayList<Boolean>> trainingData5 = new ArrayList<ArrayList<Boolean>>();
		trainingData5.addAll(part5);
		trainingData5.addAll(part1);
		trainingData5.addAll(part2);
		trainingData5.addAll(part3);
		DecisionTree<String> decisionTree5 = decisionTreeLearning(trainingData5, voteLabels, null);
		maxTreeDepth = Integer.parseInt(args[1]);
		decisionTree5.output(0);
		System.out.println();
		//test with part 4

		double test1 = tester(part5, decisionTree1);
		double test2 = tester(part1, decisionTree2);
		double test3 = tester(part2, decisionTree3);
		double test4 = tester(part3, decisionTree4);
		double test5 = tester(part4, decisionTree5);
		double average = (test1 + test2 + test3 + test4 + test5)/5;
		System.out.print(String.format("%.2f", test1) + " " + String.format("%.2f", test2) + " " + String.format("%.2f", test3) + " " + String.format("%.2f", test4) + " " + String.format("%.2f", test5) + " " + String.format("%.2f", average));
		System.out.println();

	}

	//function DECISION-TREE-LEARNING(examples, attributes, parent_examples) returns a tree
	private static DecisionTree<String> decisionTreeLearning(ArrayList<ArrayList<Boolean>> examples, ArrayList<String> attributes, ArrayList<ArrayList<Boolean>> parentExamples) {
		DecisionTree<String> decisionTree;

		//if examples is empty then return PLURALITY-VALUE(parent examples)
		if(examples.isEmpty() || examples == null || maxTreeDepth == 0) {
			return new DecisionTree<String>(pluralityValue(parentExamples));
		}
		//else if all examples have the same classification then return the classification
		else if(sameClassification(examples)) {
			return new DecisionTree<String>(pluralityValue(parentExamples));
		}
		//else if attributes is empty then return PLURALITY-VALUE(examples)
		else if(attributes.isEmpty() || attributes == null) {
			return new DecisionTree<String>(pluralityValue(parentExamples));
		}
		//else
		else {
			maxTreeDepth--;
			//A <-- argmaxa (of every) attributes IMPORTANCE(a, examples)
			String splitAttribute = importance(attributes, examples);
			//tree <-- a new decision tree with root test A
			decisionTree = new DecisionTree<String>(splitAttribute);
			for(int i = 0; i < voteLabels.size(); i++) {
				if(voteLabels.get(i).equals(splitAttribute)) {
					int labelIndex = i;
					ArrayList<ArrayList<Boolean>> trueBranch = new ArrayList<ArrayList<Boolean>>();
					ArrayList<ArrayList<Boolean>> falseBranch = new ArrayList<ArrayList<Boolean>>();
					//for each value vk of A do
					for(ArrayList<Boolean> exs : examples) {
						//exs <-- {e : e(of every)examples and e.A = vk}
						if(exs.get(labelIndex) == true) {
							trueBranch.add(exs);
						}
						else if(exs.get(labelIndex) == false) {
							falseBranch.add(exs);
						}
					}
					//subtree <-- DECISION-TREE-LEARNING(exs, attributes - A, examples) 
					attributes.remove(splitAttribute);
					DecisionTree<String> yesTreeBranch = decisionTreeLearning(trueBranch, attributes, examples);
					DecisionTree<String> noTreeBranch = decisionTreeLearning(falseBranch, attributes, examples);
					//add a branch to tree with label (A = vk) and subtree subtree
					decisionTree.setLeftChild(yesTreeBranch);
					decisionTree.setRightChild(noTreeBranch);
				}
			}
		}
		//return tree
		return decisionTree;
	}

	/**
	 * Cycle through the attributes and store that which has the biggest 
	 * information gain (pass in parent dataset and the child dataset 
	 * (that which was split based on attribute a)) --> entropy
	 * @param attributes are the still attributes this tree branch hasn't split on yet
	 * @param examples are a subset of the dataset
	 * @return the best attribute to split on in this case
	 */
	private static String importance(ArrayList<String> attributes, ArrayList<ArrayList<Boolean>> examples) {
		double bestInfoGain = 0;
		String splitAttribute = null;
		for(String label : attributes) {
			for(int i = 0; i < voteLabels.size(); i++) {
				if(voteLabels.get(i).equals(label)) {
					int labelIndex = i;
					ArrayList<ArrayList<Boolean>> trueBranch = new ArrayList<ArrayList<Boolean>>();
					ArrayList<ArrayList<Boolean>> falseBranch = new ArrayList<ArrayList<Boolean>>();
					//for each value vk of A do
					for(ArrayList<Boolean> exs : examples) {
						//exs <-- {e : e(of every)examples and e.A = vk}
						if(exs.get(labelIndex) == true) {
							trueBranch.add(exs);
						}
						else if(exs.get(labelIndex) == false) {
							falseBranch.add(exs);
						}
					}
					if(!trueBranch.isEmpty() || !falseBranch.isEmpty()) {
						double infoGain = informationGain(examples, trueBranch, falseBranch);
						if(infoGain > bestInfoGain) {
							bestInfoGain = infoGain;
							splitAttribute = label;
						}
					}
				}
			}
		}

		return splitAttribute;
	}

	private static double informationGain(ArrayList<ArrayList<Boolean>> parent, ArrayList<ArrayList<Boolean>> trueBranch, ArrayList<ArrayList<Boolean>> falseBranch) {
		// IG(parent, children) = entropy(parent) - [p(c1) * entropy(c1) + p(c2) * entropy(c2)]
		double weightedBranchEntropy = 0;
		if(!trueBranch.isEmpty()) {
			weightedBranchEntropy += trueBranch.size()/parent.size() * entropy(trueBranch);
		}
		if(!falseBranch.isEmpty()) {
			weightedBranchEntropy += falseBranch.size()/parent.size() * entropy(falseBranch);
		}
		double value = entropy(parent) - weightedBranchEntropy;
		if (Double.isNaN(value)) {
			return 0;
		}
		return value;
	}

	private static double entropy(ArrayList<ArrayList<Boolean>> subset) {
		// - [ p( democrats ) * log2 p( democrats ) + p( republicans ) * log2 p( republicans ) ]
		double trueCount = 0;
		double falseCount = 0;
		double entropy = 0;
		if(!subset.isEmpty()) {
			for(ArrayList<Boolean> exs : subset) {
				//exs <-- {e : e(of every)examples and e.A = vk}
				if(!exs.isEmpty()) {
					if(exs.get(exs.size()-1) == true) {
						trueCount++;
					}
					else if(exs.get(exs.size()-1) == false) {
						falseCount++;
					}
				}
			}
			trueCount = trueCount/subset.size();
			falseCount = falseCount/subset.size();
			entropy += trueCount*(Math.log(trueCount)/Math.log(2)+1e-10);
			entropy += falseCount*(Math.log(falseCount)/Math.log(2)+1e-10);
			entropy = entropy*-1;
		}
		return entropy;
	}

	private static String pluralityValue(ArrayList<ArrayList<Boolean>> examples) {
		int count0 = 0;
		int count1 = 0;
		if(examples != null) {
			for(ArrayList<Boolean> ex: examples) {
				if(ex.get(ex.size()-1) == false) {
					count0++;
				}
				else if(ex.get(ex.size()-1) == true) {
					count1++;
				}
			}

			if(count0 > count1) {
				return classification0;
			}
			else if(count0 < count1) {
				return classification1;
			}
			else {
				int choice = rand.nextInt(2);
				if(choice == 0) {
					return classification0;
				}
				else if(choice == 1) {
					return classification1;
				}
			}
		}
		return classification1;
	}

	private static boolean sameClassification(ArrayList<ArrayList<Boolean>> examples) {
		boolean same = true;
		if(examples.isEmpty() || examples == null) {
			return same;
		}
		for(int i = 1; i < examples.size(); i++) {
			Boolean first = examples.get(i-1).get(examples.get(i-1).size()-1);
			Boolean second = examples.get(i).get(examples.get(i).size()-1);
			if(first != null && second != null && !first.equals(second)) {
				same = false;
			}
		}
		return same;
	}

	private static double tester(ArrayList<ArrayList<Boolean>> test, DecisionTree<String> tree) {
		double correct = 0;

		//cycle through all the congress members
		for(ArrayList<Boolean> congressMem: test) {
			if(recursiveTester(congressMem, tree)) {
				correct++;
			}
		}

		double accuracy = correct/(double)(test.size());
		return accuracy;
	}

	private static boolean recursiveTester(ArrayList<Boolean> congressMem, DecisionTree<String> tree) {
		boolean correct = true;
		if(tree.getData() != null) {
			String attribute = tree.getData();
			//if the tree is a leaf and has either classifications check if the congressmember does too and if so, add to the correct counter
			if(attribute.equals(classification0)) {
				if(congressMem.get(congressMem.size()-1).equals(false)) {
					return true;
				}
				else {
					return false;
				}
			}
			else if(attribute.equals(classification1)) {
				if(congressMem.get(congressMem.size()-1).equals(true)) {
					return true;
				}
				else {
					return false;
				}
			}
			//otherwise, cycle through the attribute labels to find the one that = the tree's data
			else {
				//for(String label : voteLabels) {
					for(int i = 0; i < voteLabels.size(); i++) {
						//goes through the original attributes list to find the index of the label in question
						if(voteLabels.get(i).equals(attribute)) {
							int labelIndex = i;
							if(congressMem.get(labelIndex) == true) {
								if(tree.hasLeftChild()) {
									correct = recursiveTester(congressMem, tree.getLeftChild());
								}
							}
							else if(congressMem.get(labelIndex) == false) {
								if(tree.hasRightChild()) {
									correct = recursiveTester(congressMem, tree.getRightChild());
								}
							}
						}
					}
				//}
			}
		}
		return correct;
	}

}