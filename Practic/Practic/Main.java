package Practic;

import java.util.Scanner;

class TaxCalculator {
    public double calculateTax(double income, Calculator calculator) {
        return calculator.calculate(income);
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaxCalculator taxCalculator = new TaxCalculator();

        System.out.println("Выберите тип налогоплательщика:");
        System.out.println("1 - Физ.лицо");
        System.out.println("2 - Самозанятый");
        System.out.println("3 - ИП");

        int choice = scanner.nextInt();

        System.out.println("Введите доход:");
        double income = scanner.nextDouble();

        Calculator calculator = null;

        switch (choice) {
            case 1:
                calculator = new IndividualTaxCalculator();
                break;
            case 2:
                calculator = new SelfEmployedTaxCalculator();
                break;
            case 3:
                calculator = new IpTaxCalculator();
                break;
            default:
                System.out.println("Error");
                System.exit(0);
        }
        double tax = taxCalculator.calculateTax(income, calculator);
        System.out.println("Налог составляет: " + tax);
    }
}
