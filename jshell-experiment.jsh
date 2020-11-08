// The following can be run in jshell. Launch jshell from this project via the command:     mvn jshell:run

import net.finmath.aadexperiments.value.*;

BiFunction<Value, Value, Value> hypothenuse = (x,y) -> x.squared().add(y.squared()).sqrt();

var x = new ValueDoubleDifferentiable(3.0);
var y = new ValueDoubleDifferentiable(4.0);

var z = hypothenuse.apply(x,y);

var eps = new ValueDoubleDifferentiable(1E-5);

var derivFD = hypothenuse.apply(x.add(eps),y).sub(z).div(eps);

var derivAN = x.div(z);

var derivAD = ((ValueDoubleDifferentiable) z).getDerivativeWithRespectTo(x);
