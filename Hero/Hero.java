package PackageOne;
import java.util.Scanner;
//[метод доступа][прочие методы][тип возвращаемого значения] имя метода(параметры)
public class Hero {
    public static void main(String[] args) {

    }
    //Дано целое число(int), складывать цифры числа до тех пор, пока не останется одна цифра.
//Ввод: а) из args
//      б) из консоли + проверка ввода обоих пунктов на корректность
//Пример 38 -> 3 + 8 -> 11 -> 1 + 1 -> 2

    public class Main {

        public static void main(String[] arg) {
            try {
                int numb_1 = Integer.parseInt(arg[0]);

                if (numb_1 < 0){
                    throw new RuntimeException();
                }

                System.out.println("Answer a: ");
                System.out.println(calculate(numb_1));
            }

            catch (Exception err){
                System.out.println("Error!Please enter the natural number.");
            }
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Write the number: ");
                int numb_2 = in.nextInt();

                if (numb_2 < 0){
                    throw new RuntimeException();
                }

                System.out.println("Answer b: ");
                System.out.println(calculate(numb_2));
            }
            catch (Exception err2){
                System.out.println("Error!Please enter the natural number.");
            }
        }

        public static int calculate(int arg){
            while (arg > 9) {
                int sum = 0;
                while (arg != 0) {
                    sum += arg % 10;
                    arg /= 10;
                }
                arg = sum;
            }
            return arg;
        }
    }
