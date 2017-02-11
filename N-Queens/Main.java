import Algorithms.*;

import java.util.Scanner;

/**
 * Main class.
 */
public class Main {

    // Seed -8191564060360249868 lets the heuristic search solve n = 150 in 20 secs.
    public static void main(String[] args) {
        if (args.length != 1) {
            printMenu();
            return;
        }

        int arg;

        try {
            arg = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            printMenu();
            return;
        }

        handleParameters(arg);
    }

    private static void handleParameters(int arg) {
        if (arg == 0) {
            int n = getIntInput("Queens: ");
            int seed = getIntInput("Seed: ");
            boolean debug = getBooleanInput();
            CompletelyRandom.search(n, seed, debug);
        } else if (arg == 1) {
            int n = getIntInput("Queens: ");
            int seed = getIntInput("Seed: ");
            boolean debug = getBooleanInput();
            SemiRandom.search(n, seed, debug);
        } else if (arg == 2) {
            int n = getIntInput("Queens: ");
            int timeLimit = getIntInput("Time Limit (secs): ");
            boolean debug = getBooleanInput();
            BlindSearchNaive.search(n, timeLimit, debug);
        } else if (arg == 3) {
            int n = getIntInput("Queens: ");
            int timeLimit = getIntInput("Time Limit (secs): ");
            boolean debug = getBooleanInput();
            BlindSearchIntermediate.search(n, timeLimit, debug);
        } else if (arg == 4) {
            int n = getIntInput("Queens: ");
            int timeLimit = getIntInput("Time Limit (secs): ");
            boolean debug = getBooleanInput();
            BlindSearchAdvanced.search(n, timeLimit, debug);
        } else if (arg == 5) {
            int n = getIntInput("Queens: ");
            int seed = getIntInput("Seed: ");
            int timeLimit = getIntInput("Time Limit (secs): ");
            boolean debug = getBooleanInput();
            IterativeRepair.search(n, seed, timeLimit, debug);
        } else if (arg == 6) {
            int n = getIntInput("Queens: ");
            int seed = getIntInput("Seed: ");
            int timeLimit = getIntInput("Time Limit (secs): ");
            boolean debug = getBooleanInput();
            HeuristicSearch.search(n, seed, timeLimit, debug);
        } else if (arg == 7) {
            int n = getIntInput("Queens: ");
            ExplicitSolution.run(n);
        } else {
            System.out.println("Parameter must be between 0 and 7 inclusive.");
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Usage: ");
        System.out.println("\tjava Main [integer from 0 to 7]");
        System.out.println();

        System.out.println("Parameters: ");
        System.out.println("\t0 - random search");
        System.out.println("\t1 - semi-random search");
        System.out.println("\t2 - naive blind search");
        System.out.println("\t3 - intermediate blind search");
        System.out.println("\t4 - advanced blind search");
        System.out.println("\t5 - iterative repair");
        System.out.println("\t6 - population based search");
        System.out.println("\t7 - explicit solution");
        System.out.println();

        System.out.println("Examples: ");
        System.out.println("\tjava Main 1");
        System.out.println("\tjava Main 6");
        System.out.println();
    }

    private static boolean getBooleanInput() {
        System.out.print("Debug Mode: ");
        Scanner scanner = new Scanner(System.in);
        boolean input = false;
        try {
            input = scanner.nextBoolean();
        } catch (Exception ex) {
            System.out.println("Input must be a boolean (true/false).");
            System.exit(-1);
        }
        return input;
    }

    private static int getIntInput(String msg) {
        System.out.print(msg);
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        try {
            input = scanner.nextInt();
        } catch (Exception ex) {
            System.out.println("Input must be an integer.");
            System.exit(-1);
        }

        if (msg.equals("Queens: ") && input < 4) {
            System.out.println("Number of Queens should be at least 4.");
            System.exit(-1);
        }

        return input;
    }

}