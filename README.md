# Computational Finance Lecture - Algorithmic Differentiation

This repository contains a simple implementation of an adjoint algorithmic differentiation
(backward algorithmic differentiation, backward automatic differentiation) (AAD).

The material is used during the lecture *Applied Mathematical Finance* / *Computational Finance 2* (Winter 2020/2021).

## Decription

### Interfaces

An interface `Value` is defined that specifies possible arithmetic operations like (add, sub, mult, div, squared, sqrt). The function `hypothenuse` used in the program `DiffernetiationExperimentHypothenuse` performs all its calculation in terms of this interfac.
This allows to "inject" different implementations (a technique related to *dependency injection*).

### Implementations

The class `ValueDouble` implements the interface `Value` by performing all valuations on a floating point Double.

The class `ValueDoubleDifferntiable` implements the interface `Value` by performing all valuations on a floating point Double, but in addition tracking all operations to allow for an algorithmic differentiation.

## Running

### From Eclipse

Run the class `DiffernetiationExperimentHypothenuse` from Eclipse (right-click, Run As -> Java Applications).

### Via JShell.

Run in a command shell (Windows CMD or macOS Terminal): `mvn compile jshell:run`

Then type (or copy) the following:

```
import net.finmath.aadexperiments.value.*;

BiFunction<Value, Value, Value> hypothenuse = (x,y) -> x.squared().add(y.squared()).sqrt();

var x = new ValueDoubleDifferentiable(3.0);
var y = new ValueDoubleDifferentiable(4.0);

var z = hypothenuse.apply(x,y);

var eps = new ValueDoubleDifferentiable(1E-5);

var derivAN = x.div(z);		// Analytic

var derivFD = hypothenuse.apply(x.add(eps),y).sub(z).div(eps);		// Finite Difference

var derivAD = ((ValueDifferentiable) z).getDerivativeWithRespectTo(x);		// Algorithmic Differentiation
```
