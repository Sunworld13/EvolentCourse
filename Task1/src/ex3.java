import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ex3 {
    public static void main(String[] args) {

        int[] array = new int[20];
        Random random = new Random();


        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(15) + 1;
        }


        System.out.println("Содержимое массива:");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();


        Map<Integer, Integer> countMap = new HashMap<>();

        for (int num : array) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        System.out.println("Числа, которые встречаются больше одного раза:");
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("Число '" + entry.getKey() + "' встречается " + entry.getValue() + " раза");
            }
        }
    }
}
