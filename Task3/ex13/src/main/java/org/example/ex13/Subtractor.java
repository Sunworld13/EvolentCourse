package org.example.ex13;
import org.springframework.stereotype.Component;

@Component("subtractor")
public class Subtractor implements Operation {
    @Override
    public double getResult(double a, double b) {
        return a - b;
    }
}