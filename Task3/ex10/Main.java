package ex10;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        long sum = getArithmeticProgressionSum(10_000_000, 1_000_000_000);
        System.out.println("Сумма чисел между 10_000_000 и 1_000_000_000: " + sum);
    }

    
    public static long getArithmeticProgressionSum(int a, int b) {
        return IntStream.range(a, b).asLongStream().sum();
    }
}
