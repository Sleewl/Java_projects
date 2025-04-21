import java.util.Scanner;

class Attraction {
    String name;
    double price;

    public Attraction(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void ride() {
        System.out.println("Вы катаетесь на " + name);
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

interface User {
    void ride(Attraction attraction);
}

class VirtualUser implements User {
    private double balance;

    public VirtualUser(double balance) {
        this.balance = balance;
    }

    @Override
    public void ride(Attraction attraction) {
        while (balance >= attraction.getPrice()) {
            attraction.ride();
            balance -= attraction.getPrice();
            System.out.println("Остаток на балансе: $" + balance);
        }
        System.out.println("Денег больше нет.");
    }
}

class RealUser implements User {
    private double balance;

    public RealUser(double balance) {
        this.balance = balance;
    }

    @Override
    public void ride(Attraction attraction) {
        if (balance >= attraction.getPrice()) {
            attraction.ride();
            balance -= attraction.getPrice();
            System.out.println("Вы покатались на " + attraction.getName());
            System.out.println("Остаток на балансе: $" + balance);
        } else {
            System.out.println("Недостаточно денег на " + attraction.getName());
        }
    }

    public void chooseRide(Attraction[] attractions) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите аттракцион (или введите 'exit' для выхода):");
            for (int i = 0; i < attractions.length; i++) {
                System.out.println((i + 1) + ". " + attractions[i].getName() + " ($" + attractions[i].getPrice() + ")");
            }

            String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("Выход из парка.");
                break;
            }

            int choice;
            try {
                choice = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Попробуйте еще раз.");
                continue;
            }

            if (choice >= 0 && choice < attractions.length) {
                ride(attractions[choice]);
            } else {
                System.out.println("Некорректный выбор. Попробуйте еще раз.");
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Использование: java AmusementPark -V <баланс> или -U <баланс>");
            return;
        }

        String userType = args[0];
        double balance = Double.parseDouble(args[1]);

        Attraction[] attractions = {
                new Attraction("Американские горки", 5.0),
                new Attraction("Колесо обозрения", 3.0),
                new Attraction("Автодром", 2.0)
        };

        if (userType.equals("-V")) {
            VirtualUser virtualUser = new VirtualUser(balance);
            System.out.println("Виртуальный пользователь. Баланс: $" + balance);
            for (Attraction attraction : attractions) {
                virtualUser.ride(attraction);
            }
            
        } else if (userType.equals("-U")) {
            RealUser realUser = new RealUser(balance);
            System.out.println("Реальный пользователь. Баланс: $" + balance);
            realUser.chooseRide(attractions);

        } else {
            System.out.println("Неизвестный тип пользователя. Используйте -V для виртуального или -U для реального.");
        }
    }
}
