import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ex5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


        System.out.println("Введите дату в формате dd.MM.yyyy:");
        String inputDate1 = scanner.nextLine();
        Date date1 = null;




        try {
            date1 = sdf.parse(inputDate1);
            String CheckDdate = sdf.format(date1);
            boolean b = CheckDdate.equals(inputDate1);
            if (!b){
                System.out.println("Ошибка: вы ввели некорректный день или месяц");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Неверный формат даты.");
            return;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_MONTH, 45);
        Date dateAfter45Days = calendar.getTime();
        System.out.println("Дата после увеличения на 45 дней: " + sdf.format(dateAfter45Days));


        calendar.setTime(date1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfYear = calendar.getTime();
        System.out.println("Дата после сдвига на начало года: " + sdf.format(startOfYear));


        int workingDaysToAdd = 10;
        int addedDays = 0;
        while (addedDays < workingDaysToAdd) {
            calendar.setTime(date1);
            calendar.add(Calendar.DAY_OF_MONTH, addedDays + 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                addedDays++;
            }
        }
        Date dateAfter10WorkingDays = calendar.getTime();
        System.out.println("Дата после увеличения на 10 рабочих дней: " + sdf.format(dateAfter10WorkingDays));


        System.out.println("Введите вторую дату в формате dd.MM.yyyy:");
        String inputDate2 = scanner.nextLine();
        Date date2 = null;


        try {
            date2 = sdf.parse(inputDate2);
        } catch (ParseException e) {
            System.out.println("Неверный формат даты.");
            return;
        }


        int workingDaysBetween = 0;
        calendar.setTime(date1);
        while (calendar.getTime().before(date2) || calendar.getTime().equals(date2)) {
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workingDaysBetween++;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        System.out.println("Количество рабочих дней между введенными датами: " + workingDaysBetween);


        scanner.close();
    }
}
