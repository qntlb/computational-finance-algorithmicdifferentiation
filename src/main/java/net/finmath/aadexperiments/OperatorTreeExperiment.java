package net.finmath.aadexperiments;

public class OperatorTreeExperiment {

	/**
	 * This trivial program is used to illustrate the dependency of x3 on x1 and x2, where x1 is an independent (input), but x2 is a dependent variable.
	 * Here it is: x3 = x1-x2 and x2 = x1. Hence it is x3 = 0 independent of x1.
	 * Question: What is dx3/dx1 and dx3/dx2?
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {

		double x1 = 10.0;

		double x2 = x1;

		double x3 = x1 - x2;

		System.out.println(x3);
	}
}
