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
 * The {@code Complex} class models a complex number. 
 * 
 * @author Matheus Borges Teixeira
 * @version 1.0
 */
public class Complex
{

	/** Imaginary part of the complex number **/
	double imaginary = 0;
	/** Real part of the complex number **/
	double real = 0;
	
	/**
	 * Default constructor.
	 * Needs both complex number parts to be specified. 
	 * 
	 * @param real_part defines the real argument.
	 * @param imaginary_part defines the imaginary argument.
	 */
	public Complex(double real_part, double imaginary_part)
	{
		real = real_part;
		imaginary = imaginary_part;
	}
	
	/**
	 * Returns the module of the complex number for polar coordinates.
	 * 
	 * @return value of the module.
	 */
	public double getModule()
	{
		return Math.sqrt(real*real+imaginary*imaginary);
	}
	
	/**
	 * Returns the phase of the complex number for polar coordinates.
	 *  
	 * @return value of the phase.
	 */
	public double getPhase()
	{
		if(real != 0)
		{
			return Math.atan(imaginary/real);
		}
		else
		{
			if(imaginary >= 0)
			{
				return Math.PI/2;
			}
			else{
				return -Math.PI/2;
			}
		}
	}
	
	/**
	 * Returns the real component of the complex number.
	 * 
	 * @return value of the real component.
	 */
	public double getReal()
	{
		return real;
	}
	
	/**
	 * Returns the imaginary component of the complex number.
	 * 
	 * @return value of the imaginary component.
	 */
	public double getImaginary()
	{
		return imaginary;
	}
	
	/**
	 * Performs the addition operation.
	 * 
	 * @param whole defines another complex number to be added to the current.
	 * @return a new complex number with the operation result.  
	 */
	public Complex add(Complex whole)
	{
		double RE = real + whole.getReal();
		double IM = imaginary + whole.getImaginary();
		return new Complex(RE,IM);
	}
	
	/**
	 * Performs the subtraction operation.
	 * 
	 * @param whole defines another complex number to be subtracted from the current.
	 * @return a new complex number with the operation result.  
	 */
	public Complex minus(Complex whole)
	{
		double RE = real - whole.getReal();
		double IM = imaginary - whole.getImaginary();
		return new Complex(RE,IM);
	}
	
	/**
	 * Performs the multiplication operation.
	 * 
	 * @param whole defines another complex number to be multiplied to the current.
	 * @return a new complex number with the operation result.
	 */
	public Complex multiply(Complex multiplier)
	{
		double RE = real*multiplier.getReal() - imaginary*multiplier.getImaginary();
		double IM = real*multiplier.getImaginary() + imaginary*multiplier.getReal();
		return new Complex(RE,IM);
	}
	
	/**
	 * Performs the division operation.
	 * 
	 * @param whole defines another complex number to divide the current.
	 * @return a new complex number with the operation result.  
	 */
	public Complex divide(Complex denominator)
	{
		double thetaline = getPhase() - denominator.getPhase();
		double radius = getModule()/denominator.getModule();
		return new Complex(Math.cos(thetaline)*radius,Math.sin(thetaline)*radius);
	}
	
	/**
	 * Verifies if the current complex number is equals to 0.
	 * 
	 * @return whether the complex number is equals to 0 or not.
	 */
	public boolean isNull()
	{
		return (imaginary == 0 && real == 0);
	}
	
	/**
	 * Serializes the object in the form of a string.
	 */
	public String toString()
	{
		String result = "";
		if(real != 0)
		{
			result += "("+real;
		}
		if(imaginary != 0)
		{
			if(result.length() <= 0)
			{
				result += "(";
			}
			else{
				result += " ";
			}
			if(imaginary < 0)
			{
				result += imaginary + "j)";
			}
			else
			{
				if(real != 0)
				{
					result += "+ " + imaginary + "j)";
				}
				else
				{
					result += imaginary + "j)";
				}
			}
		}
		else
		{
			if(result.length() > 0)
			{
				result += ")";
			}
		}
		return result;
	}
}
