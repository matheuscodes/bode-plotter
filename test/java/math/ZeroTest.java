package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@code Zero} class.
 */
public class ZeroTest {

    @Test
    public void testConstructor() {
        Zero z = new Zero(2.0, 3.0);
        assertEquals(2.0, z.getReal(), 1e-9);
        assertEquals(3.0, z.getImaginary(), 1e-9);
    }

    @Test
    public void testIsNull() {
        Zero z = new Zero(0.0, 0.0);
        assertTrue(z.isNull());
    }

    @Test
    public void testIsNotNull() {
        Zero z = new Zero(1.0, 0.0);
        assertFalse(z.isNull());
    }

    @Test
    public void testDBValueAtNonZeroFrequency() {
        Zero z = new Zero(-1.0, 0.0);
        double result = z.dBValue(1.0);
        // jw = j*1, jw - zero = j*1 - (-1) = 1 + j, module = sqrt(2)
        double expected = 20 * Math.log10(Math.sqrt(2.0));
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testDBValueAtOriginZero() {
        Zero z = new Zero(0.0, 0.0);
        double result = z.dBValue(10.0);
        // jw = j*10, jw - 0 = j*10, module = 10
        assertEquals(20.0, result, 1e-6);
    }

    @Test
    public void testArcValueAtNonZeroFrequency() {
        Zero z = new Zero(-1.0, 0.0);
        double result = z.arcValue(1.0);
        // jw - (-1) = 1 + j, phase = atan(1/1) = PI/4
        assertEquals(Math.PI / 4, result, 1e-9);
    }

    @Test
    public void testArcValueAtZeroFrequency() {
        Zero z = new Zero(-1.0, 0.0);
        double result = z.arcValue(0.0);
        // jw = 0, jw - (-1) = 1, phase = 0
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testToString() {
        Zero z = new Zero(2.0, 3.0);
        String s = z.toString();
        assertTrue(s.contains("2.0"));
    }
}
