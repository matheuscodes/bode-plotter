package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@code Complex} class.
 */
public class ComplexTest {

    @Test
    public void testConstructorAndGetters() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(3.0, c.getReal(), 1e-9);
        assertEquals(4.0, c.getImaginary(), 1e-9);
    }

    @Test
    public void testGetModule() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.getModule(), 1e-9);
    }

    @Test
    public void testGetModuleZero() {
        Complex c = new Complex(0.0, 0.0);
        assertEquals(0.0, c.getModule(), 1e-9);
    }

    @Test
    public void testGetPhaseWithNonZeroReal() {
        Complex c = new Complex(1.0, 1.0);
        assertEquals(Math.atan(1.0), c.getPhase(), 1e-9);
    }

    @Test
    public void testGetPhaseWithZeroRealPositiveImaginary() {
        Complex c = new Complex(0.0, 5.0);
        assertEquals(Math.PI / 2, c.getPhase(), 1e-9);
    }

    @Test
    public void testGetPhaseWithZeroRealZeroImaginary() {
        Complex c = new Complex(0.0, 0.0);
        assertEquals(Math.PI / 2, c.getPhase(), 1e-9);
    }

    @Test
    public void testGetPhaseWithZeroRealNegativeImaginary() {
        Complex c = new Complex(0.0, -3.0);
        assertEquals(-Math.PI / 2, c.getPhase(), 1e-9);
    }

    @Test
    public void testAdd() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = a.add(b);
        assertEquals(4.0, result.getReal(), 1e-9);
        assertEquals(6.0, result.getImaginary(), 1e-9);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(5.0, 7.0);
        Complex b = new Complex(3.0, 4.0);
        Complex result = a.minus(b);
        assertEquals(2.0, result.getReal(), 1e-9);
        assertEquals(3.0, result.getImaginary(), 1e-9);
    }

    @Test
    public void testMultiply() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        // (1+2j)(3+4j) = 3-8 + (4+6)j = -5+10j
        Complex result = a.multiply(b);
        assertEquals(-5.0, result.getReal(), 1e-9);
        assertEquals(10.0, result.getImaginary(), 1e-9);
    }

    @Test
    public void testDivide() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 0.0);
        Complex result = a.divide(b);
        assertEquals(5.0, result.getModule(), 1e-9);
    }

    @Test
    public void testDivideByImaginary() {
        Complex a = new Complex(0.0, 4.0);
        Complex b = new Complex(0.0, 2.0);
        Complex result = a.divide(b);
        assertEquals(2.0, result.getModule(), 1e-9);
    }

    @Test
    public void testIsNullTrue() {
        Complex c = new Complex(0.0, 0.0);
        assertTrue(c.isNull());
    }

    @Test
    public void testIsNullFalseWithReal() {
        Complex c = new Complex(1.0, 0.0);
        assertFalse(c.isNull());
    }

    @Test
    public void testIsNullFalseWithImaginary() {
        Complex c = new Complex(0.0, 1.0);
        assertFalse(c.isNull());
    }

    @Test
    public void testToStringBothPartsPositive() {
        Complex c = new Complex(3.0, 4.0);
        String s = c.toString();
        assertTrue(s.contains("3.0"));
        assertTrue(s.contains("4.0"));
        assertTrue(s.contains("+"));
    }

    @Test
    public void testToStringBothPartsNegativeImaginary() {
        Complex c = new Complex(3.0, -4.0);
        String s = c.toString();
        assertTrue(s.contains("3.0"));
        assertTrue(s.contains("-4.0"));
    }

    @Test
    public void testToStringRealOnly() {
        Complex c = new Complex(5.0, 0.0);
        String s = c.toString();
        assertTrue(s.contains("5.0"));
        assertFalse(s.contains("j"));
    }

    @Test
    public void testToStringImaginaryOnlyPositive() {
        Complex c = new Complex(0.0, 3.0);
        String s = c.toString();
        assertTrue(s.contains("3.0"));
        assertTrue(s.contains("j"));
    }

    @Test
    public void testToStringImaginaryOnlyNegative() {
        Complex c = new Complex(0.0, -3.0);
        String s = c.toString();
        assertTrue(s.contains("-3.0"));
        assertTrue(s.contains("j"));
    }

    @Test
    public void testToStringZero() {
        Complex c = new Complex(0.0, 0.0);
        assertEquals("", c.toString());
    }
}
