package net.finmath.aadexperiments;

import net.finmath.aadexperiments.value.Value;
import net.finmath.aadexperiments.value.ValueDoubleDifferentiable;

public class DiffernetiationExperimentHypothenuse {

	public static void main(String[] args) {
		
		/*
		 * Note: The behavior of the program will change, depending on
		 * which implementation is used for Value. Use either ValueDouble or ValueDoubleDifferentiable.
		 */
		Value a = new ValueDoubleDifferentiable(7.0);
		Value b = new ValueDoubleDifferentiable(24.0);
		Value epsilon = new ValueDoubleDifferentiable(1E-5);

		System.out.println("Results for a = " + a + " and b = " + b);
		System.out.println();
		
		/*
		 * Evaluation of function
		 */
		Value c = hypotenuse(a,b);
		
		System.out.println("Value is c = ............................: " + c);
		System.out.println("The type of c is ........................: " + c.getClass().getSimpleName());
		System.out.println();
		

		/*
		 * Partial derivative dz/dx
		 */
		Value dzdxAnalytic = a.div(c);
		System.out.println("Derivative (analytic)....................: " + dzdxAnalytic);
		System.out.println();

		
		/*
		 * Partial derivative dz/dx by finite difference
		 */
		Value dzdxFinitedifferce = (hypotenuse(a.add(epsilon), b).sub(hypotenuse(a, b)).div(epsilon));
		System.out.println("Derivative (finite difference)...........: " + dzdxFinitedifferce);
		System.out.println();
	
	
		Value dzdxAlgorithmicDifferentiation = ((ValueDoubleDifferentiable)c).getDerivativeWithRespectTo((ValueDoubleDifferentiable)a);
		System.out.println("Derivative (automatic differentiation)...: " + dzdxAlgorithmicDifferentiation);
	}

	private static Value hypotenuse(Value a, Value b) {
		Value c = (a.squared().add(b.squared())).sqrt();
		return c;
	}
}
