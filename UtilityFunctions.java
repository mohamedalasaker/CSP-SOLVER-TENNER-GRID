import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class UtilityFunctions{
	

	
	public static boolean checkConstraint(int[][] assignments, int rowNum, int colNum, int sum) {
		
		if (checkRow(assignments, rowNum) && checkSumCoulmn(assignments, colNum, sum)
				&& checkcontiguous(assignments, rowNum, colNum)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkRow(int[][] assignments, int rowNum) {
		
		ForwardCheckingWithMRVHeuristic.nck++;
		SimpleBacktracking.nck++;
		ForwardChecking.nck++;
		BacktrackingWithMRVHeuristic.nck++;
		int[] compare = new int[assignments[0].length];
		for (int i = 0; i < compare.length; i++) {
			compare[i] = -1;
		}

		for (int j = 0; j < assignments[0].length; j++) {
			if (assignments[rowNum][j] >= 0 && compare[assignments[rowNum][j]] == -1) {
				compare[assignments[rowNum][j]] = assignments[rowNum][j];
			} else if (assignments[rowNum][j] >= 0 && compare[assignments[rowNum][j]] != -1) {
				return false;
			}
		}
		return true;
	}

	
	public static boolean checkSumCoulmn(int[][] assignments, int colNum, int sum) {
		ForwardCheckingWithMRVHeuristic.nck++;
		SimpleBacktracking.nck++;
		ForwardChecking.nck++;
		BacktrackingWithMRVHeuristic.nck++;

		int c = 0;
		boolean flag = false;
		for (int j = 0; j < assignments.length - 1; j++) {
			if (assignments[j][colNum] != -1) {
				c += assignments[j][colNum];
			} else {
				flag = true;
			}
		}
		if (flag == false) {
			return c == sum;
		}
		return c <= sum;
	}

		
	public static boolean checkcontiguous(int[][] assignments, int rowNum, int colNum) {

		ForwardCheckingWithMRVHeuristic.nck++;
		SimpleBacktracking.nck++;
		ForwardChecking.nck++;
		BacktrackingWithMRVHeuristic.nck++;

		// check the 3x3 square it is in (but it skips it self)
		for(int i = rowNum - 1 ; i <= rowNum + 1 ; i++) {                            
			// loop through columns	
			for(int j = colNum - 1 ; j <= colNum + 1 ; j++) {
				if(j != colNum && i != rowNum) {
					try{
						if(assignments[rowNum][colNum] == assignments[i][j]) {
							return false; // violated constraint of contiguous
						}
					}catch (Exception e) {
						// just catch outOfBound exception if cell is not valid
					}
				}	
			}
		}
		return true;
	}
	
	
	
	// fill and unary reduce grid
	public static void unaryReduce(int[][] assignments , PriorityQueue<List<int[]>> nextSelections){
		
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
						assignments[i][j] = k;
						if (UtilityFunctions.checkConstraint(assignments, i, j, assignments[assignments.length - 1][j])) {
							domain[k] = k;
						}
						assignments[i][j] = -1;
					}
					List<int[]> var = new ArrayList<>();
					var.add(loc);
					var.add(domain);
					nextSelections.add(var);
				}

			}
		}

	}
	
	public static void generateTennerGrid(int[][] grid) {
		int c = 0;
		while (!fillMatrix(grid) &&  c < 100000) {
			fillMatrix(grid);
			c++;
		}
		
		if(c >= 100000){
			int[][] tmp = backUp(grid);
			for(int i = 0 ; i < grid.length ; i++) {
				for(int j = 0 ; j < grid[0].length ; j++) {
					grid[i][j] = tmp[i][j];
				}
			}
		}else{
			for(int j = 0 ; j < grid[0].length ; j++) {
				int k = 0;
				for(int i = 0 ; i < grid.length - 1 ; i++) {
					k += grid[i][j];
				}
				grid[grid.length-1][j] = k;
			}
		}
		
		Random rand = new Random(); // create an instance of the Random class
		int limit = 0;
		if(grid.length == 4) {
			limit = 14;
		}else {
			limit = 19;
		}
		int counter = 0;
		for (int i = 0; i < grid.length - 1; i++) {             
			for (int j = 0; j < grid[0].length; j++) { 
				if(counter >= limit) {
					return;
				}
				int x = rand.nextInt(i+1);
				int y = rand.nextInt(j+1);
				if(grid[x][y] != -1) {
					grid[x][y] = -1; 
					counter++;	
				}
				
				
			}
		}
		
	}
	
	private static int[][] backUp(int[][] grid){
		
		if(grid.length == 3) {
			int[][] a = {{1,3,8,6,9,0,7,4,5,2},
						{5,7,1,4,3,8,2,9,0,6},
						{6,10,9,10,12,8,9,13,5,8}
					};
			return a;
			
		}
		
		if(grid.length == 4) {
			int[][] a = {{-1,6,2,0,-1,-1,-1,8,5,7},
					{-1,0,1,7,8,-1,-1,-1,9,-1},
					{-1,4,-1,-1,2,-1,3,7,-1,8},
					{13,10,8,7,19,16,11,19,15,17}};
			return a;
			
		}
		
		if(grid.length == 5) {
			int[][] a = {{5,3,7,0,9,6,4,8,2,1},
						{8,6,5,4,1,7,2,3,9,0},
						{1,2,7,3,8,9,6,0,5,4},
						{5,6,1,9,4,2,8,7,3,0},
						{19,17,20,16,22,24,20,18,19,5}};
			return a;
		}
		
		if(grid.length == 6) {
			int[][] a = {{-1,-1,1,-1,3,8,6,9,-1,2},
					{6,2,-1,-1,1,-1,-1,7,4,3},
					{7,4,5,-1,0,6,-1,-1,8,-1},
					{-1,-1,8,-1,4,9,5,7,-1,-1},
					{7,9,1,0,-1,-1,4,8,5,2},
					{26,20,15,27,14,35,21,34,23,10}};
			return a;
		}
		
		
		
		return null;
		
	}
	
	public static void copyArray(int[][] temp , int[][]temp1) {
		for(int i = 0 ; i < temp.length ; i++) {
			for(int j = 0 ; j < temp[0].length ; j++) {
				temp1[i][j] = temp[i][j];
			}
		}
	}
	
	
	private static boolean fillMatrix(int[][] matrix) { // method to fill the matrix
		int counter = 0; // variable to count the number of iterations
		Random rand = new Random(); // create an instance of the Random class

		for (int i = 0; i < matrix.length - 1; i++) { // loop through the rows
			for (int j = 0; j < matrix[i].length; j++) { // loop through the columns
				matrix[i][j] = -1; // initialize all cells with -1
			}
		}

		while (!isMatrixFilled(matrix)) { // while matrix is not filled
			for (int i = 0; i < matrix.length - 1; i++) { // loop through the rows
				boolean[] used = new boolean[10]; // array to keep track of the numbers already used
				boolean[] rowUsed = new boolean[10];
				for (int j = 0; j < matrix[i].length; j++) { // loop through the columns
					if (matrix[i][j] == -1) { // if the cell is not filled
						int num;
						do {
							num = rand.nextInt(10); // get a random number between 0 and 9
							counter++; // increment the counter
							if (counter > 200) { // threshold to exit the loop
								return false;
							}
						} while (used[num] || isContiguous(matrix, i, j, num) || rowUsed[num]);
						matrix[i][j] = num; // assign the random number to the cell
						used[num] = true;
						rowUsed[matrix[i][j]] = true;
					} else {
						rowUsed[matrix[i][j]] = true;
					}
				}
			}
		}
		return true;
	}

	private static boolean isContiguous(int[][] matrix, int row, int col, int num) {
		if (row > 0 && matrix[row - 1][col] == num) {
			return true;
		}
		if (col > 0 && matrix[row][col - 1] == num) {
			return true;
		}
		if (row > 0 && col > 0 && matrix[row - 1][col - 1] == num) {
			return true;
		}
		if (row > 0 && col < matrix[row].length - 1 && matrix[row - 1][col + 1] == num) {
			return true;
		}
		return false;
	}

	private static boolean isMatrixFilled(int[][] matrix) {
		for (int i = 0; i < matrix.length - 1; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == -1) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static boolean isComplete(int[][] assignments) {
		for (int i = 0; i < assignments.length - 1; i++) {
			for (int j = 0; j < assignments[0].length; j++) {
				if (assignments[i][j] == -1) {
					return false;
				}
			}
		}
		return true;
	}

	

	
	public static void printTenner(int a[][] , int nva , int nck , double startTime , int choice) {
		for (int i = 0; i < a.length; i++){
			if(i != a.length - 1) {
				
				System.out.print("    ");
			}
			for (int j = 0; j < a[0].length; j++) {
				
				if (a[i][j] == -1) {
					System.out.print("  " + "  " + "  |");
				} else {
					if (a[i][j] / 10 < 1) {
						System.out.print("  " + a[i][j] + "   |");
					} else {
						System.out.print("  " + a[i][j] + "  |");
					}
				}

			}
			System.out.println();
			if(i == a.length - 2) {
				System.out.println("------------------------------------------------------------------------------");
				System.out.print("Sum:");
			}
		}
		
		if(choice != 0) {
			System.out.println("the number of variable assignments is :" + nva);
			System.out.println("the number of consistincy checks is :" + nck);
			System.out.println("The total time is :" + (System.currentTimeMillis() - startTime));
		}
	}
	public static boolean isInDomain(int[] domain , int value) {
		for(int p = 0 ; p < domain.length ; p++) {
			if(value == domain[p]) {
				return true;
			}
		}
		return false;
	}
	
		

public static void printFinalAssignments(int a[][] , int nva , int nck , double startTime , int choice) {
	for (int i = 0; i < a.length; i++){
		if(i != a.length - 1) {
			
			System.out.print("    ");
		}
		for (int j = 0; j < a[0].length; j++) {
			
			if (a[i][j] == -1) {
				System.out.print("  " + "  " + "  |");
			} else {
				if (a[i][j] / 10 < 1) {
					System.out.print("  " + a[i][j] + "   |");
				} else {
					System.out.print("  " + a[i][j] + "  |");
				}
			}
			
		}
		System.out.println();
		if(i == a.length - 2) {
			System.out.println("------------------------------------------------------------------------------");
			System.out.print("Sum:");
		}
	}
	
	if(choice != 0) {
		System.out.println("the number of variable assignments is :" + nva);
		System.out.println("the number of consistincy checks is :" + nck);
		System.out.println("The total time is :" + (System.currentTimeMillis() - startTime));
	}
}




}


class comparator1 implements Comparator<List<int[]>> {

	public static int getSize(int[] a) {
		int c = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != -1)
				c++;
		}

		return c;
		
	}

	@Override
	public int compare(List<int[]> o1, List<int[]> o2) {
		// TODO Auto-generated method stub
		if (getSize(o1.get(1)) < getSize(o2.get(1))) {
			return -1;
		} else if (getSize(o1.get(1)) > getSize(o2.get(1))) {
			return 1;
		} else {
			return 0;
		}

	}
	

}
