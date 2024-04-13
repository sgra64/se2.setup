package application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;


/**
 * JUnit 5 tests for Factorizer class.
 */
public class Factorizer_Tests {

    /*
     * test object is an instance of the Factorizer class
     */
    private static Factorizer testObj;


    /**
     * Static setup method executed once for all tests. Creates
     * the test object.
     * 
     * @throws Exception is creation of test object fails
     */
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        testObj = Application.createFactorizer(null);
    }


    @Test
    public void test0001_FactorizeRegularCases() {
        assertIterableEquals(Arrays.asList(2, 2, 3, 3), testObj.factorize(36));
        assertIterableEquals(Arrays.asList(2, 2, 3, 7, 13), testObj.factorize(1092));
        assertIterableEquals(Arrays.asList(7, 23, 59, 1153), testObj.factorize(10952347));
    }


    @Test
    public void test0002_FactorizeCornerCases() {
        assertIterableEquals(Arrays.asList(0), testObj.factorize(0));
        assertIterableEquals(Arrays.asList(1), testObj.factorize(1));
        assertIterableEquals(Arrays.asList(2), testObj.factorize(2));
        assertIterableEquals(Arrays.asList(3), testObj.factorize(3));
        assertIterableEquals(Arrays.asList(2, 2), testObj.factorize(4));
    }


    @Test
    void test0003_FactorizeExceptionCases() {
        var msg = "illegal negative parameter: ";
        //
        Exception thrown = assertThrows(
           IllegalArgumentException.class,
           () -> testObj.factorize(-1));
        assertEquals(msg + -1, thrown.getMessage());
        //
        thrown = assertThrows(
           IllegalArgumentException.class,
           () -> testObj.factorize(-100));
        assertEquals(msg + -100, thrown.getMessage());
        //
        thrown = assertThrows(
           IllegalArgumentException.class,
           () -> testObj.factorize(Integer.MIN_VALUE));
        assertEquals(msg + Integer.MIN_VALUE, thrown.getMessage());
    }
}