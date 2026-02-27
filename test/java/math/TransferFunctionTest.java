package math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@code TransferFunction} class.
 */
public class TransferFunctionTest {

    private TransferFunction tf;

    @BeforeEach
    public void setUp() {
        tf = new TransferFunction();
    }

    @Test
    public void testDefaultConstantIsOne() {
        assertEquals(1.0, tf.getConstant(), 1e-9);
    }

    @Test
    public void testSetAndGetConstant() {
        tf.setConstant(5.0);
        assertEquals(5.0, tf.getConstant(), 1e-9);
    }

    @Test
    public void testGetValueWithNoPolesOrZeros() {
        // With constant = 1 and no poles/zeros, value should be 0 dB
        assertEquals(0.0, tf.getValue(1.0), 1e-9);
    }

    @Test
    public void testGetValueWithConstant() {
        tf.setConstant(10.0);
        // 20*log10(10) = 20 dB
        assertEquals(20.0, tf.getValue(1.0), 1e-9);
    }

    @Test
    public void testGetValueWithPole() {
        tf.addPole(new Pole(0.0, 0.0));
        double result = tf.getValue(10.0);
        // pole at origin at freq=10: 1/(j*10) has module 0.1, dB = -20
        assertEquals(-20.0, result, 1e-6);
    }

    @Test
    public void testGetValueWithZero() {
        tf.addZero(new Zero(0.0, 0.0));
        double result = tf.getValue(10.0);
        // zero at origin at freq=10: j*10 has module 10, dB = 20
        assertEquals(20.0, result, 1e-6);
    }

    @Test
    public void testGetValueWithPoleAndZero() {
        tf.addPole(new Pole(0.0, 0.0));
        tf.addZero(new Zero(0.0, 0.0));
        double result = tf.getValue(10.0);
        // They cancel out: 0 dB
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testAddPoleAndGetPoles() {
        Pole p = new Pole(-1.0, 0.0);
        tf.addPole(p);
        assertEquals(1, tf.getPoles().size());
        assertEquals(p, tf.getPoles().get(0));
    }

    @Test
    public void testAddZeroAndGetZeros() {
        Zero z = new Zero(-2.0, 0.0);
        tf.addZero(z);
        assertEquals(1, tf.getZeros().size());
        assertEquals(z, tf.getZeros().get(0));
    }

    @Test
    public void testRemoveAll() {
        tf.setConstant(5.0);
        tf.addPole(new Pole(-1.0, 0.0));
        tf.addZero(new Zero(-2.0, 0.0));
        tf.removeAll();
        assertEquals(1.0, tf.getConstant(), 1e-9);
        assertTrue(tf.getPoles().isEmpty());
        assertTrue(tf.getZeros().isEmpty());
    }

    @Test
    public void testGetValueWithMultiplePoles() {
        tf.addPole(new Pole(0.0, 0.0));
        tf.addPole(new Pole(0.0, 0.0));
        double result = tf.getValue(10.0);
        // Two poles at origin: -40 dB at w=10
        assertEquals(-40.0, result, 1e-6);
    }
}
