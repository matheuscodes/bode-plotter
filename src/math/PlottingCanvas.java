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
package math;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import math.Function;

/**
 * The {@code PlottingCanvas} class takes care of rendering a graph based on a function. 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class PlottingCanvas extends Canvas
{
	/** Function which will be plotted **/
	Function toPlot = null;
	/** Lowest x axis value visible in the graph **/
	double minimumx = 0.1;
	/** Highest x axis value visible in the graph **/
	double maximumx = 1000;
	/** Lowest y axis value visible in the graph **/
	double minimumy = -5;
	/** Highest y axis value visible in the graph **/
	double maximumy = 5;
	/** Unit to be printed after the value **/
	String unit = null;
	/** Required default version UID **/
	private static final long serialVersionUID = 1;
	
	/**
	 * Initializes the Canvas
	 */
    public void init()
    {
    	setBackground(Color.WHITE);
    	unit = " ";
    }
    
    /**
     * Sets the unit to be printed with the Y axis values.
     * 
     * @param which defines the string to be appended.
     */
    public void setUnit(String which)
    {
    	unit = which;
    }
    
    /**
     * Sets the function which will be used for plotting.
     * 
     * @param newone specifies the function to be observed.
     */
    public void setPlotFunction(Function newone)
    {
    	toPlot = newone;
    	return;
    }
    
    /**
     * Sets the range for the graph Y axis.
     * 
     * @param max defines the lowest value for the Y axis.
     * @param min defines the highest value for the Y axis.
     */
    public void setViewRange(double max, double min)
    {
    	maximumy = max;
    	minimumy = min;
    }
    
    /**
     * Dislocates the graph Y axis by the given value.
     * Negative values move the view down.
     * Positive values move the view up.
     *  
     * @param upordown defines the value to move referent to the function scale.
     */
    public void moveViewWindow(double upordown)
    {
    	double difference = Math.abs(maximumy-minimumy);
    	
    	double sizeofoney = getBounds().getHeight()/difference;
    	
    	maximumy = maximumy + upordown/sizeofoney;
    	minimumy = minimumy + upordown/sizeofoney;
    }
    
    /**
     * Expands or Compresses the graph Y axis range.
     * 
     * @param proportion defines the value which will multiply the current range.
     */
    public void enlargeViewWindow(double proportion)
    {
    	maximumy = proportion*maximumy;
    	minimumy = proportion*minimumy;
    }
    
    /**
     * Sets the range for the graph X axis.
     * 
     * @param max defines the lowest value for the X axis.
     * @param min defines the highest value for the X axis.
     */
    public void setBandwidth(double min, double max)
    {
    	maximumx = max;
    	minimumx = min;
    }
    
    /**
     * Overrides the original Canvas method.
     */
    @Override
    public void update(Graphics g)
    {
    	Image image = createImage(getBounds().width+1, getBounds().height+1);
    	if (image != null)
    	{
	        Graphics buffer = image.getGraphics();
	        render(buffer);
        	g.drawImage(image,0,0,this);
    	}
    }

    /**
     * Renders the graph.
     * This function plots using the X axis in logarithmic scale.
     * 
     * @param g defines where the function will be plotted.
     */
    public void render(Graphics g)
    {
    	int times = (int)(Math.log10(maximumx) - Math.log10(minimumx));
    	
    	double blockofpixel = getBounds().getWidth()/times;
    	
    	double difference = Math.abs(maximumy-minimumy);
    	
    	double sizeofoney = getBounds().getHeight()/difference;
    	
    	int middle = (int)((maximumy/difference)*getBounds().getHeight()) ;
    	
    	g.setColor(Color.black);
    	
    	g.drawLine(0,0,0,getBounds().height);
    	g.drawLine(0,0,getBounds().width,0);
    	g.drawLine(0,getBounds().height-1,getBounds().width-1,getBounds().height-1);
    	g.drawLine(getBounds().width-1,0,getBounds().width-1,getBounds().height-1);
    	
    	for(double k = 0; k <= maximumy ; k = k + 1)
    	{
    		if((k % 10) == 0)
    		{
    			g.drawLine(0,(int)(middle - k*sizeofoney),10,(int)(middle - k*sizeofoney));
    			g.drawString(k+unit,10,(int)(middle-(k*sizeofoney)));
    		}
    		else
    		{
    			g.drawLine(0,(int)((middle- k*sizeofoney)),5,(int)(middle-(k*sizeofoney)));
    		}
    	}
    	
    	for(double k =0; k <= Math.abs(minimumy) ; k = k + 1)
    	{
    		if((k % 10) == 0)
    		{
    			g.drawLine(0,(int)(middle + k*sizeofoney),10,(int)(middle + k*sizeofoney));
    			g.drawString(k+unit,10,(int)(middle+(k*sizeofoney)));
    		}
    		else
    		{
    			g.drawLine(0,(int)((middle+ k*sizeofoney)),5,(int)(middle+(k*sizeofoney)));
    		}
    	}
    	g.drawLine(0,middle,(int)getBounds().getWidth(),middle);
    	for(int j = 0; j < times; j++ )
    	{
    		g.drawString(""+ (j+(int)Math.log10(minimumx)),(int)(j*blockofpixel),middle+25);
    		for(int i = 1; i < 10; i++)
	    	{
	    		g.drawLine((int)(Math.log10(i)*blockofpixel + j*blockofpixel),middle,(int)(Math.log10(i)*blockofpixel + j*blockofpixel),middle+5);
	    	}
    		g.drawLine((int)(blockofpixel + j*blockofpixel),middle,(int)(blockofpixel + j*blockofpixel),middle+10);
    	}
    	
    	
    	if(toPlot != null)
    	{
	    	g.setColor(Color.BLUE);
	    	for(int j = 0; j < times; j++ )
	    	{
	    		double w = Math.pow(10,j+(int)Math.log10(minimumx));
	    		for(double i = 1; i < 10; i= i+0.1)
		    	{
	    			g.drawLine((int)(Math.log10(i)*blockofpixel + j*blockofpixel),middle-(int)(toPlot.getValue(w*i)*sizeofoney),(int)(Math.log10(i+0.1)*blockofpixel + j*blockofpixel),middle-(int)(toPlot.getValue(w*(i+0.1))*sizeofoney));
		    	}
	    	}
    	}
    }
}
