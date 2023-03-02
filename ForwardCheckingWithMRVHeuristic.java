import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



public class ForwardCheckingWithMRVHeuristic {

	
	int nva; // number of variable assignments 
	static int nck; // number of consitincy checks 
	double startTime; // start time of the search
	PriorityQueue<List<int[]>> nextSelections; // priority queue of the variables (sorted from most constrained to least constrained)
	List<List<Integer>> assignmentsPrintArray;

	public boolean ForwardCheckingWithMRVHeuristicSearch(int[][] assignments) {
		nva = 0;     
		nck = 0;
		startTime = System.currentTimeMillis();
		// new compartor1() is a class written in Utility function class to make 
		// the comparsion of the PQ
		nextSelections = new PriorityQueue<>(new comparator1());
		assignmentsPrintArray = new ArrayList<>();
		
		// add unassigned variables to assignmentsPrintArray
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
		
		// this method will add the locations and domains to nextSelection (after it reduce its domain so all values in it can be assigned)
		UtilityFunctions.unaryReduce(assignments, nextSelections);
		// call the recursive
		boolean a = ForwardCheckingWithMRVHeuristicRec(assignments);
		
		// printing only
		System.out.println("========================\n\n");
		System.out.println("forward check with mrv");
		UtilityFunctions.printTenner(assignments, nva, nck, startTime,1);
		
		System.out.println();
		for(List<Integer> b : assignmentsPrintArray) {
			System.out.println("[" + b.get(0) + "," + b.get(1) +"] =" + assignments[b.get(0)][b.get(1)]);
		}
		System.out.println("========================\n\n");		
		return a;
	}

	

	private boolean ForwardCheckingWithMRVHeuristicRec(int[][] assignments) {

		// if complete
		if (UtilityFunctions.isComplete(assignments)) {
			return true;
		}
		
		
		// it will return the most constraint cell (the list has location and domain here) and remove from nextselection
		List<int[]> k = nextSelections.poll();
		
		// try the values on the domain
		for (int j = 0; j <= 9; j++) {
			
			// it checks if it is not in the domain then dont try it
			int[] dom = k.get(1);
			if(!UtilityFunctions.isInDomain(dom, j)) {
				if (j == 9) {
					assignments[k.get(0)[0]][k.get(0)[1]] = -1;
					nextSelections.add(k);
				}
				continue;
			}
			
			assignments[k.get(0)[0]][k.get(0)[1]] = j;
			nva++;
			if(UtilityFunctions.checkConstraint(assignments, k.get(0)[0], k.get(0)[1],assignments[assignments.length - 1][k.get(0)[1]])) {
				
				List<List<int[]>> tmp1 = forwardCheck(assignments, k.get(0)[0], k.get(0)[1]);
				boolean flag = ForwardCheckingWithMRVHeuristicRec(assignments);
				if (flag == true){
					return true;
				} else if (j == 9){
					assignments[k.get(0)[0]][k.get(0)[1]] = -1;
					nextSelections.add(k);
				}
				for(List<int[]> e : tmp1) {
					int []dd = searchDomain(e.get(0)[0], e.get(0)[1]);
					for(int p = 0 ; p < e.get(1).length ; p++) {
						if(e.get(1)[p] != -1) {
							dd[e.get(1)[p]] = e.get(1)[p] ;
						}
					}
				}
			} else {
				if (j == 9) {
					nextSelections.add(k);
				}
				assignments[k.get(0)[0]][k.get(0)[1]] = -1;
			}

			
		}
		return false;
	}


	// same as forward checking version
	private int[] searchDomain(int rowNum, int colNum) {

		int[] a = null;
		for (List<int[]> k : nextSelections) {
			if (k.get(0)[0] == rowNum && k.get(0)[1] == colNum) {
				a = k.get(1);
				return a;
			}
		}
		
		return a;
	}


	// same as the forwardchecking class 

	private void ContiguesReduction(int assignments[][] , int rowNum , int colNum , List<List<int[]>> removedDomain) {

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
			// to skip the row that we have assigned since we have done it in the row reduction
			i++; 
		}
		
		
	}

	
	// same as the forwardchecking class 
	public List<List<int[]>> forwardCheck(int assignments[][], int rowNum, int colNum) {
		
		ForwardCheckingWithMRVHeuristic.nck++;
		ForwardChecking.nck++;
		
		List<List<int[]>> tmp = new ArrayList<>();
		// reduce domain row
		for (int i = 0; i < assignments[0].length; i++) {
			if (assignments[rowNum][i] == -1) {
				searchDomain(rowNum, i)[assignments[rowNum][colNum]] = -1;
				int[] loc = {rowNum,i};
				int[] deletedDomain = {assignments[rowNum][colNum]};
				List<int[]> tmp1 = new ArrayList<>();
				tmp1.add(loc);
				tmp1.add(deletedDomain);
				tmp.add(tmp1);
			}
		}
		
		// reduce domain column
		int c = 0;
		for (int i = 0; i < assignments.length - 1; i++) {
			if (assignments[i][colNum] != -1){
				c += assignments[i][colNum];
			}
		}
		
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
				for(int s = 0 ; s < 10 ; s++) {
					deletedDomain[s] = -1;
				}
				List<int[]> tmp2 = new ArrayList<>();
				tmp2.add(loc);

				int[] tmp1 = searchDomain(i,colNum);
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

	
