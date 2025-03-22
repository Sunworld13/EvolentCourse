package ex9;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class StudentWithClock extends Student {
    @Override
    public void learn() {

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("Текущее время: " + currentTime.format(formatter));


        super.learn();


        currentTime = LocalTime.now();
        System.out.println("Текущее время: " + currentTime.format(formatter));
    }
}
