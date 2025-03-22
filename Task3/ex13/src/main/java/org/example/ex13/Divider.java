package org.example.ex13;
import org.springframework.stereotype.Component;

@Component("divider")
public class Divider implements Operation{
    @Override
    public double getResult(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Деление на ноль невозможно!");
        }
        return a / b;
    }
}