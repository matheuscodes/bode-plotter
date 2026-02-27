package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@code Pole} class.
 */
public class PoleTest {

    @Test
    public void testConstructor() {
        Pole p = new Pole(2.0, 3.0);
        assertEquals(2.0, p.getReal(), 1e-9);
        assertEquals(3.0, p.getImaginary(), 1e-9);
    }

    @Test
    public void testIsNull() {
        Pole p = new Pole(0.0, 0.0);
        assertTrue(p.isNull());
    }

    @Test
    public void testIsNotNull() {
        Pole p = new Pole(1.0, 0.0);
        assertFalse(p.isNull());
    }

    @Test
    public void testDBValueAtNonZeroFrequency() {
        Pole p = new Pole(-1.0, 0.0);
        double result = p.dBValue(1.0);
        // jw = j*1, jw - pole = j*1 - (-1) = 1 + j, module = sqrt(2)
        // 1/sqrt(2) in dB = -20*log10(sqrt(2)) = -10*log10(2)
        double expected = 20 * Math.log10(1.0 / Math.sqrt(2.0));
        assertEquals(expected, result, 1e-9);
    }

    @Test
    public void testDBValueAtHighFrequency() {
        Pole p = new Pole(0.0, 0.0);
        double result = p.dBValue(100.0);
        // jw = j*100, jw - 0 = j*100, module = 100
        // 1/100 in dB = -40 dB
        assertEquals(-40.0, result, 1e-6);
    }

    @Test
    public void testArcValueAtNonZeroFrequency() {
        Pole p = new Pole(-1.0, 0.0);
        double result = p.arcValue(1.0);
        // 1/(1+j) has phase = -atan(1) = -PI/4
        assertNotNull(result);
        assertEquals(-Math.PI / 4, result, 1e-9);
    }

    @Test
    public void testArcValueAtZeroFrequency() {
        Pole p = new Pole(-1.0, 0.0);
        double result = p.arcValue(0.0);
        // jw = 0, jw - (-1) = 1+0j, 1/1 has phase 0
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testToString() {
        Pole p = new Pole(2.0, 3.0);
        String s = p.toString();
        assertTrue(s.contains("2.0"));
    }
}
