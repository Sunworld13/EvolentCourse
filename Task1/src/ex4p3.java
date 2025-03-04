import java.util.Scanner;

public class ex4p3 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Введите дату в формате 'дд.мм.гггг':");
        String inputDate = scanner.nextLine();


        if (inputDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {

            String[] parts = inputDate.split("\\.");
            String day = parts[0];
            String month = parts[1];
            String year = parts[2];


            String outputDate = year + "-" + month + "-" + day;


            System.out.println(outputDate);
        } else {
            System.out.println("Неверный формат даты. Пожалуйста, используйте формат 'дд.мм.гггг'.");
        }


        scanner.close();
    }
}
