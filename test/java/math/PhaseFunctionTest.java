package math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@code PhaseFunction} class.
 */
public class PhaseFunctionTest {

    private PhaseFunction pf;

    @BeforeEach
    public void setUp() {
        pf = new PhaseFunction();
    }

    @Test
    public void testGetValueNoPolesOrZeros() {
        assertEquals(0.0, pf.getValue(1.0), 1e-9);
    }

    @Test
    public void testGetValueWithZeroAtOrigin() {
        pf.addZero(new Zero(0.0, 0.0));
        double result = pf.getValue(1.0);
        // jw at w=1 gives j, phase = PI/2, in degrees = 90
        assertEquals(90.0, result, 1e-9);
    }

    @Test
    public void testGetValueWithPoleAtOrigin() {
        pf.addPole(new Pole(0.0, 0.0));
        double result = pf.getValue(1.0);
        // 1/(jw) at w=1: phase = -PI/2, in degrees = -90
        assertEquals(-90.0, result, 1e-9);
    }

    @Test
    public void testGetValueWithPoleAndZeroCancellation() {
        pf.addPole(new Pole(0.0, 0.0));
        pf.addZero(new Zero(0.0, 0.0));
        double result = pf.getValue(5.0);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testGetValueWithRealPole() {
        pf.addPole(new Pole(-1.0, 0.0));
        double result = pf.getValue(1.0);
        // 1/(j*1 - (-1)) = 1/(1+j), phase = -atan(1) = -PI/4, degrees = -45
        assertEquals(-45.0, result, 1e-9);
    }

    @Test
    public void testGetValueWithRealZero() {
        pf.addZero(new Zero(-1.0, 0.0));
        double result = pf.getValue(1.0);
        // j*1 - (-1) = 1+j, phase = atan(1) = PI/4, degrees = 45
        assertEquals(45.0, result, 1e-9);
    }

    @Test
    public void testAddPoleAndRemoveAll() {
        pf.addPole(new Pole(-1.0, 0.0));
        pf.addZero(new Zero(-2.0, 0.0));
        pf.removeAll();
        assertTrue(pf.getPoles().isEmpty());
        assertTrue(pf.getZeros().isEmpty());
        assertEquals(0.0, pf.getValue(1.0), 1e-9);
    }

    @Test
    public void testGetValueAtHighFrequency() {
        pf.addZero(new Zero(0.0, 0.0));
        double result = pf.getValue(1000.0);
        // j*1000, phase = PI/2, degrees = 90
        assertEquals(90.0, result, 1e-9);
    }
}
