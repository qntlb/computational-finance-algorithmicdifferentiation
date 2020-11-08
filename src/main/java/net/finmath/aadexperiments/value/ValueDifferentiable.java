package net.finmath.aadexperiments.value;

public interface ValueDifferentiable extends Value {

	/**
	 * Returns the floating point value of the derivative of this object z with respect to the argument object x.
	 * 
	 * @return New object representing the result.
	 */
	Value getDerivativeWithRespectTo(ValueDifferentiable x);

}