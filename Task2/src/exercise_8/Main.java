package exercise_8;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, List<User>> userMap = new HashMap<>();


        for (int i = 1; i <= 5; i++) {
            System.out.println("Введите имя пользователя " + i);
            String name = scanner.nextLine();
            System.out.println("Введите возраст пользователя " + i);
            int age = scanner.nextInt();
            scanner.nextLine();


            User user = new User(name, age);


            if (userMap.containsKey(age)) {
                userMap.get(age).add(user);
            } else {
                List<User> userList = new ArrayList<>();
                userList.add(user);
                userMap.put(age, userList);
            }
        }


        System.out.println("Введите требуемый возраст");
        int requiredAge = scanner.nextInt();


        if (userMap.containsKey(requiredAge)) {

            List<User> users = userMap.get(requiredAge);


            users.sort(Comparator.comparing(User::getName));


            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("Пользователь с возрастом '" + requiredAge + "' не найден");
        }
    }
}
