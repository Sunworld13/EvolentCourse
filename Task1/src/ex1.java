import java.util.Scanner;

public class ex1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Как тебя зовут?");

        String name = scanner.nextLine();

        System.out.println("Привет, " + name + "!");

        scanner.close();
    }
}
