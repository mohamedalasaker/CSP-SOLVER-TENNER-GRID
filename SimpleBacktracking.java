import java.util.*;

public class SimpleBacktracking {

	int nva; // number of variable assignments 
	static int nck; // number of consistency checks (static because we increment it in utilityFunctions class not here)
	double startTime; // start time of the search
	List<List<Integer>> nextSelections; // A list of pairs (the location of the variables that needs to be assigned)
	List<List<Integer>> assignmentsPrintArray; // a list contains all of the location of the variables that will be assigned in the end

	
	public boolean SimpleBacktrackingSerach(int[][] assignments) {
		
		nva = 0;
		nck = 0;
		startTime = System.currentTimeMillis();
		nextSelections = new ArrayList<>();
		assignmentsPrintArray = new ArrayList<>();
		
		// loop through all cells and add the unassigned variables to next selection and assignmentsPrintArray
		for(int i = 0 ; i < assignments.length - 1 ; i++) {
			for(int j = 0 ; j < assignments[0].length ; j++) {
				if(assignments[i][j] == -1) {
					ArrayList<Integer>  pair = new ArrayList<>();
					// i is the row 
					// j is the column
					pair.add(i);
					pair.add(j);
					nextSelections.add(pair);
					assignmentsPrintArray.add(pair); //to print final assignments
				}
			}
		}
		
		
		// call the recursive function
		boolean a = SimpleBacktrackingRec(assignments);
		// printing
		System.out.println("========================\n\n");
		System.out.println("simple backtrack search:");
		// last parameter for choice, so the initial state(choice = 0) so we dont print the nck , nva
		// here choice = 1 because we want to print it.
		UtilityFunctions.printTenner(assignments, nva, nck, startTime,1);
		System.out.println();
		// print final assignments made
		for(List<Integer> b : assignmentsPrintArray) {
			System.out.println("[" + b.get(0) + "," + b.get(1) +"] =" + assignments[b.get(0)][b.get(1)]);
		}
		System.out.println("========================\n\n");
		return a;
	}

	
	private boolean SimpleBacktrackingRec(int[][] assignments) {
		// if the assignments complete then return
		if (UtilityFunctions.isComplete(assignments)) {
			return true;
		}
		
		
		// get the first unassigned cell
		List<Integer> k = nextSelections.get(0);
		
		// loop through the domain and try the values 
		for (int j = 0; j <= 9; j++) {
			// assign a value
			assignments[k.get(0)][k.get(1)] = j;
			nva++;			
			// to remove the variable in the first time because we have assigned it now
			if (j == 0) 
				nextSelections.remove(0);
			
			// check if consistent with the constraint after assign
			if (UtilityFunctions.checkConstraint(assignments, k.get(0), k.get(1), assignments[assignments.length - 1][k.get(1)])) {
				boolean flag = SimpleBacktrackingRec(assignments);
				
				// if assignment complete in all the next cells return true
				if (flag == true) {
					return true;
				} else if (j == 9) {
					// here to reset the value so we can backtrack to the previous assignment
					assignments[k.get(0)][k.get(1)] = -1;
					// here we re add it to the vars that need to be assigned
					nextSelections.add(0, k);
				}
			} else { // here if not consistent with constraint try another value
				if (j == 9) { // here if no value is consistent then add it to next selection and backtrack
					nextSelections.add(0, k);
				}
				// reset
				assignments[k.get(0)][k.get(1)] = -1;
			}

		}
		return false;
	}

	
}
