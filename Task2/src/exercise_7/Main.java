package exercise_7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            System.out.println("Введите имя пользователя " + i);
            String name = scanner.nextLine();
            System.out.println("Введите возраст пользователя " + i);
            int age = scanner.nextInt();
            scanner.nextLine();


            users.add(new User(name, age));
        }


        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getAge().compareTo(u2.getAge());
            }
        });


        for (User user : users) {
            System.out.println(user);
        }
    }
}