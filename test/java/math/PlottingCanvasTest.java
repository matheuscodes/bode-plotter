package math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

/**
 * Tests for the {@code PlottingCanvas} class.
 */
public class PlottingCanvasTest {

    private PlottingCanvas canvas;

    @BeforeEach
    public void setUp() {
        canvas = new PlottingCanvas();
    }

    @Test
    public void testInit() {
        canvas.init();
        assertEquals(" ", canvas.unit);
    }

    @Test
    public void testSetUnit() {
        canvas.init();
        canvas.setUnit(" dB");
        assertEquals(" dB", canvas.unit);
    }

    @Test
    public void testSetPlotFunction() {
        TransferFunction tf = new TransferFunction();
        canvas.setPlotFunction(tf);
        assertEquals(tf, canvas.toPlot);
    }

    @Test
    public void testSetPlotFunctionNull() {
        canvas.setPlotFunction(null);
        assertNull(canvas.toPlot);
    }

    @Test
    public void testSetViewRange() {
        canvas.setViewRange(200, -200);
        assertEquals(200, canvas.maximumy, 1e-9);
        assertEquals(-200, canvas.minimumy, 1e-9);
    }

    @Test
    public void testEnlargeViewWindow() {
        canvas.setViewRange(100, -100);
        canvas.enlargeViewWindow(2.0);
        assertEquals(200, canvas.maximumy, 1e-9);
        assertEquals(-200, canvas.minimumy, 1e-9);
    }

    @Test
    public void testEnlargeViewWindowCompress() {
        canvas.setViewRange(100, -100);
        canvas.enlargeViewWindow(0.5);
        assertEquals(50, canvas.maximumy, 1e-9);
        assertEquals(-50, canvas.minimumy, 1e-9);
    }

    @Test
    public void testSetBandwidth() {
        canvas.setBandwidth(0.5, 5000);
        assertEquals(0.5, canvas.minimumx, 1e-9);
        assertEquals(5000, canvas.maximumx, 1e-9);
    }

    @Test
    public void testRenderWithNoFunction() {
        canvas.init();
        canvas.setViewRange(100, -100);
        canvas.setBandwidth(0.1, 1000);
        canvas.setSize(800, 600);
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        canvas.render(g);
        g.dispose();
    }

    @Test
    public void testRenderWithTransferFunction() {
        canvas.init();
        canvas.setViewRange(100, -100);
        canvas.setBandwidth(0.1, 1000);
        canvas.setSize(800, 600);
        TransferFunction tf = new TransferFunction();
        tf.addPole(new Pole(-1.0, 0.0));
        canvas.setPlotFunction(tf);
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        canvas.render(g);
        g.dispose();
    }

    @Test
    public void testRenderWithPhaseFunction() {
        canvas.init();
        canvas.setViewRange(90, -90);
        canvas.setBandwidth(0.1, 1000);
        canvas.setSize(800, 600);
        PhaseFunction pf = new PhaseFunction();
        pf.addZero(new Zero(0.0, 0.0));
        canvas.setPlotFunction(pf);
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        canvas.render(g);
        g.dispose();
    }
}
