/**
 *  Copyright (C) 2007 Matheus Borges Teixeira
 *  
 *  This file is part of Bode Plotter, a tool for plotting Bode graphs.
 *
 *  BodePlotter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BodePlotter is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with BodePlotter.  If not, see <http://www.gnu.org/licenses/>.
 */
package main;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math.PhaseFunction;
import math.PlottingCanvas;
import math.Pole;
import math.TransferFunction;
import math.Zero;

/**
 * The {@code Main} class controls and defines the entire software UI 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class Main
{
	/** Graph plotting the gain response in dB **/
	static PlottingCanvas module = null;
	/** Graph plotting the delay response in degrees **/
	static PlottingCanvas phase = null;
	
	/** Simple label for transfer function display of poles **/
	static JLabel listpole = null;
	/** Simple label for transfer function display of zeros **/
	static JLabel listzero = null;
	
	/** Actual mathematical gain function **/
	static TransferFunction modhjw = null;
	/** Actual mathematical delay function **/
	static PhaseFunction arghjw = null;
	
	/**
	 * Starts up the window where graphs will be plotted.
	 * 
	 * @param args is just kept for signature. Entirely ignored.
	 */
	public static void main(String[] args) {
		/** Initializing the JFrame window **/
		JFrame window = new JFrame("Bode Graph Plotter");
		window.setContentPane(new JPanel());
		window.getContentPane().setLayout(new BorderLayout());
		window.setIconImage(new ImageIcon(window.getClass().getResource("/logo.png")).getImage());
		
		/** Building bottom Copyrights **/
		JPanel copyrights = new JPanel();
		copyrights.add(new JLabel("Copyright (C) 2007 "));
		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(window.getClass().getResource("/logo.png")));
		logo.setPreferredSize(new Dimension(32,32));
		copyrights.add(logo);
		copyrights.add(new JLabel("Matheus Borges Teixeira"));
		
		/** Creating main UI screens **/
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		
		/** Variable middle serves only for graph update reference **/
		final JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());
		createUpperMenu(main);
		createFunctionsPlot(middle,main);
		createBottomMenu(main);
		
		/** Defining graph update events, only to avoid blank graphs **/
		window.addWindowFocusListener(new WindowFocusListener(){
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				middle.doLayout();
				module.repaint();
				phase.repaint();
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				middle.doLayout();
				module.repaint();
				phase.repaint();
			}
		});
		window.addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent arg0) 
			{
				Dimension available = module.getParent().getSize();
				/** Adapting the size for the new available space **/
				module.setPreferredSize(new Dimension((499*available.width)/1000,(99*available.height)/100));
				phase.setPreferredSize(new Dimension((499*available.width)/1000,(99*available.height)/100));
				middle.doLayout();
				module.repaint();
				phase.repaint();				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) 
			{
				middle.doLayout();
				module.repaint();
				phase.repaint();
			}

			@Override
			public void componentShown(ComponentEvent arg0) 
			{
				middle.doLayout();
				module.repaint();
				phase.repaint();
			}

			@Override
			public void componentHidden(ComponentEvent arg0) 
			{
				middle.doLayout();
				module.repaint();
				phase.repaint();
			}
		});
		
		/** Wrapping up JFrame window **/
		window.getContentPane().add(main,BorderLayout.CENTER);
		window.getContentPane().add(copyrights,BorderLayout.SOUTH);
		window.setBounds(0,0,1024,768);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Creates two empty functions, one for gain and one for delay.
	 * Also creates the UI for displaying the response based on frequency.
	 * 
	 * @param middle specifies the container where the graphs will be placed.
	 * @param window specifies the container where the main UI is being placed.
	 */
	private static void createFunctionsPlot(final JPanel middle, Container window) {
		modhjw = new TransferFunction();
		
		module = new PlottingCanvas();
		module.init();
		module.setUnit(" dB");
		module.setPlotFunction(modhjw);
		module.setViewRange(100, -100);
		module.setBounds(0,0,200,200);
		
		arghjw = new PhaseFunction();
		
		phase = new PlottingCanvas();
		phase.init();
		phase.setUnit("°");
		phase.setPlotFunction(arghjw);
		phase.setViewRange(100, -100);
		phase.setBounds(0,0,200,200);
		
		JPanel transfer_function = new JPanel();
		transfer_function.setLayout(new BorderLayout());
		JLabel temporary = new JLabel();
		listpole = new JLabel();
		listzero = new JLabel("1");
		listpole.setHorizontalAlignment(JLabel.CENTER);
		listzero.setHorizontalAlignment(JLabel.CENTER);
		temporary.setPreferredSize(new Dimension(0,1));
		temporary.setOpaque(true);
		temporary.setBackground(Color.BLACK);
				
		transfer_function.add(temporary,BorderLayout.CENTER);
		transfer_function.add(listpole,BorderLayout.SOUTH);
		transfer_function.add(listzero,BorderLayout.NORTH);
		
		JPanel holder = new JPanel();
		holder.add(new JLabel("H(jw)="),BorderLayout.WEST);
		holder.add(transfer_function,BorderLayout.CENTER);
		
		middle.add(module,BorderLayout.WEST);
		middle.add(phase,BorderLayout.EAST);
		middle.add(holder,BorderLayout.SOUTH);
		
		window.add(middle,BorderLayout.CENTER);		
	}

	/**
	 * Creates the bottom menu for configuring graph ranges and dimensions.
	 * 
	 * @param window specifies the container where the main UI is being placed.
	 */
	static private void createBottomMenu(final Container window)
	{	
		JPanel bottom_north = new JPanel();
		
		JButton zoominm = new JButton("Expand Module");
		zoominm.setPreferredSize(new Dimension(200,30));
		zoominm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				module.enlargeViewWindow(0.9);
				phase.repaint();
				module.repaint();
			}
			
		});
		bottom_north.add(zoominm);
		
		JButton zoomoutm = new JButton("Compress Module");
		zoomoutm.setPreferredSize(new Dimension(200,30));
		zoomoutm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				module.enlargeViewWindow(1.1);
				phase.repaint();
				module.repaint();
			}
		});
		bottom_north.add(zoomoutm);
		
		JButton rollupm = new JButton("Roll up Module");
		rollupm.setPreferredSize(new Dimension(200,30));
		rollupm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				module.moveViewWindow(-10);
				phase.repaint();
				module.repaint();
			}
		});

		JButton rolldownm = new JButton("Roll down Module");
		rolldownm.setPreferredSize(new Dimension(200,30));
		rolldownm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				module.moveViewWindow(10);
				phase.repaint();
				module.repaint();
			}
		});
		
		bottom_north.add(rollupm);
		bottom_north.add(rolldownm);
		
		
		JPanel bottom_center = new JPanel();
				
		JButton zoominp = new JButton("Expand Phase");
		zoominp.setPreferredSize(new Dimension(200,30));
		zoominp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				phase.enlargeViewWindow(0.9);
				phase.repaint();
				module.repaint();
			}
		});
		bottom_center.add(zoominp);
		
		JButton zoomoutp = new JButton("Compress Phase");
		zoomoutp.setPreferredSize(new Dimension(200,30));
		zoomoutp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				phase.enlargeViewWindow(1.1);
				phase.repaint();
				module.repaint();
			}
		});
		bottom_center.add(zoomoutp);
		
		JButton rollupp = new JButton("Roll up Phase");
		rollupp.setPreferredSize(new Dimension(200,30));
		rollupp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				phase.moveViewWindow(-10);
				phase.repaint();
				module.repaint();
			}
		});
		bottom_center.add(rollupp);
		
		
		JButton rolldownp = new JButton("Roll down Phase");
		rolldownp.setPreferredSize(new Dimension(200,30));
		rolldownp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				phase.moveViewWindow(10);
				phase.repaint();
				module.repaint();
			}
			
		});
		bottom_center.add(rolldownp);
		
		JPanel bottom_south = new JPanel();
			
		final JTextField min = new JTextField();
		final JTextField max = new JTextField();
		
		min.setText("0.1");
		max.setText("1000");
		min.setPreferredSize(new Dimension(70,30));
		max.setPreferredSize(new Dimension(70,30));
		
		bottom_south.add(new JLabel("  Min: "));
		bottom_south.add(min);
		bottom_south.add(new JLabel("  Max: "));
		bottom_south.add(max);
		
		JButton band = new JButton("Set Bandwidth");
		band.setPreferredSize(new Dimension(150,30));
		band.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Double minj = new Double(min.getText());
				Double maxj = new Double(max.getText());
				phase.setBandwidth(minj.doubleValue(),maxj.doubleValue());
				module.setBandwidth(minj.doubleValue(),maxj.doubleValue());
				phase.repaint();
				module.repaint();
			}
		});
		bottom_south.add(band);
		
		JButton help = new JButton("Help (?)");
		help.setPreferredSize(new Dimension(150,30));
		help.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JDialog helpscreen = new JDialog();
				helpscreen.setTitle("Bode Graph Plotter Help");
				helpscreen.setContentPane(new JPanel());
				ImageIcon page = new ImageIcon(helpscreen.getClass().getResource("/instructions.png"));
				JLabel display = new JLabel();
				display.setIcon(page);
				helpscreen.getContentPane().add(display);
				display.setPreferredSize(new Dimension(1067,759));
				helpscreen.setPreferredSize(new Dimension(1067,759));
				helpscreen.setSize(new Dimension(1125,800));
				helpscreen.setAlwaysOnTop(true);
				helpscreen.setVisible(true);
			}
		});
		bottom_south.add(help);
		
		
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(bottom_north,BorderLayout.NORTH);
		bottom.add(bottom_center,BorderLayout.CENTER);
		bottom.add(bottom_south,BorderLayout.SOUTH);
		
		window.add(bottom,BorderLayout.SOUTH);
	}
	
	/**
	 * Creates the top menu for adding and removing poles and zeros.
	 * 
	 * @param window specifies the container where the main UI is being placed.
	 */
	static private void createUpperMenu(Container window)
	{
		JPanel upper = new JPanel();
		
		
		final JTextField real = new JTextField();
		real.setText("0");
		real.setPreferredSize(new Dimension(70,30));
		
		final JTextField imaginary = new JTextField();
		imaginary.setText("0");
		imaginary.setPreferredSize(new Dimension(70,30));
		
		upper.add(new JLabel("  Real: "));
		upper.add(real);
		upper.add(new JLabel("  Imaginary: "));
		upper.add(imaginary);
		
		JButton z = new JButton("Add as Zero");
		z.setPreferredSize(new Dimension(150,30));
		z.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				Double re = new Double(real.getText());
				Double im = new Double(imaginary.getText());
				modhjw.addZero(new Zero(re.doubleValue(),im.doubleValue()));
				arghjw.addZero(new Zero(re.doubleValue(),im.doubleValue()));
				rewriteFunctionLabels();
				phase.repaint();
				module.repaint();
			}
		});
		upper.add(z);
		
		JButton p = new JButton("Add as Polo");
		p.setPreferredSize(new Dimension(150,30));
		p.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Double re = new Double(real.getText());
				Double im = new Double(imaginary.getText());
				modhjw.addPole(new Pole(re.doubleValue(),im.doubleValue()));
				arghjw.addPole(new Pole(re.doubleValue(),im.doubleValue()));
				rewriteFunctionLabels();
				phase.repaint();
				module.repaint();
			}
		});
		upper.add(p);
		
		
		final JTextField constant = new JTextField();
		constant.setText("1");
		constant.setPreferredSize(new Dimension(70,30));

		upper.add(new JLabel("  Constant: "));
		upper.add(constant);
				
		JButton c = new JButton("Set Constant");
		c.setPreferredSize(new Dimension(150,30));
		c.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Double cons = new Double(constant.getText());
				modhjw.setConstant(cons.doubleValue());
				rewriteFunctionLabels();
				phase.repaint();
				module.repaint();
			}
		});
		upper.add(c);
		
		JButton r = new JButton("Remove All");
		r.setPreferredSize(new Dimension(100,30));
		r.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				modhjw.removeAll();
				arghjw.removeAll();
				constant.setText("1");
				real.setText("0");
				imaginary.setText("0");
				listpole.setText("");
				listzero.setText("1");
				phase.setViewRange(100, -100);
				module.setViewRange(100, -100);
				module.getParent().doLayout();
				phase.repaint();
				module.repaint();				
			}
		});
		upper.add(r);
		
		window.add(upper,BorderLayout.NORTH);
	}
	
	/**
	 * Updates the label display of the transfer function.
	 * This is called whenever a pole/zero is added or removed.
	 */
	private static void rewriteFunctionLabels()
	{
		String polex = "";
		String zerox = ""+modhjw.getConstant();
		for(Zero z: modhjw.getZeros())
		{
			zerox += "(jw";
			if(z.isNull())
			{
				zerox += ")";
			}
			else
			{
				zerox += "- "+ z.toString() + ")";
			}
		}
		
		for(Pole p: modhjw.getPoles())
		{
			
			polex += "(jw";
			if(p.isNull())
			{
				polex += ")";
			}
			else
			{
				polex += "- "+ p.toString() + ")";
			}
		}
		
		listpole.setText(polex);
		listzero.setText(zerox);
	}

}
