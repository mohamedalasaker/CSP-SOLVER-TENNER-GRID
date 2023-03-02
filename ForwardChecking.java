import java.util.ArrayList;
import java.util.List;


public class ForwardChecking {

	int nva; // number of variable assignments 
	static int nck; // number of consistency check 
	double startTime; // start time of the search
	List<List<int[]>> nextSelections; // list of pair of a location and domain (the first array we made it for location and the second for domain)
	List<List<Integer>> assignmentsPrintArray; // to print the final assignments we made at the end

	public boolean ForwardCheckingSearch(int[][] assignments){
		
		nva = 0;     
		nck = 0;
		startTime = System.currentTimeMillis();
		nextSelections = new ArrayList<>();
		assignmentsPrintArray = new ArrayList<>();
		
		// add unassigned cells to assignmentPrintArray
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
		
		
		// here we add each location with its domain (initial the domain has all values 0..9)
		for (int i = 0; i < assignments.length - 1; i++) {
			for (int j = 0; j < assignments[0].length; j++) {
				if (assignments[i][j] == -1) {
					int[] loc = new int[2];
					int[] domain = new int[10];
					
					for (int k = 0; k < 10; k++) {
						domain[k] = -1;
					}
					
					loc[0] = i;
					loc[1] = j;
					
					for (int k = 0; k <= 9; k++) {
						domain[k] = k;	
					}
					
					List<int[]> var = new ArrayList<>();
					var.add(loc); // index 0
					var.add(domain); // index 1
					nextSelections.add(var);
				}
			}
		}

		// call the recursive 
		
		boolean a = ForwardCheckingRec(assignments);
		
		// printing
		System.out.println("========================\n\n");
		System.out.println("forward check");
		UtilityFunctions.printTenner(assignments, nva, nck, startTime,1);
		System.out.println();
		for(List<Integer> b : assignmentsPrintArray) {
			System.out.println("[" + b.get(0) + "," + b.get(1) +"] =" + assignments[b.get(0)][b.get(1)]);
		}
		System.out.println("========================\n\n");		
		return a;
	}

	
	
	
	private boolean ForwardCheckingRec(int[][] assignments) {
		// if complete return true
		if (UtilityFunctions.isComplete(assignments)) {
			return true;
		}
		
		// take the first unassigned cell
		List<int[]> k = nextSelections.remove(0);
		
		// try the values
		for (int j = 0; j <= 9; j++) {
			
			// it checks if it is not in the domain then dont try it
			int[] dom = k.get(1);
			if(!UtilityFunctions.isInDomain(dom, j)) {
				if(j == 9) {
					assignments[k.get(0)[0]][k.get(0)[1]] = -1;
					nextSelections.add(k);
				}
				continue;
			}
			
			// assign a value
			assignments[k.get(0)[0]][k.get(0)[1]] = j;
			nva++;
			
			// check if it is consistent with constraint
			if (UtilityFunctions.checkConstraint(assignments, k.get(0)[0], k.get(0)[1],assignments[assignments.length - 1][k.get(0)[1]])) {
				// tmp1 is the list with a pair of location and the values of the domain that have been deleted from that cell
				// so we can reset them if the search did not succeed so we can change this assignment the go back to those which are deleted and try them again
				List<List<int[]>> tmp1 = forwardCheck(assignments, k.get(0)[0], k.get(0)[1]);
				boolean flag = ForwardCheckingRec(assignments);
				if (flag == true){
					return true;
				} else if (j == 9){
					// if no value in the domain is valid then it goes back to nextselection to backtrack and then try it again
					nextSelections.add(k);
					assignments[k.get(0)[0]][k.get(0)[1]] = -1;
				}
				
				// reset the deleted values from each domain back to the domain
				for(List<int[]> e : tmp1) {
					// search domain takes a location (row,column) then return its domain from nextSelection
					try {
						int []dd = searchDomain(e.get(0)[0], e.get(0)[1]);
						for(int p = 0 ; p < e.get(1).length ; p++) {
							if(e.get(1)[p] != -1) {
								dd[e.get(1)[p]] = e.get(1)[p] ;
							}
						}
					}catch(Exception x) {
						
					}
				}
			} else {
				// if no value in the domain is valid then it goes back to nextselection to backtrack and then try it again
				if (j == 9) {
					assignments[k.get(0)[0]][k.get(0)[1]] = -1;
					nextSelections.add(k);
				}
			}

			
		}
		return false;
	}

	

