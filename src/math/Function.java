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

import java.util.Vector;


/**
 * The {@code Function} class models a transfer function.
 * A Transfer function is the product of all distances to zeros 
 * divided by the product of the distances of all poles.
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public abstract class Function
{
	/** Values that make the denominator of the function equals to zero **/
	Vector<Pole> poles = null;
	/** Values that make the function equals to zero **/
	Vector<Zero> zeros = null;
	
	/**
	 * Default constructor defining an empty transfer function.
	 */
	public Function()
	{
		poles = new Vector<Pole>();
		zeros = new Vector<Zero>();
	}

	/**
	 * Gets all zeros of the transfer function.
	 * 
	 * @return all zeros.
	 */
	public Vector<Zero> getZeros()
	{
		return zeros;
	}
	
	/**
	 * Gets all poles of the transfer function.
	 * 
	 * @return all poles.
	 */
	public Vector<Pole> getPoles()
	{
		return poles;
	}
	
	/**
	 * Adds a pole to the transfer function.
	 * 
	 * @param newone specifies the pole to be added.
	 */
	public void addPole(Pole newone)
	{
		poles.add(newone);
		return;
	}
	
	/**
	 * Adds a zero to the transfer function.
	 * 
	 * @param newone specifies the zero to be added.
	 */
	public void addZero(Zero newone)
	{
		zeros.add(newone);
		return;
	}

	/**
	 * Resets the transfer function.
	 */
	public void removeAll()
	{
		zeros.removeAllElements();
		poles.removeAllElements();
	}
	
	abstract public double getValue(double x);
}
