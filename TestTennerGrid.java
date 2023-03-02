import java.math.*;
import java.util.*;



public class TestTennerGrid {

	
	
	
	public static void main(String[] args) {
			
		int noOfRows = 6;
	
		int[][] a4 = new int[noOfRows][10];
		
		UtilityFunctions.generateTennerGrid(a4);
		System.out.println("The initial state is:");
		UtilityFunctions.printTenner(a4, 0, 0, 0, 0);
		System.out.println();
		Scanner input = new Scanner(System.in);
		int choice;
		do {

			System.out.println("1- Simple backtracking");
			System.out.println("2- Backtracking with forward checking");
			System.out.println("3- Backtracking with forward checking + MRV");
			System.out.println("4- Backtracking with MRV");
			System.out.println("5- regenerate grid");
			System.out.println("6- Exit");
			System.out.print("Enter choice of algorithm: ");

			choice = input.nextInt();

			switch (choice) {

			case 1:

				int[][] k1 = new int[noOfRows][10];
				UtilityFunctions.copyArray(a4, k1);

				SimpleBacktracking f = new SimpleBacktracking();
				f.SimpleBacktrackingSerach(k1);

				break;

			case 2:

				int[][] k3 = new int[noOfRows][10];
				UtilityFunctions.copyArray(a4, k3);
				ForwardChecking s = new ForwardChecking();
				s.ForwardCheckingSearch(k3);

				break;

			case 3:

				int[][] k2 = new int[noOfRows][10];
				UtilityFunctions.copyArray(a4, k2);

				ForwardCheckingWithMRVHeuristic f1 = new ForwardCheckingWithMRVHeuristic();
				f1.ForwardCheckingWithMRVHeuristicSearch(k2);

				break;

			case 4:

				int[][] k4 = new int[noOfRows][10];
				UtilityFunctions.copyArray(a4, k4);

				BacktrackingWithMRVHeuristic r = new BacktrackingWithMRVHeuristic();
				r.BacktrackingWithMRVHeuristicSearch(k4);

				break;

			case 5:
				UtilityFunctions.generateTennerGrid(a4);
				System.out.println("regenerated succesfully");
				break;
			case 6:
				System.out.println("Thank you");
				System.exit(0);
				break;

			default:
				System.out.println("Choice not valid");
				break;

			}

		} while (choice != 6);

	}
		
}
