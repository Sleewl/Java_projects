package Interface;

public class Main{
    public static void main(String[] args){
        Sedan sedan = new Sedan();
        Truck truck = new Truck();
        UnusualCar unusualCar = new UnusualCar();
        truck.gas();
        sedan.brake();
        unusualCar.gas();
        unusualCar.brake();
        unusualCar.stop();
    }
}

class UnusualCar implements Car,Bus {
    public void gas() {
        System.out.println("Эта машина газует по-другому!");
    }
    public void brake() {
        System.out.println("Эта машина тормозит по-другому!");
    }
    public void stop(){
        System.out.println("Этот автобус ОСТАНОВИЛСЯ");
    }
}

interface Car {
    default void gas() {
        System.out.println("Газ!");
    }
    default void brake() {
        System.out.println("Тормоз!");
    }
}

interface Bus{
    default void stop(){
        System.out.println("ОСТАНОВКА!");
    }
}



class Sedan implements Car {

}
class Truck implements Car {

}


