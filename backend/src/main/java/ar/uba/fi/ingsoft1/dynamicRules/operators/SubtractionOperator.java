package ar.uba.fi.ingsoft1.dynamicRules.operators;

public class SubtractionOperator implements ArithmeticOperator {
    @Override
    public double operate(double currentValue, double newValue) {
        return currentValue - newValue;
    }
}
