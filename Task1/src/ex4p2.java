import java.util.Scanner;

public class ex4p2 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.print("Введите строку: ");
        String string = scanner.nextLine();


        String outputString = string
                .replaceAll("кака", "вырезано цензурой")
                .replaceAll("бяка", "вырезано цензурой");


        System.out.println(outputString);


        scanner.close();
    }
}