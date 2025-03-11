package exercise_6;

import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя первого пользователя");
        String name1 = scanner.nextLine();
        System.out.println("Введите возраст первого пользователя");
        int age1 = scanner.nextInt();
        scanner.nextLine();

        User user1 = new User(name1, age1);


        System.out.println("Введите имя второго пользователя");
        String name2 = scanner.nextLine();
        System.out.println("Введите возраст второго пользователя");
        int age2 = scanner.nextInt();


        User user2 = new User(name2, age2);


        if (user1.getAge() < user2.getAge()) {
            System.out.println(user1);
        } else if (user2.getAge() < user1.getAge()) {
            System.out.println(user2);
        }
        else {
            System.out.println("У данных польователей возрасты равны");
        }



    }
}