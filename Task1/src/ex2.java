import java.util.Scanner;

public class ex2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите 3 числа");
        System.out.println("Ввод: ");
        int a = scanner.nextInt();
        System.out.println("Ввод: ");
        int b = scanner.nextInt();
        System.out.println("Ввод: ");
        int c = scanner.nextInt();

        boolean bool = false;

        if (a % 5 == 0) {
            System.out.print("a=" + a);
            bool = true;
        }
        if (b%5==0)
        {
            System.out.print(",");
        }

        System.out.print(" ");

        if (b % 5 == 0) {
            System.out.print("b=" + b + " ");
            bool = true;
        }
        if (c%5==0)
        {
            System.out.print(",");
        }

        System.out.print("");

        if (c % 5 == 0) {
            System.out.print("c=" + c + " ");
            bool = true;
        }

        if (!bool) {
            System.out.println("нет значений, кратных 5");
        } else {
            System.out.println();
        }
        System.out.println("Целочисленное деление a на b: " + (a / b));

        System.out.println("Деление a на b (число с плавающей запятой): " + ((double) a / b));

        System.out.println("Деление a на b, округленное до ближайшего целого в большую сторону: " + Math.ceil((double) a / b));

        System.out.println("Деление a на b, округленное до ближайшего целого в меньшую сторону: " + Math.floor((double) a / b));

        System.out.println("Деление a на b, округленное до ближайшего целого: " + Math.round((double) a / b));

        System.out.println("Остаток от деления b на c: " + (b % c));

        System.out.println("Наименьшее значение из a и b: " + Math.min(a, b));

        System.out.println("Наибольшее значение из b и c: " + Math.max(b, c));

        scanner.close();
    }
}