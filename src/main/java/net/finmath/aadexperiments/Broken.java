/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 24 Jan 2020
 */

package net.finmath.aadexperiments;

import net.finmath.aadexperiments.value.Value;
import net.finmath.aadexperiments.value.ValueDoubleDifferentiable;
import net.finmath.montecarlo.automaticdifferentiation.RandomVariableDifferentiableFactory;
import net.finmath.montecarlo.automaticdifferentiation.backward.RandomVariableDifferentiableAADFactory;

/**
 * @author Christian Fries
 *
 */
public class Broken {

	public static void main( String[] args )
	{
		RandomVariableDifferentiableFactory randomVariableFactory = new RandomVariableDifferentiableAADFactory();

		// Create an input value
		var x = new ValueDoubleDifferentiable(1.0);

		// Perform successive calculations
		Value x1 = x.mult(new ValueDoubleDifferentiable(2.0));
		Value x2 = x1.mult(new ValueDoubleDifferentiable(4.0));

		// Extract the value of x2
		Double x2AsFloatingPoint = x2.asFloatingPoint();
		
		// Create a new constant
		Value x2Copy = new ValueDoubleDifferentiable(x2AsFloatingPoint);

		// Perform a calculation with x1 and a copy of x2
		Value yBroken = x1.add(x2Copy);

		// Alternatively, do the same calculation with x1 and x2
		Value y = x1.add(x2);

		// The two values are the same:
		
		System.out.println("Value ............................: " + y);
		System.out.println("Value (broken tree) ..............: " + yBroken);
		System.out.println();
		
		// The algorithmic differentiation differs:
		
		Value dydx			= ((ValueDoubleDifferentiable)y).getDerivativeWithRespectTo(x);
		Value dydxBroken	= ((ValueDoubleDifferentiable)yBroken).getDerivativeWithRespectTo(x);

		System.out.println("Derivative dy/dx .................: " + dydx);
		System.out.println("Derivative dy/dx (broken tree) ...: " + dydxBroken);
			
	}
}