	public int[] searchDomain(int rowNum, int colNum) {
		// it search for the location in nextSelecion and return the domain of this location
		int[] a = null;
		for (List<int[]> k : nextSelections) {
			if (k.get(0)[0] == rowNum && k.get(0)[1] == colNum) {
				// if found return it
				a = k.get(1);
				return a;
			}
		}
		return null; // not found (it must be there we are sure of it we returned it to the list after we tried every possible value)
	}

	
	private void ContiguesReduction(int assignments[][] , int rowNum , int colNum , List<List<int[]>> removedDomain) {

		// loop through the row above it - below it    - right , left columns
		// if there is indexoutofbound we will catch it (for edge cases like a corner cell it will happen)
		for(int i = rowNum - 1 ; i <= rowNum + 1 ; i++) {                            
			// loop through columns	
			for(int j = colNum - 1 ; j <= colNum + 1 ; j++) {
				try{
					if(assignments[i][j] == -1) {
						 // violated constraint of contiguous
						searchDomain(i,j)[assignments[rowNum][colNum]] = -1;
						// add to deleted domain
						List<int[]> tmp = new ArrayList<>();
						int[] loc = {i,j};
						int[] deletedDomain = {assignments[rowNum][colNum]};
						tmp.add(loc);
						tmp.add(deletedDomain);
						removedDomain.add(tmp);
					}
				}catch (Exception e) {
						// just catch outOfBound exception
				}	
			}
			i++; // to skip the row that we have assigned (it is all ready reduced in the row reduction)
		}
		
		
	}

	
	
	private List<List<int[]>> forwardCheck(int assignments[][], int rowNum, int colNum) {
		// it return a list of pair of location and and array of deleted values of the domain of that location 
		
		// increment number of consistincy checks
		ForwardCheckingWithMRVHeuristic.nck++;
		ForwardChecking.nck++;
		// list to save the location and the reduced values of this variable
		List<List<int[]>> tmp = new ArrayList<>();
		
		// reduce domain row
		for (int i = 0; i < assignments[0].length; i++) {
			if (assignments[rowNum][i] == -1) {
				// get the domain and remove that value = assignments[rowNum][colNum]
				searchDomain(rowNum, i)[assignments[rowNum][colNum]] = -1;
				// save the location to add it to tmp
				int[] loc = {rowNum,i};
				// the deleted values stored here (only the value of assignments[rowNum][colNum] sure)
				int[] deletedDomain = {assignments[rowNum][colNum]};
				List<int[]> tmp1 = new ArrayList<>();
				tmp1.add(loc);
				tmp1.add(deletedDomain);
				tmp.add(tmp1);
			}
		}
		
		
		// reduce domain column
		int c = 0; // sum of that column
		// loop through the column 
		for (int i = 0; i < assignments.length - 1; i++) {
			if (assignments[i][colNum] != -1){
				c += assignments[i][colNum];
			}
		}
		
		// count is used to check if there is more than one empty variable  we will 
		// check if the sum is <= the last cell in the coulmn then it is okay
		// if only one empty var in the coulmn then the sum must = lat cell
		int count = 0;
		for (int k = 0; k < assignments.length - 1; k++) {
			if (assignments[k][colNum] == -1) {
				count++;
			}
		}
		for (int i = 0; i < assignments.length - 1; i++) {
			if (assignments[i][colNum] == -1) {
				int[] loc = {i,colNum};
				int[] deletedDomain = new int[10];
				// set to -1 (we set it to -1 to know that it is not deleted)
				for(int s = 0 ; s < 10 ; s++) {
					deletedDomain[s] = -1;
				}
				List<int[]> tmp2 = new ArrayList<>();
				tmp2.add(loc);

				int[] tmp1 = searchDomain(i,colNum);
				// loop through the  values in that domain
				for (int k = 0; k < tmp1.length; k++) {
					if (tmp1[k] != -1) {
						if(count >=2) {
							if (tmp1[k] + c > assignments[assignments.length - 1][colNum]) {
								deletedDomain[tmp1[k]] = tmp1[k];
								tmp1[k] = -1;
							}
						}else {
							if(tmp1[k] + c != assignments[assignments.length - 1][colNum]) {
								deletedDomain[tmp1[k]] = tmp1[k];
								tmp1[k] = -1;
							}
						}
					}
				}
				tmp2.add(deletedDomain);
				tmp.add(tmp2);

			}
		}

		// contigues reduction
		ContiguesReduction(assignments, rowNum, colNum,tmp);
		return tmp;
		
	}	

}
