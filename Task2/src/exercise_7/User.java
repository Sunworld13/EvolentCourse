package exercise_7;

class User {
    private String name;
    private Integer age;

    // Конструктор класса
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // Метод toString
    @Override
    public String toString() {
        return name + ", возраст " + age + " лет";
    }
}
