package ex12;

public class Divider implements Operation {
    @Override
    public double getResult(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Деление на ноль невозможно!");
        }
        return a / b;
    }
}