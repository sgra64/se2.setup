package application;

import java.util.*;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Class to factorize numbers into prime factors.
 * <p>
 * Examples:
 * <pre>
 * n=27 -> [3, 3, 3]
 * n=1092 -> [2, 2, 3, 7, 13]
 * n=10952347 -> [7, 23, 59, 1153]
 * </pre>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class Factorizer implements Runnable {

    /*
     * arguments passed from command line
     */
    private final String[] args;

    /**
     * Public {@code String[] args} constructor.
     * @param args arguments passed from command line
     */
    public Factorizer(String[] args) { this.args = args; }


    /**
     * Method of {@link Runnable} interface called on {@code Application}
     * instance. Actual program execution starts here.
     */
    @Override
    public void run() {
        // say Hello and process args[]
        System.out.println(String.format("Hello, %s", this.getClass().getSimpleName()));
        parseArgs(args)
            .map(n -> factorize(n))
            .map(factors -> String.format(" - factorized: %s", factors))
            .forEach(System.out::println);
    }


    /**
     * Factorize a number {@code n} into its prime factors.
     * <p>
     * Examples:
     * <pre>
     * n=27 -> [3, 3, 3]
     * n=1092 -> [2, 2, 3, 7, 13]
     * n=10952347 -> [7, 23, 59, 1153]
     * </pre>
     * @param n number to factorize
     * @return list of prime factors
     */
    public List<Integer> factorize(int n) {

        /*
         * Code returns solution for n=36: [2, 2, 3, 3] (2*2*3*3=36).
         * TODO: complete function to return correct solution for any n.
         * find inspiration, e.g. here: https://www.geeksforgeeks.org/prime-factor
         */
        return Arrays.asList(2, 2, 3, 3);
    }


    /**
     * Parse sequence of arguments for patterns {@code "n=10"}, {@code "n=100"}.
     * Return as Stream of Integers {@code 10}, {@code 100}.
     * @param args arguments passed from command line
     * @return Stream of (Integer) {@code n} parsed from argument list
     */
    private Stream<Integer> parseArgs(String[] args) {
        return Stream.of(args)
            .filter(arg -> arg != null && arg.length() > 0)
            .peek(System.out::println)
            .map(arg -> {
                // split arg: "n=16" into "n" (argsp[0]) and "16" (argsp[1])
                var sp = arg.split("=");
                var n = sp.length >= 2 && sp[0].equals("n")? sp[1] : "0";
                try {
                    return Optional.of(Integer.parseInt(n));
                } catch(NumberFormatException e) {
                    System.out.println(String.format("cannot parse int from: \"%s\"", arg));
                }
                return Optional.ofNullable((Integer)null);
            })
            .filter(opt -> opt.isPresent() && opt.get() > 0)
            .map(opt -> Integer.valueOf(opt.get()));
    }
}