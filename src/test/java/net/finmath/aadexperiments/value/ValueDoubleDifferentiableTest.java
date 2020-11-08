package net.finmath.aadexperiments.value;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class ValueDoubleDifferentiableTest {

	@Test
	void testHypotenuse1() {
		BiFunction<Value,Value,Value> hypotenuse = (a,b) -> (a.squared().add(b.squared())).sqrt();

		ValueDifferentiable a = new ValueDoubleDifferentiable(3.0);
		ValueDifferentiable b = new ValueDoubleDifferentiable(4.0);
		
		ValueDifferentiable c = (ValueDifferentiable) hypotenuse.apply(a, b);
		
		Value derivativeAnalytic1 = a.div(c);
		Value derivativeAlgorithmic1 = c.getDerivativeWithRespectTo(a);
		assertEquals(derivativeAnalytic1.asFloatingPoint(), derivativeAlgorithmic1.asFloatingPoint(), 1E-15, "partial derivative dz/dx");

		Value derivativeAnalytic2 = b.div(c);
		Value derivativeAlgorithmic2 = c.getDerivativeWithRespectTo(b);
		assertEquals(derivativeAnalytic2.asFloatingPoint(), derivativeAlgorithmic2.asFloatingPoint(), 1E-15, "partial derivative dz/dx");
	}

	@Test
	void testHypotenuse2() {
		BiFunction<Value,Value,Value> hypotenuse = (a,b) -> (a.squared().add(b.squared())).sqrt();

		ValueDifferentiable a = new ValueDoubleDifferentiable(4.0);
		ValueDifferentiable b = new ValueDoubleDifferentiable(7.0);
		
		ValueDifferentiable c = (ValueDifferentiable) hypotenuse.apply(a, b);
		
		Value derivativeAnalytic1 = a.div(c);
		Value derivativeAlgorithmic1 = c.getDerivativeWithRespectTo(a);
		assertEquals(derivativeAnalytic1.asFloatingPoint(), derivativeAlgorithmic1.asFloatingPoint(), 1E-15, "partial derivative dz/dx");

		Value derivativeAnalytic2 = b.div(c);
		Value derivativeAlgorithmic2 = c.getDerivativeWithRespectTo(b);
		assertEquals(derivativeAnalytic2.asFloatingPoint(), derivativeAlgorithmic2.asFloatingPoint(), 1E-15, "partial derivative dz/dx");
	}

	/**
	 * This test is sensitive to the fact that the tree is processed in the right order (descending by id).
	 */
	@Test
	void testOder() {
		Function<Value, Value> function = x -> {
			Value x1 = x.mult(new ValueDoubleDifferentiable(2.0));
			Value x2 = x1.mult(new ValueDoubleDifferentiable(4.0));
			Value x3 = x1.add(x2);
			
			return x3;
		};

		ValueDifferentiable x = new ValueDoubleDifferentiable(1.0);
		ValueDifferentiable y = (ValueDifferentiable) function.apply(x);
		
		Double derivativeAnalytic = 10.0;
		Value derivativeAlgorithmic = y.getDerivativeWithRespectTo(x);
		assertEquals(derivativeAnalytic, derivativeAlgorithmic.asFloatingPoint(), 1E-15, "partial derivative dy/dx");
	}
}
