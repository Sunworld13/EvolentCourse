import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ex4p4 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.print("Введите дату в формате 'дд.мм.гггг': ");
        String inputDate = scanner.nextLine();


        SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        try {

            Date date = inputFormat.parse(inputDate);
            String date1 = inputFormat.format(date);
            boolean b = date1.equals(inputDate);
            if (!b){
                System.out.println("Ошибка: вы ввели некорректный день или месяц");
                return;
            }

            String outputDate = outputFormat.format(date);


            System.out.println(outputDate);
        } catch (ParseException e) {

            System.out.println("Ошибка: введенная дата не соответствует формату 'дд.мм.гггг'.");
        }


        scanner.close();
    }
}