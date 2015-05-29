package applications;

import java.util.Scanner;
import applications.Runner;

public class EntryPoint {
	
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {		
		
		
		System.out.println("Welcome to the Network Simulator");
		boolean repeat = true;
		
		while (repeat) {
			dumpStart();

			boolean loop = true;
			
			while (loop) {
				String s = sc.next();

				if (s.equals("descr")) {
					int choice = sc.nextInt();
					if(!Runner.describe(choice)) System.out.println("Please pick a correct index.");
					else waitForContinue(); System.out.println("\n"); dumpStart();
				} else if (s.equals("run")) {
					int choice = sc.nextInt();
					if (Runner.start(choice)) loop = false;
					else System.out.println("Please pick a correct index.");
				} else {
					System.out.println("Don't know what you mean, please try again.");
					loop = true;
				}
				sc.nextLine();
			}

			System.out.println("\nDo you wish to run another simulation? (y/n)");
			String c = sc.nextLine();
			if (c.equals("y") || c.equals("Y")) {
				repeat = true;
			} else {
				repeat = false;
			}
		}
		System.out.println("Closing simulator.");
		
	}
	
	private static void dumpStart() {
		System.out.println("Type 'run X' to run any of the simulations, type 'descr X' to get their description.");
		System.out.println("The following simulations are available: ");
		Runner.printOptions();
	}
	
	private static void waitForContinue() {
		System.out.println("\nContinue? (y)");
		sc.next();
	}
}
