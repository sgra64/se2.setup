package application;

import java.util.*;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Class to factorize a number into prime factors.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
@RunPriority(priority = 2)
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
        Stream.of(args)
            .filter(arg -> arg != null && arg.length() > 0)
            .peek(System.out::println)
            .map(arg -> {
                Optional<String> empty = Optional.empty();
                // split arg: "n=16" into "n" (argsp[0]) and "16" (argsp[1])
                var argsp = arg.split("=");
                if(argsp.length >= 2) {
                    String nstr = argsp[1];
                    return Optional.of(nstr);
                }
                return empty;
            })
            .filter(opt -> opt.isPresent())
            .map(nstr -> {
                Optional<Integer> empty = Optional.empty();
                try {
                    return Optional.of(Integer.parseInt(nstr.get()));
                } catch(NumberFormatException e) {
                    System.out.println(String.format("cannot parse int from: \"%s\"", nstr));
                }
                return empty;
            })
            .filter(opt -> opt.isPresent())
            .mapToInt(opt -> Integer.valueOf(opt.get()))
            .mapToObj(n -> factorize(n))
            .map(factors -> String.format(" - factorized: %s", factors))
            .peek(System.out::println)
            .count();
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