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


/**
 * The {@code TransferFunction} class computes the module component of the Bode Plot.
 * It uses all individual Zeros and Poles influences plus a defined constant. 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class TransferFunction extends Function
{
	/** Constant gain in all frequencies **/
	double constant = 1;
	
	/**
	 * Defines the constant gain.
	 * 
	 * @param value specifies the gain.
	 */
	public void setConstant(double value)
	{
		constant = value;
	}
	
	/**
	 * Gets the constant gain.
	 * 
	 * @return constant gain.
	 */
	public double getConstant()
	{
		return constant;
	}
	
	/**
	 * Calculates the module of the transfer function for the given frequency.
	 * The result is present in dB scale. 
	 * 
	 * @param x specifies the frequency to be used in rad/s
	 * @return value of the phase summing up all influences in dBs.
	 */
	public double getValue(double x) 
	{
		double result = 0;
		for(Pole p: poles)
		{
			result = result + p.dBValue(x);
		}
		for(Zero z: zeros)
		{
			result = result + z.dBValue(x);
		}
		
		result += 20*Math.log10(constant);
		return result;
	}
	
	/** Needs to be overridden to handle the constant **/
	@Override
	public void removeAll()
	{
		super.removeAll();
		constant = 1;
	}
}
