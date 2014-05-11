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
 * The {@code PhaseFunction} class computes the phase component of the Bode Plot.
 * It uses all individual Zeros and Poles influences. 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class PhaseFunction extends Function
{
	/**
	 * Calculates the phase of the transfer function for the given frequency.
	 * The result is present in degree scale. 
	 * 
	 * @param x specifies the frequency to be used in rad/s
	 * @return value of the phase summing up all influences in degrees.
	 */
	public double getValue(double x) 
	{
		double result = 0;
		for(Pole p: poles)
		{
			result = result + p.arcValue(x);
		}
		for(Zero z: zeros)
		{
			result = result + z.arcValue(x);
		}		

		return result*360/(2*Math.PI);
	}
}
