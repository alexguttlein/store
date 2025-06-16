package ar.uba.fi.ingsoft1.dynamicRules.operators;

public class AdditionOperator implements ArithmeticOperator {
    @Override
    public double operate(double currentValue, double newValue) {
        return currentValue + newValue;
    }
}
