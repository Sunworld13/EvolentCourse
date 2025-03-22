package org.example.ex13;
import org.springframework.stereotype.Component;

@Component("adder")
public class Adder implements Operation {
    @Override
    public double getResult(double a, double b) {
        return a + b;
    }
}