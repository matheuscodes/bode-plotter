package main;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import math.Pole;
import math.Zero;

/**
 * Tests for the UI setup methods in the {@code Main} class.
 * The private static methods are exercised via reflection and the
 * button ActionListeners are fired directly to avoid needing a display.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainTest {

    private static JPanel topPanel;

    @BeforeAll
    static void initUI() throws Exception {
        topPanel = new JPanel(new BorderLayout());
        JPanel middle = new JPanel(new BorderLayout());

        Method createFP = Main.class.getDeclaredMethod(
                "createFunctionsPlot", JPanel.class, Container.class);
        createFP.setAccessible(true);
        createFP.invoke(null, middle, topPanel);

        // Ensure component bounds are set so moveViewWindow calculations work
        Main.module.setSize(200, 200);
        Main.phase.setSize(200, 200);

        Method createUM = Main.class.getDeclaredMethod("createUpperMenu", Container.class);
        createUM.setAccessible(true);
        createUM.invoke(null, topPanel);

        Method createBM = Main.class.getDeclaredMethod("createBottomMenu", Container.class);
        createBM.setAccessible(true);
        createBM.invoke(null, topPanel);
    }

    /** Recursively finds the first JButton with the given text in a container tree. */
    static JButton findButton(Container c, String text) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JButton && text.equals(((JButton) comp).getText())) {
                return (JButton) comp;
            }
            if (comp instanceof Container) {
                JButton found = findButton((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    /** Fires all registered ActionListeners on the button with the given label. */
    static void fireButton(String label) {
        JButton btn = findButton(topPanel, label);
        assertNotNull(btn, "Button '" + label + "' not found in the component tree");
        for (ActionListener al : btn.getActionListeners()) {
            al.actionPerformed(new ActionEvent(btn, ActionEvent.ACTION_PERFORMED, label));
        }
    }

    /** Reads a package-private double field from a PlottingCanvas instance via reflection. */
    static double getCanvasField(Object canvas, String name) throws Exception {
        Field f = canvas.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return (double) f.get(canvas);
    }

    // -------------------------------------------------------------------------
    // Static field initialization
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    void testStaticFieldsInitialized() {
        assertNotNull(Main.module);
        assertNotNull(Main.phase);
        assertNotNull(Main.modhjw);
        assertNotNull(Main.arghjw);
        assertNotNull(Main.listpole);
        assertNotNull(Main.listzero);
    }

    @Test
    @Order(2)
    void testInitialTransferFunctionState() {
        assertEquals(0, Main.modhjw.getPoles().size());
        assertEquals(0, Main.modhjw.getZeros().size());
        assertEquals(1.0, Main.modhjw.getConstant(), 1e-9);
    }

    // -------------------------------------------------------------------------
    // rewriteFunctionLabels – covers the 4 label-building branches
    // -------------------------------------------------------------------------

    @Test
    @Order(3)
    void testRewriteFunctionLabelsEmpty() throws Exception {
        Main.modhjw.removeAll();
        Method m = Main.class.getDeclaredMethod("rewriteFunctionLabels");
        m.setAccessible(true);
        m.invoke(null);
        assertEquals("", Main.listpole.getText());
        assertEquals("1.0", Main.listzero.getText());
    }

    @Test
    @Order(4)
    void testRewriteFunctionLabelsNullPole() throws Exception {
        Main.modhjw.removeAll();
        Main.modhjw.addPole(new Pole(0.0, 0.0)); // isNull() == true
        Method m = Main.class.getDeclaredMethod("rewriteFunctionLabels");
        m.setAccessible(true);
        m.invoke(null);
        assertTrue(Main.listpole.getText().contains("(jw)"),
                "Null pole should produce '(jw)' label, got: " + Main.listpole.getText());
        Main.modhjw.removeAll();
    }

    @Test
    @Order(5)
    void testRewriteFunctionLabelsNonNullPole() throws Exception {
        Main.modhjw.removeAll();
        Main.modhjw.addPole(new Pole(-1.0, 0.0));
        Method m = Main.class.getDeclaredMethod("rewriteFunctionLabels");
        m.setAccessible(true);
        m.invoke(null);
        String text = Main.listpole.getText();
        assertTrue(text.contains("jw") && text.contains("-"),
                "Non-null pole label should contain 'jw' and '-', got: " + text);
        Main.modhjw.removeAll();
    }

    @Test
    @Order(6)
    void testRewriteFunctionLabelsNullZero() throws Exception {
        Main.modhjw.removeAll();
        Main.modhjw.addZero(new Zero(0.0, 0.0)); // isNull() == true
        Method m = Main.class.getDeclaredMethod("rewriteFunctionLabels");
        m.setAccessible(true);
        m.invoke(null);
        assertTrue(Main.listzero.getText().contains("(jw)"),
                "Null zero should produce '(jw)' label, got: " + Main.listzero.getText());
        Main.modhjw.removeAll();
    }

    @Test
    @Order(7)
    void testRewriteFunctionLabelsNonNullZero() throws Exception {
        Main.modhjw.removeAll();
        Main.modhjw.addZero(new Zero(-1.0, 0.0));
        Method m = Main.class.getDeclaredMethod("rewriteFunctionLabels");
        m.setAccessible(true);
        m.invoke(null);
        String text = Main.listzero.getText();
        assertTrue(text.contains("jw") && text.contains("-"),
                "Non-null zero label should contain 'jw' and '-', got: " + text);
        Main.modhjw.removeAll();
    }

    // -------------------------------------------------------------------------
    // Upper-menu button listeners (Add as Zero, Add as Polo, Set Constant, Remove All)
    // -------------------------------------------------------------------------

    @Test
    @Order(8)
    void testAddAsZeroButton() {
        Main.modhjw.removeAll();
        Main.arghjw.removeAll();
        fireButton("Add as Zero");
        assertEquals(1, Main.modhjw.getZeros().size());
        assertEquals(1, Main.arghjw.getZeros().size());
    }

    @Test
    @Order(9)
    void testAddAsPoloButton() {
        Main.modhjw.removeAll();
        Main.arghjw.removeAll();
        // "Add as Polo" is the exact label used in Main.java (original text)
        fireButton("Add as Polo");
        assertEquals(1, Main.modhjw.getPoles().size());
        assertEquals(1, Main.arghjw.getPoles().size());
    }

    @Test
    @Order(10)
    void testSetConstantButton() {
        Main.modhjw.setConstant(5.0);
        fireButton("Set Constant"); // reads text field default value "1"
        assertEquals(1.0, Main.modhjw.getConstant(), 1e-9);
    }

    @Test
    @Order(11)
    void testRemoveAllButton() {
        Main.modhjw.addPole(new Pole(-1.0, 0.0));
        Main.modhjw.addZero(new Zero(-2.0, 0.0));
        Main.arghjw.addPole(new Pole(-1.0, 0.0));
        Main.arghjw.addZero(new Zero(-2.0, 0.0));
        fireButton("Remove All");
        assertTrue(Main.modhjw.getPoles().isEmpty());
        assertTrue(Main.modhjw.getZeros().isEmpty());
        assertTrue(Main.arghjw.getPoles().isEmpty());
        assertTrue(Main.arghjw.getZeros().isEmpty());
        assertEquals(1.0, Main.modhjw.getConstant(), 1e-9);
        assertEquals("", Main.listpole.getText());
        // The Remove All button sets listzero directly to "1", not via rewriteFunctionLabels
        // which would produce "1.0" from modhjw.getConstant(). Both are intentional.
        assertEquals("1", Main.listzero.getText());
    }

    // -------------------------------------------------------------------------
    // Bottom-menu module view buttons
    // -------------------------------------------------------------------------

    @Test
    @Order(12)
    void testExpandModuleButton() throws Exception {
        Main.module.setViewRange(100, -100);
        fireButton("Expand Module");
        assertEquals(90.0, getCanvasField(Main.module, "maximumy"), 1e-9);
        assertEquals(-90.0, getCanvasField(Main.module, "minimumy"), 1e-9);
    }

    @Test
    @Order(13)
    void testCompressModuleButton() throws Exception {
        Main.module.setViewRange(100, -100);
        fireButton("Compress Module");
        assertEquals(110.0, getCanvasField(Main.module, "maximumy"), 1e-9);
        assertEquals(-110.0, getCanvasField(Main.module, "minimumy"), 1e-9);
    }

    @Test
    @Order(14)
    void testRollUpModuleButton() throws Exception {
        Main.module.setViewRange(100, -100);
        Main.module.setSize(200, 200);
        double before = getCanvasField(Main.module, "maximumy");
        fireButton("Roll up Module");
        assertNotEquals(before, getCanvasField(Main.module, "maximumy"));
    }

    @Test
    @Order(15)
    void testRollDownModuleButton() throws Exception {
        Main.module.setViewRange(100, -100);
        Main.module.setSize(200, 200);
        double before = getCanvasField(Main.module, "maximumy");
        fireButton("Roll down Module");
        assertNotEquals(before, getCanvasField(Main.module, "maximumy"));
    }

    // -------------------------------------------------------------------------
    // Bottom-menu phase view buttons
    // -------------------------------------------------------------------------

    @Test
    @Order(16)
    void testExpandPhaseButton() throws Exception {
        Main.phase.setViewRange(100, -100);
        fireButton("Expand Phase");
        assertEquals(90.0, getCanvasField(Main.phase, "maximumy"), 1e-9);
        assertEquals(-90.0, getCanvasField(Main.phase, "minimumy"), 1e-9);
    }

    @Test
    @Order(17)
    void testCompressPhaseButton() throws Exception {
        Main.phase.setViewRange(100, -100);
        fireButton("Compress Phase");
        assertEquals(110.0, getCanvasField(Main.phase, "maximumy"), 1e-9);
        assertEquals(-110.0, getCanvasField(Main.phase, "minimumy"), 1e-9);
    }

    @Test
    @Order(18)
    void testRollUpPhaseButton() throws Exception {
        Main.phase.setViewRange(100, -100);
        Main.phase.setSize(200, 200);
        double before = getCanvasField(Main.phase, "maximumy");
        fireButton("Roll up Phase");
        assertNotEquals(before, getCanvasField(Main.phase, "maximumy"));
    }

    @Test
    @Order(19)
    void testRollDownPhaseButton() throws Exception {
        Main.phase.setViewRange(100, -100);
        Main.phase.setSize(200, 200);
        double before = getCanvasField(Main.phase, "maximumy");
        fireButton("Roll down Phase");
        assertNotEquals(before, getCanvasField(Main.phase, "maximumy"));
    }

    // -------------------------------------------------------------------------
    // Bottom-menu bandwidth button
    // -------------------------------------------------------------------------

    @Test
    @Order(20)
    void testSetBandwidthButton() throws Exception {
        fireButton("Set Bandwidth"); // reads text fields: min="0.1", max="1000"
        assertEquals(0.1, getCanvasField(Main.module, "minimumx"), 1e-9);
        assertEquals(1000.0, getCanvasField(Main.module, "maximumx"), 1e-9);
        assertEquals(0.1, getCanvasField(Main.phase, "minimumx"), 1e-9);
        assertEquals(1000.0, getCanvasField(Main.phase, "maximumx"), 1e-9);
    }
}
