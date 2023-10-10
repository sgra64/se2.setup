package application;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;


/**
 * JUnit 5 tests.
 */
public class AppTest {

    private App app = new App();

    @Test
    public void test0001_FactorizeRegularCases() {
        assertIterableEquals(Arrays.asList(2, 2, 3, 3), app.factorize(36));
        assertIterableEquals(Arrays.asList(2, 2, 3, 7, 13), app.factorize(1092));
        assertIterableEquals(Arrays.asList(7, 23, 59, 1153), app.factorize(10952347));
    }

    @Test
    public void test0002_FactorizeCornerCases() {
        assertIterableEquals(Arrays.asList(0), app.factorize(0));
        assertIterableEquals(Arrays.asList(1), app.factorize(1));
        assertIterableEquals(Arrays.asList(2), app.factorize(2));
        assertIterableEquals(Arrays.asList(3), app.factorize(3));
        assertIterableEquals(Arrays.asList(2, 2), app.factorize(4));
    }

    @Test
    void test0003_FactorizeExceptionCases() {
        var msg = "illegal negative parameter: ";
        //
        Exception thrown = assertThrows(
           IllegalArgumentException.class,
           () -> app.factorize(-1));
        assertEquals(msg + -1, thrown.getMessage());
        //
        thrown = assertThrows(
           IllegalArgumentException.class,
           () -> app.factorize(-100));
        assertEquals(msg + -100, thrown.getMessage());
        //
        thrown = assertThrows(
           IllegalArgumentException.class,
           () -> app.factorize(Integer.MIN_VALUE));
        assertEquals(msg + Integer.MIN_VALUE, thrown.getMessage());
    }
}
