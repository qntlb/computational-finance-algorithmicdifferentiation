package net.finmath.aadexperiments.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ValueDoubleDifferentiable implements ValueDifferentiable {

	private enum Operator {
		SQUARED, SQRT, ADD, SUB, MULT, DIV
	}
	
	private static AtomicLong nextId = new AtomicLong();

	private Double value;
	private Operator operator;
	private List<ValueDoubleDifferentiable> arguments;
	private Long id;

	/**
	 * Creates a node from an operation.
	 * 
	 * @param value Value of this node.
	 * @param operator Operator that lead to this value.
	 * @param arguments Arguments that were used in this operation.
	 */
	private ValueDoubleDifferentiable(Double value, Operator operator, List<ValueDoubleDifferentiable> arguments) {
		super();
		this.value = value;
		this.operator = operator;
		this.arguments = arguments;
		this.id = nextId.getAndIncrement();
	}

	/**
	 * Creates a node from a constant - a leaf node.
	 * 
	 * @param value Value of this node.
	 */
	public ValueDoubleDifferentiable(Double value) {
		this(value, null, null);
	}

	/*
	 * The operations, implementing the interface.
	 */

	@Override
	public Value squared() {
		return new ValueDoubleDifferentiable(value * value, Operator.SQUARED, List.of(this));
	}

	@Override
	public Value sqrt() {
		return new ValueDoubleDifferentiable(Math.sqrt(value), Operator.SQRT, List.of(this));
	}

	@Override
	public Value add(Value x) {
		return new ValueDoubleDifferentiable(value + x.asFloatingPoint(), Operator.ADD, List.of(this, (ValueDoubleDifferentiable)x));
	}

	@Override
	public Value sub(Value x) {
		return new ValueDoubleDifferentiable(value - x.asFloatingPoint(), Operator.SUB, List.of(this, (ValueDoubleDifferentiable)x));
	}

	@Override
	public Value mult(Value x) {
		return new ValueDoubleDifferentiable(value * x.asFloatingPoint(), Operator.MULT, List.of(this, (ValueDoubleDifferentiable)x));
	}

	@Override
	public Value div(Value x) {
		return new ValueDoubleDifferentiable(value / x.asFloatingPoint(), Operator.DIV, List.of(this, (ValueDoubleDifferentiable)x));
	}

	@Override
	public Double asFloatingPoint() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * Get the derivatives of a node with respect to all input nodes via a backward algorithmic differentiation (adjoint differentiation).
	 * 
	 * @return A map x -> D which gives D = dy/dx, where y is this node and x is any input node.
	 */
	public Map<ValueDifferentiable, Double> getDerivativeWithRespectTo() {

		Map<ValueDifferentiable, Double> derivativesWithRespectTo = new HashMap<>();
		// Init with dy / dy = 1
		derivativesWithRespectTo.put(this, 1.0);

		// This creates a set (queue) of objects, sorted ascending by their getID() value
		PriorityQueue<ValueDoubleDifferentiable> nodesToProcess = new PriorityQueue<>((o1,o2) -> -o1.getID().compareTo(o2.getID()));

		// Add the root note
		nodesToProcess.add(this);

		// Walk down the tree, always removing the node with the highest id and adding their arguments
		while(!nodesToProcess.isEmpty()) {

			// Get and remove the top most node.
			ValueDoubleDifferentiable currentNode = nodesToProcess.poll();

			List<ValueDoubleDifferentiable> currentNodeArguments = currentNode.getArguments();
			if(currentNodeArguments != null) {
				// If node derivative has been calculated yet for an argument node, initialize it to 0.0
				for(ValueDoubleDifferentiable argument : currentNodeArguments) if(!derivativesWithRespectTo.containsKey(argument)) derivativesWithRespectTo.put(argument, 0.0);

				// Update the derivative as Di = Di + Dm * dxm / dxi (where Dm = dy/xm).
				switch(currentNode.getOperator()) {
				case ADD:
					derivativesWithRespectTo.put(currentNodeArguments.get(0), derivativesWithRespectTo.get(currentNodeArguments.get(0)) + derivativesWithRespectTo.get(currentNode) * 1.0);
					derivativesWithRespectTo.put(currentNodeArguments.get(1), derivativesWithRespectTo.get(currentNodeArguments.get(1)) + derivativesWithRespectTo.get(currentNode) * 1.0);
					break;
				case SUB:
					derivativesWithRespectTo.put(currentNodeArguments.get(0), derivativesWithRespectTo.get(currentNodeArguments.get(0)) + derivativesWithRespectTo.get(currentNode) * 1.0);
					derivativesWithRespectTo.put(currentNodeArguments.get(1), derivativesWithRespectTo.get(currentNodeArguments.get(1)) - derivativesWithRespectTo.get(currentNode) * 1.0);
					break;
				case MULT:
					derivativesWithRespectTo.put(currentNodeArguments.get(0), derivativesWithRespectTo.get(currentNodeArguments.get(0)) + derivativesWithRespectTo.get(currentNode) * currentNodeArguments.get(1).asFloatingPoint());
					derivativesWithRespectTo.put(currentNodeArguments.get(1), derivativesWithRespectTo.get(currentNodeArguments.get(1)) + derivativesWithRespectTo.get(currentNode) * currentNodeArguments.get(0).asFloatingPoint());
					break;
				case DIV:
					break;
				case SQUARED:
					derivativesWithRespectTo.put(currentNodeArguments.get(0), derivativesWithRespectTo.get(currentNodeArguments.get(0)) + derivativesWithRespectTo.get(currentNode) * 2 * currentNodeArguments.get(0).asFloatingPoint());
					break;
				case SQRT:
					derivativesWithRespectTo.put(currentNodeArguments.get(0), derivativesWithRespectTo.get(currentNodeArguments.get(0)) + derivativesWithRespectTo.get(currentNode) / 2 / Math.sqrt(currentNodeArguments.get(0).asFloatingPoint()));
					break;
				}

				// Add all arguments to our queue of nodes we have to work on
				nodesToProcess.addAll(currentNode.getArguments());
			}
		}

		return derivativesWithRespectTo;
	}

	@Override
	public Value getDerivativeWithRespectTo(ValueDifferentiable x) {
		return new ValueDouble(getDerivativeWithRespectTo().getOrDefault(x, 0.0));
	}


	public Operator getOperator() {
		return operator;
	}

	public List<ValueDoubleDifferentiable> getArguments() {
		return arguments;
	}

	Long getID() {
		return id.longValue();
	}
}
