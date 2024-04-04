package application;

import java.util.*;


/**
 * Class to factorize a number into prime factors.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
@RunPriority(priority = 20)
public class Factorizer_2 extends Factorizer {

    /**
     * Public {@code String[] args} constructor.
     * @param args arguments passed from command line
     */
    public Factorizer_2(String[] args) { super(args); }

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
    @Override
    public List<Integer> factorize(int n) {
        if(n < 0)
            throw new IllegalArgumentException(String.format("illegal negative parameter: %d", n));
        //
        List<Integer> factors  = new ArrayList<Integer>();
        //
        if(n==0 || n==1) {
            factors.add(n);
        } else {
            // use number of 2s that divide n
            while(n % 2 == 0) {
                factors.add(2);
                n /= 2;
            }
            // n must be odd at this point. We can skip one element (Note i = i +2)
            for(int i = 3; i <= Math.sqrt(n); i += 2) {
                // While i divides n, use i and divide n
                while(n % i == 0) {
                    factors.add(i);
                    n /= i;
                }
            }
            // n is a prime number greater than 2
            if(n > 2) {
                factors.add(n);
            }
        }
        return factors;
    }
}