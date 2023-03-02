import java.util.*;



public class BacktrackingWithMRVHeuristic{
	
	int nva; // number of variable assignments 
	static int nck; // number of consistency checks
	double startTime; // start of the search
	List<List<Integer>> assignmentsPrintArray; // a list contains all of the location of the variables that will be assigned in the end

	
	public boolean BacktrackingWithMRVHeuristicSearch(int[][] assignments) {
		nva = 0;     
		nck = 0;
		startTime = System.currentTimeMillis();	
		assignmentsPrintArray = new ArrayList<>();
		
		// add the unassigned variables to assignmentPrint array so we could display the final assignments we made 
		for(int i = 0 ; i < assignments.length - 1 ; i++) {
			for(int j = 0 ; j < assignments[0].length ; j++) {
				if(assignments[i][j] == -1) {
					ArrayList<Integer>  pair = new ArrayList<>();
					pair.add(i);
					pair.add(j);
					assignmentsPrintArray.add(pair); // new to print final assignments
				}
			}
		}
		
		// call the recursive method
		boolean a = BacktrackingWithMRVHeuristicRec(assignments);
		
		// printing
		
		System.out.println("========================\n\n");
		System.out.println("backtrack with mrv heuristic");
		UtilityFunctions.printTenner(assignments, nva, nck, startTime,1);
		System.out.println();

		for(List<Integer> b : assignmentsPrintArray) {
			System.out.println("[" + b.get(0) + "," + b.get(1) +"] =" + assignments[b.get(0)][b.get(1)]);
		}
		System.out.println("========================\n\n");
		return a;
	}

	

	
	
	private boolean BacktrackingWithMRVHeuristicRec(int[][] assignments) {
		// if complete return true
		if (UtilityFunctions.isComplete(assignments)) {
			return true;
		}
		
		
		// find min variable which has the least choices in the domain
		// (which means it is the most constrained)
		int[] minValuesInDomainLocation = new int[2];
		int maxConstraint = 0;
		// loop through the cells and find the variable the most constrained 
		for(int i = 0 ; i < assignments.length - 1 ; i++) {
			for(int j = 0 ; j < assignments[0].length ; j++){
				if(assignments[i][j] == -1) {
					// it counts the number of values that cannot be assigned (the number of values that cannot be assigned)
					int a = countConstraints(assignments, i, j);
					if(a > maxConstraint) {
						maxConstraint = a;
						minValuesInDomainLocation[0] = i;
						minValuesInDomainLocation[1] = j;
					}
				}
			}
		}
			
		
		for (int j = 0; j <= 9; j++) {
			// x is the row 
			// y is column
			int x = minValuesInDomainLocation[0];
			int y = minValuesInDomainLocation[1];
			
			// assign it a value
			assignments[x][y] = j;
			nva++;
			
			// check if it is consistent with constraints
			if (UtilityFunctions.checkConstraint(assignments,x, y,assignments[assignments.length - 1][y])) {
			
				boolean flag = BacktrackingWithMRVHeuristicRec(assignments);
				if (flag == true){ // if the call after it have been assigned successfully too
					return true;
				} else if (j == 9){
					// reset assignment 
					assignments[x][y] = -1;
				}
			} else {		
				// reset
				assignments[x][y] = -1;
			}

			
		}
		return false;
	}
	
	// it counts the number of values that cannot be assigned (the number of values that cannot be assigned)
	private static int countConstraints(int[][] assignments , int row , int col){
		// counter
		int i = 0;
		
		// loop through the domain and if value cannot be assigned i++ (more constrained)
		for (int k = 0; k <= 9; k++) {
			assignments[row][col] = k;
			int temp = nck; // to make nck not increase because we dont consider it to be a check here 
			if (!UtilityFunctions.checkConstraint(assignments, row, col, assignments[assignments.length - 1][col])) {
				i++;
			}
			nck = temp;
			assignments[row][col] = -1; // reset (we dont want to reduce the domain in bt + mrv)
		}
		return i;
	}

		
}


