package application;

import java.util.Arrays;
import java.util.List;


/**
 * Main application class.
 * 
 */
public class App {

    /**
     * Default constructor.
     */
    public App() { }

    /**
     * main() function.
     * 
     * @param args command line arguments passed to <i>main()</i>.
     */
    public static void main(String[] args) {
        int n = 36;
        if(args.length > 0) {
            try{
                n = Integer.parseInt(args[0]);
            } catch(NumberFormatException ex) { }
        }
        try {
            var app = new App();
            var factors = app.factorize(n);
            System.out.println("Hello, App!");
            System.out.println(String.format("n=%d factorized is: %s", n, factors.toString()));
            //
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * Factorize a number into smallest prime factors.
     * <p>
     * Examples:
     * <pre>
     * n=27 -> [3, 3, 3]
     * n=1092 -> [2, 2, 3, 7, 13]
     * n=10952347 -> [7, 23, 59, 1153]
     * </pre>
     * @param n number to factorize
     * @return list of factors
     */
    public List<Integer> factorize(int n) {

        /*
         * Code returns solution for n=36: [2, 2, 3, 3] (2*2*3*3=36).
         * TODO: remove dummy code and complete function to return correct solution for any n.
         * find inspiration, e.g. here: https://www.geeksforgeeks.org/prime-factor
         */
        return Arrays.asList(2, 2, 3, 3);
    }

}