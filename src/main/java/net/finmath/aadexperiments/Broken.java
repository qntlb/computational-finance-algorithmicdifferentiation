/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 24 Jan 2020
 */

package net.finmath.aadexperiments;

import net.finmath.aadexperiments.value.ConvertableToFloatingPoint;
import net.finmath.aadexperiments.value.Value;
import net.finmath.aadexperiments.value.ValueDoubleDifferentiable;

/**
 * @author Christian Fries
 *
 */
public class Broken {

	public static void main( String[] args )
	{
		// Create an input value
		var x0 = new ValueDoubleDifferentiable(1.0);
		var x1 = new ValueDoubleDifferentiable(2.0);

		Value y = performCalculation(x0, x1);
		Value dydx0	= ((ValueDoubleDifferentiable)y).getDerivativeWithRespectTo(x0);

		System.out.println("Value ............................: " + y);
		System.out.println("Derivative dy/dx .................: " + dydx0);

		System.out.println();

		// The algorithmic differentiation differs:

		Value yBroken = performCalculationBroken(x0,x1);
		Value dydx0Broken	= ((ValueDoubleDifferentiable)yBroken).getDerivativeWithRespectTo(x0);

		System.out.println("Value (broken tree) ..............: " + yBroken);
		System.out.println("Derivative dy/dx (broken tree) ...: " + dydx0Broken);
	}

	/**
	 * Calculates y = x0 - x1 * x0.
	 *
	 * @param x The argument x.
	 * @return The value y.
	 */
	public static Value performCalculation(Value x0, Value x1) {
		// Perform a calculation
		Value x2 = x0.mult(x1);

		// Calculate the difference of x0 and x2
		Value y = x0.sub(x2);

		return y;
	}

	/**
	 * Calculates y = x0 - x1 * x0.
	 * Using a copy of x2 = x1 * x0 during the calculation tree.
	 *
	 * @param x The argument x.
	 * @return The value y.
	 */
	public static Value performCalculationBroken(Value x0, Value x1) {
		// Perform a calculation
		Value x2 = x0.mult(x1);

		// Extract the value of x2
		Double x2AsFloatingPoint = ((ConvertableToFloatingPoint)x2).asFloatingPoint();

		// Create a new constant from the value of x2
		Value x2Copy = new ValueDoubleDifferentiable(x2AsFloatingPoint);

		// Calculate the difference of x0 and x2
		Value y = x0.sub(x2Copy);

		return y;
	}
}
