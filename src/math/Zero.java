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
 * The {@code Zero} class models the individual influence of a zero in a transfer function. 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class Zero extends Complex
{
	/**
	 * Default constructor specifying the complex parts of the pole.
	 * 
	 * @param real defines the real part of the pole.
	 * @param imaginary defines the imaginary part of the pole.
	 */
	public Zero(double real, double imaginary) {
		super(real, imaginary);
	}

	/**
	 * Calculates the influence of this zero in the module of the transfer function.
	 * The value is returned in dB scale.
	 * 
	 * @param x specifies the frequency to be considered. 
	 * @return value of the module for the given frequency in dB.
	 */
	public double dBValue(double x) {
		Complex jw = new Complex(0,x);
		Complex result = jw.minus(this);
		return 20*Math.log10(result.getModule());
	}
	
	/**
	 * Calculates the influence of this zero in the phase of the transfer function.
	 * The value is returned in rad scale.
	 * 
	 * @param x specifies the frequency to be considered. 
	 * @return value of the phase for the given frequency in rads.
	 */
	public double arcValue(double x) {
		Complex jw = new Complex(0,x);
		Complex result = jw.minus(this);
		return result.getPhase();
	}
	
}
